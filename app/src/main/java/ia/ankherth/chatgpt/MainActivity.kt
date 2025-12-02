package ia.ankherth.chatgpt

import android.os.Build
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var cookieManager: CookieManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pantalla completa
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
            android.view.View.SYSTEM_UI_FLAG_FULLSCREEN or
            android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )
        supportActionBar?.hide()

        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        cookieManager = CookieManager.getInstance()

        // Configurar cache y almacenaje
        setupCacheAndStorage()

        // Configurar cookies
        setupCookies()

        // Configurar WebView
        configureWebView()

        // Cargar ChatGPT
        val url = getChatGPTUrl()
        webView.loadUrl(url)

        // Configurar back pressed dispatcher
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun setupCacheAndStorage() {
        // Configurar directorio de cache
        val cacheDir = File(cacheDir, "webview_cache")
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }

        // Configurar directorio de datos
        val dataDir = File(filesDir, "webview_data")
        if (!dataDir.exists()) {
            dataDir.mkdirs()
        }

        WebView.setWebContentsDebuggingEnabled(false)

        // Nota: WebView.setDataDirectorySuffix() se llama ahora en ChatGPTApplication.onCreate()
        // antes de inicializar cualquier WebView para evitar el error IllegalStateException
    }

    private fun setupCookies() {
        // Habilitar cookies y almacenamiento persistente
        cookieManager.apply {
            setAcceptCookie(true)
            setAcceptThirdPartyCookies(webView, true)
            // Asegurar que las cookies se guardan inmediatamente
            flush()
        }
    }

    private fun getChatGPTUrl(): String = "https://chatgpt.com"

    private fun configureWebView() {
        webView.apply {
            webViewClient = CustomWebViewClient()

            settings.apply {
                @Suppress("SetJavaScriptEnabled")
                javaScriptEnabled = true
                domStorageEnabled = true
                databaseEnabled = true

                // Configuración de cache - CARGAR DESDE CACHE SI ESTÁ DISPONIBLE
                cacheMode = WebSettings.LOAD_DEFAULT

                // Almacenaje de aplicación
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    setAlgorithmicDarkeningAllowed(false)
                }

                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                useWideViewPort = true
                loadWithOverviewMode = true
                setSupportZoom(true)
                builtInZoomControls = true
                displayZoomControls = false

                // User agent para que ChatGPT no detecte que es un WebView
                userAgentString = "Mozilla/5.0 (Linux; Android 14; Pixel 5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"

                // Mejorar rendimiento
                setGeolocationEnabled(true)
                mediaPlaybackRequiresUserGesture = false
                allowFileAccess = true
                allowContentAccess = true

                // Nota: setAppCacheEnabled y setAppCachePath fueron eliminados en versiones recientes de Android
                // El cache se maneja automáticamente con cacheMode = WebSettings.LOAD_DEFAULT

                // Habilitar almacenamiento local (DOM Storage)
                // Nota: setDatabasePath también fue deprecado, pero se mantiene para compatibilidad con versiones antiguas
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    @Suppress("DEPRECATION")
                    setDatabasePath(filesDir.absolutePath + "/webview_db")
                }
            }
        }
    }

    private inner class CustomWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            // Guardar cookies después de que carga la página
            cookieManager.flush()

            // Inyectar script para mantener la sesión
            view?.evaluateJavascript(
                """
                (function() {
                    // Prevenir expiración de sesión
                    localStorage.setItem('_session_keep_alive', new Date().getTime());
                    sessionStorage.setItem('_session_active', 'true');
                    
                    // Enviar ping periódico para mantener sesión
                    if (!window._sessionKeepAliveInterval) {
                        window._sessionKeepAliveInterval = setInterval(function() {
                            localStorage.setItem('_session_keep_alive', new Date().getTime());
                        }, 300000); // Cada 5 minutos
                    }
                })();
                """.trimIndent()
            ) { }
        }
    }

    override fun onPause() {
        super.onPause()
        // Guardar cookies cuando se pausa la aplicación
        cookieManager.flush()
        webView.onPause()
        webView.pauseTimers()
    }

    override fun onResume() {
        super.onResume()
        // Restaurar sesión cuando se reanuda
        webView.onResume()
        webView.resumeTimers()

        // NO recargar automáticamente para mantener sesión intacta
        // Solo cargar si es la primera vez (webView.url es null)
        if (webView.url == null) {
            val url = getChatGPTUrl()
            webView.loadUrl(url)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Guardar estado antes de destruir
        cookieManager.flush()
        webView.destroy()
    }
}

