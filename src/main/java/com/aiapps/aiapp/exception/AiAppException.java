package com.aiapps.aiapp.exception;

/**
 * AIアプリケーション固有の例外を表すカスタム例外クラス
 * アプリケーション内で発生する特定のエラー条件を示すために使用
 * 
 * @author AiApp
 */
public class AiAppException extends RuntimeException {
    /**
     * コンストラクタ
     * @param message エラーメッセージ
     */
    public AiAppException(String message) {
        super(message);
    }

    /** コンストラクタ
     * @param message エラーメッセージ
     * @param cause    発生した元の例外
     */
    public AiAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
