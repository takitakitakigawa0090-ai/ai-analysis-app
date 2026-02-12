AI Analysis App

入力されたテキストを AI（Gemini API）が分析し、最適なカテゴリーに分類する Spring Boot アプリケーションです。



🌟 特徴

　AI 分類: Gemini 2.5 Flash モデルにて動作確認済み。日本語の入力を英語に翻訳してから高精度に分類します。

　モダンな環境: Java 21 と Spring Boot 3.4.2 を採用。

　簡単起動: Docker Compose により、コマンド一つで環境構築が完了します。

　セキュア: API キーなどの機密情報は環境変数（.env）で安全に管理。
 


🛠 動作環境

　AI Model: Gemini 2.5 Flash (API Key が必要)

　Container: Docker / Docker Compose

　Auth: Google OAuth2 クライアント ID / シークレット


🚀 クイックスタート

　1. リポジトリをクローン
 

　　Bash

  
　　git clone https://github.com/takitakitakigawa0090-ai/ai-analysis-app.git
  
　　cd ai-analysis-app

　2. 環境変数の設定（ポーティングガイド）

 
　　別の環境で本アプリを動かす場合は、以下の手順に従ってください。

　　APIキーの準備
  
　　　Google AI Studio で Gemini API キーを取得。

　　　Google Cloud Console で OAuth 2.0 クライアント ID を作成。

　　環境変数の配置
  
　　　プロジェクトのルートにある .env.example をコピーして .env ファイルを作成し、取得したキーを記述してください。

　　Bash
  
  　　cp .env.example .env
    
　　.env の編集内容:

　　Plaintext

　　GOOGLE_API_KEY=あなたのAPIキー

　　GOOGLE_CLIENT_ID=あなたのクライアントID

　　GOOGLE_CLIENT_SECRET=あなたのクライアントシークレット


　3. アプリの起動
 
　　Docker を使用してビルドおよび起動を行います。

　　Bash

　　アプリのパッケージング
  
　　./mvnw package -DskipTests

　　Dockerコンテナのビルドと起動
  
　　docker-compose up --build -d
  
　　Note: コンテナ内で Maven ビルドが走り、JRE 21 環境でアプリが立ち上がります。

　　起動後、ブラウザで http://localhost:8080 にアクセスしてください。
  
  


📖 操作ガイド

ログイン: 文章カテゴライズ開始画面の「文章をカテゴライズ」ボタンをクリック。

入力: テキストボックスに分析したい文章を入力（60文字以内）。

結果表示: AIが翻訳と分析を行い、最適なカテゴリを即座に返します。




📁 プロジェクト構成

src/main/java: アプリケーションのロジック（Java 21）

src/main/resources: Thymeleaf テンプレート、設定ファイル

Dockerfile: アプリ実行用の軽量 JRE 環境定義

docker-compose.yml: 環境変数およびコンテナの実行定義

.env.example: 環境変数のテンプレートファイル
