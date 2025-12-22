# 💡 EJEMPLOS Y CASOS DE USO

## 🎯 Casos de Uso Comunes

### 1. Adjuntar una Imagen a ChatGPT
```
Usuario: Abre la app de ChatGPT
         ↓
Acción:  Presiona botón "+" o el ícono de clip 📎
         ↓
Sistema: Se abre el selector de archivos
         ↓
Usuario: Navega a Fotos → Selecciona una imagen (ej: foto.jpg)
         ↓
Sistema: Se cierra el selector
         ↓
WebView: Recibe el URI de la imagen
         ↓
ChatGPT: Carga la imagen en el chat
         ↓
Usuario: Escribe la pregunta y envía
         ↓
ChatGPT: Analiza la imagen y responde ✅
```

---

### 2. Adjuntar un Documento PDF
```
Usuario: Abre la app de ChatGPT
         ↓
Acción:  Presiona "Adjuntar archivo"
         ↓
Sistema: Se abre el selector de archivos
         ↓
Usuario: Navega a Descargas → Selecciona un PDF (ej: documento.pdf)
         ↓
Sistema: Cierra el selector
         ↓
WebView: Recibe el URI del PDF
         ↓
ChatGPT: Carga el PDF en el chat
         ↓
Usuario: Pregunta algo sobre el PDF
         ↓
ChatGPT: Lee el PDF y responde ✅
```

---

### 3. Adjuntar Múltiples Archivos
```
Usuario: Presiona "Adjuntar archivo"
         ↓
Sistema: Se abre el selector (compatible con múltiples)
         ↓
Usuario: Selecciona archivo 1, luego 2, luego 3
         (en algunos dispositivos: mantén presionado + selecciona)
         ↓
Sistema: Cierra el selector con 3 archivos
         ↓
WebView: Recibe 3 URIs: [uri1, uri2, uri3]
         ↓
ChatGPT: Carga los 3 archivos
         ↓
Usuario: "Analiza estos 3 documentos" + Enviar
         ↓
ChatGPT: Procesa los 3 y responde ✅
```

---

### 4. Adjuntar Documento DOCX desde OneDrive
```
Usuario: Presiona "Adjuntar archivo"
         ↓
Sistema: Se abre el selector
         ↓
Usuario: (En el selector) Toca en "OneDrive"
         o accede a otros proveedores de almacenamiento
         ↓
Usuario: Navega a Mi trabajo → documento.docx
         ↓
Usuario: Selecciona el archivo
         ↓
Sistema: Cierra selector
         ↓
WebView: Recibe el URI (puede ser de OneDrive)
         ↓
ChatGPT: Carga el documento
         ↓
Usuario: "Resume este documento"
         ↓
ChatGPT: Lee y resume ✅
```

---

## 🔧 Ejemplos de Código (Para Desarrolladores)

### Modificar el Tipo de Archivo Aceptado

#### Situación: Solo quiero imágenes

**En CustomWebChromeClient.kt:**
```kotlin
override fun onShowFileChooser(...): Boolean {
    // ... código anterior ...
    
    // ANTES: Permitía todos los tipos
    // val acceptTypes = fileChooserParams?.acceptTypes ?: arrayOf("*/*")
    
    // AHORA: Solo imágenes
    val acceptTypes = arrayOf("image/*")
    
    // Resto del código igual...
}
```

#### Situación: Solo documentos

**En CustomWebChromeClient.kt:**
```kotlin
val acceptTypes = arrayOf(
    "application/pdf",
    "application/msword",
    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
)
```

#### Situación: Tipos específicos

**En CustomWebChromeClient.kt:**
```kotlin
val acceptTypes = arrayOf(
    "image/jpeg",
    "image/png",
    "application/pdf"
)
```

---

### Validar el Tamaño del Archivo

