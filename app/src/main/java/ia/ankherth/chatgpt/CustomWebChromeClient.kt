package ia.ankherth.chatgpt

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.result.ActivityResultLauncher

/**
 * WebChromeClient personalizado que maneja la selección de archivos desde el WebView.
 * Implementa onShowFileChooser para abrir el selector de archivos del sistema.
 */
class CustomWebChromeClient(
    private val fileChooserLauncher: ActivityResultLauncher<Intent>
) : WebChromeClient() {

    private var filePathCallback: ValueCallback<Array<Uri>>? = null

    /**
     * Se llama cuando el WebView necesita mostrar un selector de archivos.
     * Típicamente ocurre cuando el usuario presiona "Adjuntar archivo" en ChatGPT.
     */
    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        // Guardar el callback para usarlo después
        this.filePathCallback = filePathCallback

        // Obtener los tipos de archivo aceptados
        val acceptTypes = fileChooserParams?.acceptTypes ?: arrayOf("*/*")

        // Crear intent para seleccionar archivo
        val intent = Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            type = if (acceptTypes.isEmpty() || acceptTypes[0] == "*/*") {
                "*/*" // Permitir cualquier tipo de archivo
            } else {
                acceptTypes[0] // Usar el primer tipo aceptado
            }
            putExtra(Intent.EXTRA_MIME_TYPES, acceptTypes)
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, fileChooserParams?.mode == FileChooserParams.MODE_OPEN_MULTIPLE)
        }

        // Crear chooser con mejor presentación
        val chooser = Intent.createChooser(intent, "Seleccionar archivo")

        // Lanzar el selector de archivos
        fileChooserLauncher.launch(chooser)

        // Retornar true indica que manejamos el evento
        return true
    }

    /**
     * Procesar el resultado de la selección de archivo.
     * Se debe llamar desde MainActivity cuando se reciba el resultado del launcher.
     */
    fun handleFileChooserResult(resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            // Usuario canceló la selección
            filePathCallback?.onReceiveValue(arrayOf<Uri>())
            filePathCallback = null
            return
        }

        // Obtener el URI del archivo seleccionado
        val uris: Array<Uri> = when {
            data?.clipData != null -> {
                // Múltiples archivos seleccionados
                val clipData = data.clipData ?: return
                Array(clipData.itemCount) { i ->
                    clipData.getItemAt(i).uri
                }
            }
            data?.data != null -> {
                // Un solo archivo seleccionado
                arrayOf(data.data!!)
            }
            else -> {
                // No hay archivo seleccionado
                arrayOf<Uri>()
            }
        }

        // Devolver los URIs al WebView
        filePathCallback?.onReceiveValue(uris)
        filePathCallback = null
    }

    /**
     * Cancelar la selección de archivo si es necesario
     */
    fun cancelFileChooser() {
        filePathCallback?.onReceiveValue(arrayOf<Uri>())
        filePathCallback = null
    }
}

