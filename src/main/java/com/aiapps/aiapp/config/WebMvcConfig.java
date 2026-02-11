package com.aiapps.aiapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

/**
 * Web MVCの設定を管理する構成クラス
 * 静的リソース（CSS、JavaScript、Faviconなど）のハンドリングを定義
 * 
 * @author AiApp
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

        // ロガーの定義
        private static final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

        /**
         * 静的リソース（CSS、JavaScript、画像、ファビコン等）のハンドリングを構成
         * 
         * application.properties で自動マッピングを無効化（add-mappings=false）しているため、
         * このメソッドを使用して、特定のURLパスと物理的なリソース場所を明示的に紐付け
         * 
         * @param registry リソースハンドラを登録するためのレジストリ
         */
        @Override
        public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
                logger.debug("addResourceHandlersが呼び出されました。静的リソースのハンドリングを設定します。");

                // 「/css/**」というURLでアクセスが来たら、
                // 「classpath:/static/css/」フォルダの中身を返す、という設定
                // CSSファイルの場所を指定
                registry.addResourceHandler("/css/**")
                                .addResourceLocations("classpath:/static/css/");

                // JavaScriptファイルの場所を指定
                registry.addResourceHandler("/js/**")
                                .addResourceLocations("classpath:/static/js/");

                // ファビコンの場所を指定
                registry.addResourceHandler("/favicon.ico")
                                .addResourceLocations("classpath:/static/");
        }
}
