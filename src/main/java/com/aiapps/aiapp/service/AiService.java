package com.aiapps.aiapp.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aiapps.aiapp.exception.AiAppException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AI分析のビジネスロジックを担当するサービスクラス
 * Gemini APIを呼び出してAI分析を実行する機能や、入力値のエラーメッセージを取得する機能を提供
 * 
 * @author AiApp
 */
@Service
public class AiService {

	// ロガーの定義
	private static final Logger logger = LoggerFactory.getLogger(AiService.class);

	// application.properties で定義したキー名で参照 
    @Value("${google.api.key}")
    private String apiKey; 

	// HttpClientは使い回す（リソースの節約とjava:S2095対策）
	// Java 21の仮想スレッドを使用する場合も、共通のクライアントを使う
	private static final HttpClient CLIENT = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(10)) // 接続タイムアウト設定
			.build();

	// ObjectMapperも使い回すことでパフォーマンス向上
	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * Gemini APIを呼び出してAI分析を実行する
	 * 
	 * @param userInput ユーザー入力文字列
	 * @return AIからの分析結果テキスト
	 */
	public String callGeminiApi(String userInput) {

		// ログ出力
		logger.info("AI分析を開始します。入力内容: {}", userInput);

		// APIキーとエンドポイントURLの設定
		String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key="
				+ apiKey;
		try {
			String systemPrompt = "Translate input to English, then pick one from [スポーツ, テクノロジー, 政治, 経済, エンターテイメント, 医療, 教育, ビジネス, ニュース, その他]. Output 1 word only. Input:";
			String combinedInput = systemPrompt + userInput;

			// JSONリクエストボディの作成
			String jsonBody = """
					{
					    "contents": [{
					        "parts": [{
					            "text": "%s"
					        }]
					    }]
					}
					""".formatted(combinedInput.replace("\"", "\\\"").replace("\n", "\\n"));

			// POSTリクエストの構築
			HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(30)) // タイムアウト設定	
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                    .build();

			// 送信して結果を受け取る
			HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
			// レスポンスの処理
			if (response == null) {
				// nullチェック
				logger.error("AIからの応答がnullでした。");
				throw new AiAppException("AIからの応答が空でした");

				// 正常に応答が返ってきた場合
			} else if (response.statusCode() == 200) {
				// JSONをパース
				JsonNode root = MAPPER.readTree(response.body()); 

				// 必要なデータを抽出
				// 構造: candidates[0] -> content -> parts[0] -> text
				String aiText = root.path("candidates").get(0)
						.path("content")
						.path("parts").get(0)
						.path("text").asText();

				logger.info("AI分析が正常に完了しました。");
				// 分析結果を返す
				return aiText;

				// ステータスコード200以外のエラー処理
			} else {
				// ボディの最初の100文字のみログに出す
				String partialBody = response.body().length() > 100
						? response.body().substring(0, 100) + "..."
						: response.body();
				logger.warn("APIエラー : statusCode={}, body={}", response.statusCode(), partialBody);
				throw new AiAppException(
						"APIエラー : response.statusCode()= " + response.statusCode() + ", response.body()= "
								+ response.body());
			}

		} catch (IOException e) {
			throw new AiAppException("システムエラー: " + e);
		} catch (InterruptedException e) {
			// ログを出す
			logger.warn("API呼び出しが中断されました");

			// 中断フラグを再設定する (SonarLint対策)
			Thread.currentThread().interrupt();
			// 例外をラップして再スロー
			throw new AiAppException("処理が中断されました", e);
		}
	}

	/**
	 * 入力値のエラーメッセージを取得する
	 * 
	 * @param userInput ユーザー入力文字列
	 * @return エラーメッセージ（エラーがない場合は空文字）
	 */
	public String getInputErrorMessage(String userInput) {

		// 未入力チェック
		if (userInput == null || userInput.trim().isEmpty()) {
			return "エラー：文章を入力してください。";
		}
		// 文字数チェック
		if (userInput.length() > 60) {
			return "エラー：60文字以内で入力してください。";
		}

		return "";
	}
}
