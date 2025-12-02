# 🚀 GUÍA PARA DESARROLLADORES - ChatGPT WebApp

## Introducción

Este documento proporciona toda la información necesaria para trabajar con la aplicación ChatGPT WebApp después de la optimización.

---

## 📋 Estado Actual

### Versión
- **App Version**: 1.0
- **Build Version**: 1
- **Target SDK**: 36 (Android 15)
- **Min SDK**: 31 (Android 12)
- **Kotlin Version**: 1.9.x
- **Gradle Version**: 8.x

### Última Optimización
- **Fecha**: 2 de Diciembre, 2025
- **Estado**: ✅ Producción
- **Errores**: 0
- **Warnings**: 0
- **Tamaño APK**: 11 MB

---

## 🔧 Setup Inicial

### Requisitos
```bash
# Verificar Java
java -version  # Java 11+

# Verificar Android SDK
sdkmanager --list_installed

# Requerimientos mínimos:
- Android SDK 36
- Build Tools 36.0.0+
- Java 11+
```

### Clonar y Preparar
```bash
# Clonar proyecto
git clone <repo-url>
cd "ChatGPT WebAPP"

# Sincronizar Gradle
./gradlew --refresh-dependencies

# Limpiar build anterior
./gradlew clean

# Verificar compilación
./gradlew build
```

---

## 📁 Estructura del Proyecto

```
ChatGPT WebAPP/
│
├── app/
│   ├── build.gradle.kts           # Configuración Gradle
│   ├── proguard-rules.pro         # Reglas ProGuard
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── java/ia/ankherth/chatgpt/
│   │   │   │   └── MainActivity.kt    # ✅ OPTIMIZADO
│   │   │   └── res/
│   │   │       ├── layout/
│   │   │       ├── drawable/
│   │   │       └── values/
│   │   ├── test/
│   │   └── androidTest/
│   └── build/
│       └── outputs/apk/
│           └── release/
│               └── app-release-unsigned.apk
│
├── gradle/
│   ├── wrapper/
│   └── libs.versions.toml        # Versiones de dependencias
│
├── build.gradle.kts              # Build top-level
├── settings.gradle.kts
│
├── Documentación/
│   ├── OPTIMIZATION_REPORT.md    # Reporte técnico
│   ├── OPTIMIZATION_GUIDE.md     # Guía de mejoras
│   ├── BUILD_SUMMARY.md          # Resumen ejecutivo
│   ├── VERIFICATION_CHECKLIST.md # Checklist de verificación
│   └── DEVELOPER_GUIDE.md        # Este archivo
│
├── KEY.jks                       # Certificado para firmar APK
└── local.properties              # Configuración local
```

---

## 🛠️ Desarrollo Local

### Compilar la App

```bash
# Debug build (rápido)
./gradlew assembleDebug

# Release build (optimizado)
./gradlew assembleRelease

# Build completo con tests
./gradlew build

# Build limpio
./gradlew clean build
```

### Ejecutar en Emulador

```bash
# Listar dispositivos
adb devices

# Instalar APK en dispositivo
adb install app/build/outputs/apk/release/app-release-unsigned.apk

# Instalar y lanzar
adb install -r app/build/outputs/apk/release/app-release-unsigned.apk
adb shell am start -n ia.ankherth.chatgpt/.MainActivity
```

### Debugging

```bash
# Ver logs
adb logcat | grep "ia.ankherth.chatgpt"

# Debugging detallado
adb shell setprop log.tag.WebViewChromium V
adb logcat | grep "WebViewChromium"

# Generar trace
adb shell am trace-ipc start --buffer-size=40000
# ... usar app ...
adb shell am trace-ipc stop --output=/data/local/traces/trace.pb
```

---

## 📱 MainActivity.kt - Guía Rápida

### Secciones Principales

```kotlin
// 1. INICIALIZACIÓN
override fun onCreate(savedInstanceState: Bundle?) {
    // Pantalla completa
    // Configurar WebView
    // Cargar ChatGPT
    // Manejo de navegación
}

// 2. CONFIGURACIÓN
private fun setupCacheAndStorage()    // Directorios y caché
private fun setupCookies()             // Cookies del navegador
private fun configureWebView()         // Configuración WebView
private fun getChatGPTUrl(): String    // URL de ChatGPT

// 3. CICLO DE VIDA
override fun onPause()     // Pausar recursos
override fun onResume()    // Reanudar recursos
override fun onDestroy()   // Limpiar memoria

// 4. WEBVIEW CLIENT
private inner class CustomWebViewClient : WebViewClient() {
    override fun onPageFinished() // Inyectar scripts
}
```

### Modificar Comportamiento

```kotlin
// ❌ NO HACER
- Modificar window.decorView.systemUiVisibility directamente
- Usar APIs removidas (databaseEnabled, etc.)
- Cambiar onBackPressed() directamente

// ✅ HACER
- Usar @Suppress("DEPRECATION") cuando sea necesario
- Usar OnBackPressedDispatcher
- Modificar settings.cacheMode para cache
- Usar evaluateJavascript para inyectar código
```

---

## 🔐 Seguridad

### No Commitear
```bash
# Evitar commitear:
- local.properties (contiene rutas locales)
- KEY.jks (certificado privado)
- .gradle/ (cache local)
- build/ (carpeta compilada)
- .idea/ (configuración IDE local)

# Agregar a .gitignore:
local.properties
KEY.jks
build/
.gradle/
.idea/
```

### Firmar APK

