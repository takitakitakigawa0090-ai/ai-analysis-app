package com.aiapps.aiapp;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * AI分析アプリケーションのメインエントリーポイントクラス
 * Spring Bootアプリケーションの起動および初期化設定を管理します。
 * 
 * @author AiApp
 */
@SpringBootApplication
public class AiAppApplication {

	/**
	 * アプリケーションを起動
	 * SpringApplication.run を呼び出して、組み込みのWebサーバー（Tomcat等）を起動、
	 * Springのコンテキスト（DIコンテナ）を初期化
	 * @param args 起動時に渡されるコマンドライン引数
	 */
	public static void main(String[] args) {
		// 起動時にタイムゾーンを日本に設定
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"));
		SpringApplication.run(AiAppApplication.class, args);
	}

}
