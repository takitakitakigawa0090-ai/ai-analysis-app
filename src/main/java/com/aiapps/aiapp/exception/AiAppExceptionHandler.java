package com.aiapps.aiapp.exception;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.ClientAuthorizationRequiredException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * アプリ全体の例外をキャッチして処理するためのクラス
 * 予期せぬエラーが発生した際に、ユーザーにわかりやすいエラーメッセージを表示し、
 * 開発者には詳細なログを提供することを目的。
 * 
 * @author AiApp
 */
@ControllerAdvice
public class AiAppExceptionHandler {

    // ロガーの定義
    private static final Logger logger = LoggerFactory.getLogger(AiAppExceptionHandler.class);
    // 定数定義
    private static final String ERROR_ATTRIBUTE = "errorMessage";
    private static final String VIEW_ERROR = "error";
    private static final String VALUE_TOO_MANY_REQUESTS = "429";
    private static final String VALUE_CONNECT_ERROR = "CONNECT_ERROR";

    /**
     * すべての例外をキャッチして処理するハンドラー
     * 
     * @param ex    発生した例外オブジェクト
     * @param model エラーメッセージを画面に渡すためのモデルオブジェクト
     * @return エラー画面のテンプレート名
     */
    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex, Model model) {

        if (ex.getMessage().contains(VALUE_TOO_MANY_REQUESTS)) {

            logger.warn("現在AIが混み合っています。1分ほど置いてから再度お試しください。エラー内容:{} ", ex.getMessage());

            // ユーザーへのメッセージを設定
            model.addAttribute(ERROR_ATTRIBUTE, "現在AIが混み合っています。1分ほど置いてから再度お試しください。");

        } else if (ex.getMessage().contains(VALUE_CONNECT_ERROR)) {
            logger.warn("外部サービスへの接続に失敗しました。時間をおいて再度お試しください。");
            // ユーザーへのメッセージを設定
            model.addAttribute(ERROR_ATTRIBUTE, "外部サービスへの接続に失敗しました。時間をおいて再度お試しください。");
        } else {
            // 開発者用にログを記録（スタックトレースを出す）
            logger.error("予期せぬシステムエラーが発生しました: ", ex);

            // ユーザーへのメッセージを設定
            model.addAttribute(ERROR_ATTRIBUTE, "一時的なシステムエラーが発生しました。時間をおいて再度お試しください。");

        }

        // error.html を表示
        return VIEW_ERROR;
    }

    /**
     * リクエストのパラメータが不正な場合の例外をキャッチして処理するハンドラー
     * 
     * @param ex    発生した例外オブジェクト
     * @param model エラーメッセージを画面に渡すためのモデルオブジェクト
     * @return エラー画面のテンプレート名
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleBadRequest(Exception ex, Model model) {

        // 開発者用にログを記録（例外のメッセージを出す）
        logger.warn("不正なリクエストが送信されました {}: ", ex.getMessage());

        // ユーザーへのメッセージを設定
        model.addAttribute(ERROR_ATTRIBUTE, "リクエストの内容が正しくありません。");

        // error.html を表示
        return VIEW_ERROR;
    }

    /**
     * 存在しないURLへのアクセスをキャッチして処理するハンドラー
     * 
     * @param ex    発生した例外オブジェクト
     * @param model エラーメッセージを画面に渡すためのモデルオブジェクト
     * @return エラー画面のテンプレート名
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(NoHandlerFoundException ex, Model model) {

        // 開発者用にログを記録（アクセスされたURLを出す）
        logger.info("存在しないページへのアクセス {}: ", ex.getRequestURL());

        // ユーザーへのメッセージを設定
        model.addAttribute(ERROR_ATTRIBUTE, "お探しのページは見つかりませんでした。");

        // error.html を表示
        return VIEW_ERROR;
    }

    /**
     * ネットワーク接続エラーをキャッチして処理するハンドラー
     * 
     * @param ex    発生した例外オブジェクト
     * @param model エラーメッセージを画面に渡すためのモデルオブジェクト
     * @return エラー画面のテンプレート名
     */
    @ExceptionHandler(ConnectException.class)
    public String handleConnectException(ConnectException ex, Model model) {
        // HTTP 503 Service Unavailable
        logger.error("外部サービスへの接続に失敗しました。", ex);
        model.addAttribute(ERROR_ATTRIBUTE, "外部サービスへの接続に失敗しました。時間をおいて再度お試しください。");
        return VIEW_ERROR;
    }

    /**
     * ソケットタイムアウトエラーをキャッチして処理するハンドラー
     * 
     * @param ex    発生した例外オブジェクト
     * @param model エラーメッセージを画面に渡すためのモデルオブジェクト
     * @return エラー画面のテンプレート名
     */
    @ExceptionHandler(SocketTimeoutException.class)
    public String handleSocketTimeoutException(SocketTimeoutException ex, Model model) {
        // HTTP 504 Gateway Timeout
        logger.error("外部サービスへの接続がタイムアウトしました。", ex);
        model.addAttribute(ERROR_ATTRIBUTE, "外部サービスへの接続がタイムアウトしました。時間をおいて再度お試しください。");
        return VIEW_ERROR;
    }

    /**
     * 認証が必要な操作で認証されていない場合の例外をキャッチして処理するハンドラー
     * 
     * @return HTTP 401 Unauthorized のレスポンス
     */
    @ExceptionHandler(ClientAuthorizationRequiredException.class)
    public ResponseEntity<Map<String, String>> handleAuthException() {
        // HTTP 401を返す
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
