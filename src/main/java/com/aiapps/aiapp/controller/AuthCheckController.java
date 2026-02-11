package com.aiapps.aiapp.controller;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 認証状態を確認するためのコントローラー
 * Google OAuth2 認証の状態を確認するためのエンドポイントを提供
 * クライアントのアクセストークンの有効期限をチェックし 、
 * HTTP 200（OK）または HTTP 401（Unauthorized）を返す
 * すべてのメソッドがデータを直接返すため、@RestController を使用
 * これにより SonarLint の java:S4601 警告を解消し、@ResponseBody を省略
 * 
 * @author AiApp
 */
@RestController
public class AuthCheckController {

    // ロガーの定義
    private static final Logger logger = LoggerFactory.getLogger(AuthCheckController.class);

    /**
     * 認証状態を確認するエンドポイント
     * クライアントのアクセストークンの有効期限をチェックし、HTTP 200（OK）または HTTP 401（Unauthorized）を返す
     * 
     * @param client 登録されたOAuth2AuthorizedClientオブジェクト
     * @return 認証状態に応じたHTTPレスポンス
     */
    @GetMapping("/api/auth/check")
    public ResponseEntity<Void> checkAuth(
            @RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient client) {
        // ログ出力
        logger.debug("認証状態を確認します。クライアント: {}", client);
        // クライアントとアクセストークンの有効期限を確認
        if (client == null) {
            logger.warn("OAuth2AuthorizedClientがnullです。認証されていません。");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // アクセストークンの有効期限を取得
        Instant expiresAt = client.getAccessToken().getExpiresAt();

        // アクセストークンの有効期限を確認し、HTTPステータスを返す
        if (expiresAt != null && expiresAt.isAfter(Instant.now())) {
            // 日本時間に変換
            ZonedDateTime expiresAtJst = expiresAt.atZone(ZoneId.of("Asia/Tokyo"));
            logger.debug("トークンは有効です。期限: {}", expiresAtJst);
            return ResponseEntity.ok().build();
        }

        // トークンが期限切れの場合は、HTTP 401を返す
        logger.warn("トークンの期限が切れています。期限: {}", expiresAt);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