**En CustomWebChromeClient.kt:**
```kotlin
fun handleFileChooserResult(resultCode: Int, data: Intent?) {
    if (resultCode != Activity.RESULT_OK) {
        filePathCallback?.onReceiveValue(arrayOf<Uri>())
        return
    }

    val uris: Array<Uri> = when {
        data?.clipData != null -> {
            val clipData = data.clipData ?: return
            Array(clipData.itemCount) { i ->
                val uri = clipData.getItemAt(i).uri
                
                // ✅ VALIDAR TAMAÑO
                val size = getFileSize(uri)
                if (size > 50_000_000) { // 50 MB
                    showError("Archivo demasiado grande")
                    return@Array null
                }
                
                uri
            }.filterNotNull().toTypedArray()
        }
        data?.data != null -> {
            val uri = data.data ?: return
            
            // ✅ VALIDAR TAMAÑO
            val size = getFileSize(uri)
            if (size > 50_000_000) {
                showError("Archivo demasiado grande")
                arrayOf<Uri>()
            } else {
                arrayOf(uri)
            }
        }
        else -> arrayOf<Uri>()
    }

    filePathCallback?.onReceiveValue(uris)
    filePathCallback = null
}

// Función auxiliar para obtener tamaño del archivo
private fun getFileSize(uri: Uri): Long {
    return try {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
            it.moveToFirst()
            it.getLong(sizeIndex)
        } ?: 0L
    } catch (e: Exception) {
        0L
    }
}

private fun showError(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
```

---

### Mostrar Notificación de Carga Completada

**En MainActivity.kt:**
```kotlin
private inner class CustomWebViewClient : WebViewClient() {
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        
        // Detectar si una carga de archivo fue completada
        view?.evaluateJavascript(
            """
            (function() {
                // Verificar si hay un indicador de carga completada
                if (document.querySelector('[data-testid="file-upload-success"]')) {
                    // Archivo cargado exitosamente
                    window.Android?.notifyFileUploadSuccess?.()
                }
            })()
            """.trimIndent()
        ) { }
    }
}

// Agregar esto a MainActivity para escuchar desde JavaScript
private fun addJavaScriptInterface() {
    webView.addJavascriptInterface(object {
        @JavascriptInterface
        fun notifyFileUploadSuccess() {
            // Mostrar notificación
            Toast.makeText(
                this@MainActivity,
                "✅ Archivo cargado exitosamente",
                Toast.LENGTH_SHORT
            ).show()
        }
    }, "Android")
}
```

---

### Comprimir Imagen Antes de Enviar

**En CustomWebChromeClient.kt:**
```kotlin
fun handleFileChooserResult(resultCode: Int, data: Intent?) {
    if (resultCode != Activity.RESULT_OK) {
        filePathCallback?.onReceiveValue(arrayOf<Uri>())
        return
    }

    val uris: Array<Uri> = when {
        data?.clipData != null -> {
            val clipData = data.clipData ?: return
            Array(clipData.itemCount) { i ->
                val uri = clipData.getItemAt(i).uri
                
                // ✅ COMPRIMIR SI ES IMAGEN
                if (isImage(uri)) {
                    val compressed = compressImage(uri)
                    compressed ?: uri
                } else {
                    uri
                }
            }
        }
        data?.data != null -> {
            val uri = data.data ?: return
            
            // ✅ COMPRIMIR SI ES IMAGEN
            if (isImage(uri)) {
                val compressed = compressImage(uri)
                arrayOf(compressed ?: uri)
            } else {
                arrayOf(uri)
            }
        }
        else -> arrayOf<Uri>()
    }

    filePathCallback?.onReceiveValue(uris)
    filePathCallback = null
}

private fun isImage(uri: Uri): Boolean {
    val mimeType = context.contentResolver.getType(uri) ?: return false
    return mimeType.startsWith("image/")
}

private fun compressImage(uri: Uri): Uri? {
    return try {
        val originalBitmap = BitmapFactory.decodeStream(
            context.contentResolver.openInputStream(uri)
        ) ?: return uri

        val compressedFile = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(compressedFile)
        
        // Comprimir a 80% de calidad
        originalBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        outputStream.close()

        Uri.fromFile(compressedFile)
    } catch (e: Exception) {
        null
    }
}
```

---

## 📊 Casos de Uso en el Mundo Real

### Caso 1: Analista de Datos
```
Tarea: "Analiza esta tabla de ventas"

Usuario:
1. Abre ChatGPT en la app
2. Presiona Adjuntar → Selecciona Excel (ventas_2025.xlsx)
3. Escribe: "Identifica tendencias"
4. Envía

ChatGPT:
- Lee el Excel
- Analiza datos
- Proporciona insights

Resultado: ✅ Análisis completo sin salir de la app
```

### Caso 2: Estudiante
```
Tarea: "Ayuda con este problema de matemáticas"

Usuario:
1. Abre ChatGPT
2. Toma foto del problema
3. Adjunta la foto
4. Escribe: "Explica paso a paso"
5. Envía

ChatGPT:
- Ve la imagen
- Lee el problema
- Explica la solución

Resultado: ✅ Comprensión completa del problema
```

