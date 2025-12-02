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
    }

    private fun setupCookies() {
        // Habilitar cookies y almacenamiento persistente
        cookieManager.apply {
            setAcceptCookie(true)
            setAcceptThirdPartyCookies(webView, true)
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

                // Configuración de cache
                cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
                
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
                
                // User agent
                userAgentString = "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36"
                
                // Mejorar rendimiento
                setGeolocationEnabled(true)
                mediaPlaybackRequiresUserGesture = false
                allowFileAccess = true
                allowContentAccess = true
            }
        }
    }

    private inner class CustomWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            
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
        
        // Recargar si es necesario pero sin perder sesión
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
