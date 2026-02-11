package com.aiapps.aiapp.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /**
     * すべての例外をキャッチして処理するハンドラー
     * 
     * @param ex    発生した例外オブジェクト
     * @param model エラーメッセージを画面に渡すためのモデルオブジェクト
     * @return エラー画面のテンプレート名
     */
    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex, Model model) {
        
        // 開発者用にログを記録（スタックトレースを出す）
        logger.error("予期せぬシステムエラーが発生しました: ", ex);

        // ユーザーへのメッセージを設定
        model.addAttribute(ERROR_ATTRIBUTE, "一時的なシステムエラーが発生しました。時間をおいて再度お試しください。");

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
}
