package com.aiapps.aiapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * アプリケーションのセキュリティ設定を管理する構成クラス
 * Google OAuth2 による認証フロー、特定のURLパスに対するアクセス権限、
 * および静的リソース（CSS/JS/Favicon）の公開設定を定義
 * 
 * @author AiApp
 */
@Configuration
public class SecurityConfig {

    // ロガーの定義
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    /**
     * セキュリティフィルターチェーンの設定
     * 
     * @param http HttpSecurityオブジェクト
     * @return 構成済みのSecurityFilterChainオブジェクト
     * @throws Exception セキュリティ設定の構築中に発生する可能性のある例外
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        logger.debug("CsecurityFilterChainの設定を開始します。");

        // URLパスごとのアクセス権限設定
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/css/**", "/js/aiapp-script.js", "/favicon.ico",
                        "/api/auth/check") // 許可するパスを指定
                .permitAll() // これらのパスは認証不要でアクセス可能
                .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .loginProcessingUrl("/auth2/callback") // OAuth2認証後のコールバックURL
                        .loginPage("/login") // カスタムログインページのURL
                        .defaultSuccessUrl("/aiAnalysis", true) // 認証成功後のリダイレクト先URL
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") // ログアウト成功後のリダイレクト先URL
                        .invalidateHttpSession(true) // セッションを無効化
                        .deleteCookies("JSESSIONID")) // セッションCookieを削除

                // Spring Securityの設定内でキャッシュ制御を追加
                .headers(headers -> headers
                        .cacheControl(cache -> cache.disable())) // キャッシュ制御を無効化

                .exceptionHandling(ex -> ex
                        // 期限切れや認証エラー時に強制ログアウト
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/login?error=expired"); // ログインページへリダイレクト
                        }));

        return http.build(); // 構成済みのSecurityFilterChainを返す

    }
}
