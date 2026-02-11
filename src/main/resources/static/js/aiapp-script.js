// ページがブラウザのキャッシュから読み込まれた際に発生するイベントを監視
window.addEventListener('pageshow', function (event) {
    // persisted が true の場合、ブラウザのキャッシュ（戻るボタンなど）から読み込まれたことを示す
    if (event.persisted) {
        // 自作ダイアログを隠す
        const modal = document.getElementById('customDialog');
        if (modal) modal.style.display = 'none';

        // ローディング（ぐるぐる）を隠す
        const overlay = document.getElementById('loadingOverlay');
        if (overlay) overlay.style.display = 'none';

        // 送信ボタンの無効化を解除する
        const submitBtn = document.querySelector('.result-button');
        if (submitBtn) {
            // ボタンを有効化
            submitBtn.disabled = false;
            // ボタンの透明度を元に戻す
            submitBtn.style.opacity = '1';
            // ボタンのテキストを元に戻す
            submitBtn.innerText = '結果へ';
        }

        // ログインボタンの読み込み中クラスを外す
        const loginBtn = document.getElementById('loginBtn');
        if (loginBtn) {
            // 読み込み中クラスを外す
            loginBtn.classList.remove('is-loading');
            // クリック可能にする
            loginBtn.style.pointerEvents = 'auto';
            // 透明度を元に戻す
            loginBtn.style.opacity = '1';
        }
    }
});
