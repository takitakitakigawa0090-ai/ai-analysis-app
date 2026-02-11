# 実行環境として軽量なJREを使用
FROM eclipse-temurin:21-jre-jammy

# コンテナ内の作業ディレクトリ
WORKDIR /app

# ビルドされたJARファイルをコピー（名前はpom.xmlの設定に合わせて適宜修正）
COPY target/*.jar app.jar

# アプリケーションを実行
ENTRYPOINT ["java", "-jar", "app.jar"]