### Caso 3: Escritor
```
Tarea: "Revisa mi documento"

Usuario:
1. Abre ChatGPT
2. Adjunta documento (mis_ideas.docx)
3. Escribe: "Mejora la redacción"
4. Envía

ChatGPT:
- Lee el documento
- Sugiere mejoras
- Proporciona alternativas

Resultado: ✅ Documento mejorado
```

### Caso 4: Programador
```
Tarea: "Revisa este código"

Usuario:
1. Abre ChatGPT
2. Adjunta archivo (main.py)
3. Escribe: "Hay un bug, ayuda"
4. Envía

ChatGPT:
- Analiza el código
- Identifica problemas
- Sugiere soluciones

Resultado: ✅ Bug encontrado y solucionado
```

---

## 🔍 Formatos de Archivo Soportados

### Imágenes ✅
```
JPG, JPEG, PNG, GIF, WebP, BMP, TIFF, SVG
(Depende del navegador y dispositivo)
```

### Documentos ✅
```
PDF, DOCX, DOC, XLSX, XLS, PPTX, PPT, TXT
(Depende de lo que acepte ChatGPT)
```

### Audio ✅
```
MP3, WAV, M4A, OGG, AAC
(Si ChatGPT lo soporta)
```

### Video ✅
```
MP4, MOV, AVI, MKV, FLV
(Si tienes espacio suficiente)
```

### Otros ✅
```
ZIP, RAR, 7Z (comprimidos)
JSON, XML, CSV (datos)
(Prácticamente cualquier formato)
```

---

## ⚠️ Limitaciones Conocidas

### 1. Tamaño de Archivo
```
- ChatGPT: Límites propios (generalmente 50-100 MB)
- App: Sin límite, depende de la memoria del dispositivo
```

### 2. Tipos de Archivo
```
- ChatGPT: No soporta ejecutables (.exe, .apk)
- Seguridad: Bloqueados por defecto
```

### 3. Velocidad de Carga
```
- Dispositivo lento: Espera a que cargue completamente
- WiFi recomendado para archivos grandes
```

### 4. Almacenamiento
```
- Dispositivo lleno: Puede fallar la carga
- Libera espacio si es necesario
```

---

## 🚀 Tips y Trucos

### Tip 1: Usar Accesos Directos
```
Google Files → Acceso rápido a descargas
Fotos → Acceso rápido a imágenes
Documentos → Acceso a archivos guardados
```

### Tip 2: Renombrar Archivos Claramente
```
❌ MALO: photo (2).jpg
✅ BUENO: factura_enero_2025.jpg

Ayuda a ChatGPT a entender mejor el contenido
```

### Tip 3: Usar Formatos Comprimidos
```
❌ Lento: Imagen original 5MB
✅ Rápido: Imagen comprimida 500KB

Reduce tiempo de carga
```

### Tip 4: Archivo a la Vez (Si es Posible)
```
Algunos dispositivos funcionan mejor con:
1 archivo → Envía → Pregunta
1 archivo → Envía → Pregunta

En lugar de:
3 archivos → Envía → Pregunta
```

---

## 📈 Mejoras Futuras Posibles

### 1. Drag & Drop
```kotlin
// Permitir arrastrar archivos al WebView
webView.setOnDragListener { v, event ->
    // Procesar archivos arrastrados
    true
}
```

### 2. Caché de Recientes
```kotlin
// Mostrar archivos usados recientemente
val recent = loadRecentFiles()
// Mostrar en el selector para acceso rápido
```

### 3. Editor de Imágenes Integrado
```kotlin
// Editar imagen antes de enviar
val edited = editImage(uri)
filePathCallback?.onReceiveValue(arrayOf(edited))
```

### 4. Vista Previa
```kotlin
// Mostrar preview del archivo antes de enviar
showPreview(uri)
// Permitir cancelar o aceptar
```

---

## 🎯 Conclusión

La funcionalidad de adjuntar archivos abre muchas posibilidades:

✅ Análisis de documentos
✅ Procesamiento de imágenes
✅ Revisión de código
✅ Análisis de datos
✅ Y mucho más...

**¡Tu app es ahora mucho más poderosa!** 🚀

---

**Documento**: Ejemplos y Casos de Uso
**Última actualización**: 22 de diciembre de 2025
**Ejemplos**: 15+
**Código**: Listo para copiar

