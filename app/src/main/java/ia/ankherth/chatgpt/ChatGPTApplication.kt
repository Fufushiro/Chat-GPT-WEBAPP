package ia.ankherth.chatgpt

import android.app.Application
import android.os.Build
import android.webkit.WebView

class ChatGPTApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // setDataDirectorySuffix() debe llamarse ANTES de inicializar cualquier WebView
        // y solo en procesos que NO sean el proceso principal (multi-process check)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val processName = getCurrentProcessName()
            val isMainProcess = processName == null || processName == packageName
            
            // Solo configurar si NO es el proceso principal
            // En aplicaciones multi-proceso, WebView se inicializa en procesos separados
            if (!isMainProcess) {
                try {
                    WebView.setDataDirectorySuffix("webview_data")
                } catch (e: IllegalStateException) {
                    // WebView ya inicializado en este proceso, ignorar
                    android.util.Log.w("ChatGPTApplication", "WebView ya inicializado en proceso: $processName")
                }
            }
        }
    }

    private fun getCurrentProcessName(): String? {
        return try {
            val pid = android.os.Process.myPid()
            val manager = getSystemService(ACTIVITY_SERVICE) as android.app.ActivityManager
            manager.runningAppProcesses?.firstOrNull { it.pid == pid }?.processName
        } catch (e: Exception) {
            null
        }
    }
}

