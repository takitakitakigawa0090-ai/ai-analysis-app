package com.aiapps.aiapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aiapps.aiapp.service.AiService;

/**
 * AIによる文章分析機能の画面遷移およびリクエスト制御を担当するコントローラー
 * ユーザーからの入力情報の受け取り、AIサービスによる分析実行、
 * 分析結果画面への遷移を管理
 * 
 * @author AiApp
 */
@Controller
public class AiAnalyzeController {

    // ロガーの定義
    private static final Logger logger = LoggerFactory.getLogger(AiAnalyzeController.class);

    // AIサービスの注入
    private final AiService aiService;

    private static final String VIEW_INDEX = "login";
    private static final String VIEW_AI_ANALYSIS = "aiAnalysis";

    /**
     * コンストラクタ：AIサービスを注入
     * 
     * @param aiService 注入するAIサービス
     */
    public AiAnalyzeController(AiService aiService) {
        this.aiService = aiService;
    }

    /**
     * 文章カテゴライズ開始画面へのGETリクエストを処理
     * 
     * @return 文章カテゴライズ開始画面へのテンプレート名
     */
    @GetMapping({ "/", "/login" })
    public String index() {
        return VIEW_INDEX;
    }

    /**
     * 文章カテゴライズ画面へのGETリクエストを処理
     * 
     * @return 文章カテゴライズ画面へのテンプレート名
     */
    @GetMapping("/aiAnalysis")
    public String aiAnalysis() {
        return VIEW_AI_ANALYSIS;
    }

    /**
     * AI分析を実行するためのPOSTリクエストを処理
     * 
     * @param userInput ユーザー入力文字列
     * @param model     モデルオブジェクト（画面に渡すデータを保持）
     * @return 分析結果画面へのテンプレート名
     */
    @PostMapping("/analyze")
    public String analyze(@RequestParam("aiInput") String userInput, Model model) {

        // 入力値チェック
        String errorMessage = aiService.getInputErrorMessage(userInput);
        if (!errorMessage.isEmpty()) {

            logger.debug("入力エラー: {}", errorMessage);
            model.addAttribute("errorMessage", errorMessage);

        } else {

            // AIサービスを呼び出して分析を実行
            String result = aiService.callGeminiApi(userInput);
            // 画面に入力値と分析結果を渡す
            model.addAttribute("userInput", userInput);
            model.addAttribute("analysisResult", result);

        }

        return VIEW_AI_ANALYSIS;
    }

}