```bash
# Verificar certificado
keytool -list -v -keystore KEY.jks

# Firmar APK
jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 \
  -keystore KEY.jks \
  -storepass PASSWORD \
  -keypass PASSWORD \
  app/build/outputs/apk/release/app-release-unsigned.apk \
  androiddebugkey

# Verificar firma
jarsigner -verify -verbose app/build/outputs/apk/release/app-release.apk
```

---

## 🧪 Testing

### Lint Check
```bash
# Ejecutar lint
./gradlew lint

# Ver reporte
open app/build/reports/lint-results-release.html
```

### Unit Tests
```bash
# Ejecutar tests
./gradlew test

# Tests específicos
./gradlew test --tests "ia.ankherth.chatgpt.*"
```

### Instrumented Tests
```bash
# Ejecutar tests en dispositivo
./gradlew connectedAndroidTest

# Test específico
./gradlew connectedAndroidTest --tests "ia.ankherth.chatgpt.*"
```

---

## 📊 Performance

### Análisis de APK

```bash
# Ver tamaño de métodos
./gradlew buildDependents
du -h app/build/outputs/apk/release/app-release-unsigned.apk

# Analizar con bundletool
bundletool dump manifest --bundle=app.aab --output=manifest.txt
```

### Profiling

```bash
# Generar perfil de CPU
./gradlew assembleRelease --profile

# Ver reporte
open build/reports/profile-<timestamp>/index.html
```

### Memory Leak Detection

```bash
# Con LeakCanary (debugImplementation)
# Se detecta automáticamente en Debug

# Manual con adb
adb shell dumpsys meminfo ia.ankherth.chatgpt
```

---

## 🔄 Workflow de Desarrollo

### Para Nueva Feature

```bash
# 1. Crear rama
git checkout -b feature/nueva-feature

# 2. Hacer cambios
# ... editar código ...

# 3. Compilar
./gradlew clean build

# 4. Verificar no hay warnings
./gradlew lint

# 5. Tests
./gradlew test

# 6. Commit
git commit -m "feat: descripción"

# 7. Push
git push origin feature/nueva-feature

# 8. Pull Request
# ... abrir PR en GitHub ...
```

### Para Bug Fix

```bash
# 1. Crear rama desde main
git checkout -b bugfix/descripcion

# 2. Hacer fix
# ... editar código ...

# 3. Verificar compilación
./gradlew clean build

# 4. Tests (si aplica)
./gradlew test

# 5. Commit
git commit -m "fix: descripción del bug"

# 6. Push y PR
git push origin bugfix/descripcion
```

---

## 📚 Documentación Relacionada

### Archivos Generados
- `OPTIMIZATION_REPORT.md` - Reporte técnico detallado
- `OPTIMIZATION_GUIDE.md` - Guía de futuras optimizaciones
- `BUILD_SUMMARY.md` - Resumen ejecutivo
- `VERIFICATION_CHECKLIST.md` - Checklist de verificación

### Recursos Externos
- [Android Developers](https://developer.android.com/)
- [Kotlin Documentation](https://kotlinlang.org/docs/)
- [Gradle Documentation](https://docs.gradle.org/)
- [AndroidX](https://developer.android.com/jetpack/androidx)

---

## 🐛 Troubleshooting

### Problema: Build falla con "Unresolved reference"
```kotlin
// Solución:
./gradlew clean
./gradlew --refresh-dependencies
./gradlew build
```

### Problema: APK muy grande
```bash
# Solución: Activar ProGuard en build.gradle.kts
buildTypes {
    release {
        isMinifyEnabled = true
        isShrinkResources = true
    }
}
```

### Problema: Sesión expira rápido
```kotlin
// Solución: La sesión ya mantiene un ping automático
// Ver CustomWebViewClient.onPageFinished()
// Script inyectado cada 300000ms (5 minutos)
```

### Problema: WebView no carga
```kotlin
// Verificar:
1. webView.loadUrl(url) se llama en onCreate
2. INTERNET permission en AndroidManifest.xml
3. URL es válida (https://chatgpt.com)
4. Conexión a internet disponible
```

### Problema: Memory leak
```kotlin
// Solución: Revisar onDestroy()
override fun onDestroy() {
    super.onDestroy()
    cookieManager.flush()
    webView.destroy()  // ← IMPORTANTE
}
```

---

## 📞 Contacto y Soporte

### Para Dudas sobre:
- **Optimización**: Ver `OPTIMIZATION_REPORT.md`
- **Próximas mejoras**: Ver `OPTIMIZATION_GUIDE.md`
- **Build**: Ver `BUILD_SUMMARY.md`
- **Verificación**: Ver `VERIFICATION_CHECKLIST.md`

### Issues Comunes
1. Limpiar Gradle cache: `rm -rf ~/.gradle/caches/`
2. Sincronizar dependencias: `./gradlew --refresh-dependencies`
3. Invalidar caché IDE: File → Invalidate Caches

---

## ✅ Pre-commit Checklist

Antes de hacer commit:
- [ ] Compilación exitosa (`./gradlew build`)
- [ ] No hay warnings (`./gradlew lint`)
- [ ] Tests pasan (`./gradlew test`)
- [ ] Código formateado (Kotlin standard)
- [ ] Comentarios documentados
- [ ] APK verificable

---

## 🎯 Próximos Pasos

1. **Corto plazo**
   - Distribuir APK actual
   - Monitorear en usuarios reales

2. **Mediano plazo**
   - Activar ProGuard minification
   - Agregar Firebase Analytics

3. **Largo plazo**
   - Migración a Jetpack Compose
   - Tests automatizados

---

**Documento Versión**: 1.0
**Última Actualización**: 2 de Diciembre, 2025
**Autor**: Equipo de Desarrollo


