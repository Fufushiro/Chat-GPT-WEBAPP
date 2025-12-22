# Changelog

Todos los cambios notables en este proyecto serán documentados en este archivo.

## [1.3] - 2025-12-22

### Implementación de Subida de Archivos desde Almacenamiento del Dispositivo

#### ✨ Nuevas Características
- **Selector de Archivos Integrado**: Acceso completo al almacenamiento del dispositivo para adjuntar archivos en ChatGPT
  - Selector de archivos nativo del sistema usando `ACTION_GET_CONTENT` y `ACTION_OPEN_DOCUMENT`
  - Soporte para seleccionar: imágenes, documentos y archivos en general (*/*)
  - Interfaz intuitiva usando diálogos del sistema operativo

- **WebChromeClient Personalizado**: Gestión moderna de eventos del WebView
  - Implementado `CustomWebChromeClient` para manejar `onShowFileChooser()`
  - Compatible con API moderna `ActivityResultLauncher`
  - Devolución correcta del Uri seleccionado al WebView mediante `ValueCallback<Array<Uri>>`

- **Acceso a Almacenamiento Seguro**: Configuración segura del WebView
  - Habilitado `allowFileAccess = true` para acceso a archivos locales
  - Habilitado `allowContentAccess = true` para acceso a content providers
  - Uso de permisos modernos sin `WRITE_EXTERNAL_STORAGE` innecesarios
  - Compatible con Android 10+ (scoped storage)

#### 🔧 Mejoras Técnicas
- **APIs Modernas de Android**: Migración a `ActivityResultLauncher`
  - Uso de `registerForActivityResult()` en lugar de deprecated `onActivityResult()`
  - Soporte para `GetContent` contract para selección de archivos
  - Manejo automático de resultados con lambda functions
  - Mejor rendimiento y mantenibilidad

- **Gestión de Permisos Moderna**: Permisos adaptados a versiones recientes
  - `android.permission.READ_MEDIA_IMAGES` para acceso a imágenes (API 33+)
  - `android.permission.READ_MEDIA_AUDIO` para acceso a audio (API 33+)
  - `android.permission.READ_MEDIA_VIDEO` para acceso a videos (API 33+)
  - Fallback a `READ_EXTERNAL_STORAGE` para Android 12 e inferiores
  - Eliminado `WRITE_EXTERNAL_STORAGE` (no requerido para lectura)

#### 📝 Cambios en el Código
- **MainActivity.kt**: 
  - Agregado `ActivityResultLauncher` para selección de archivos
  - Integración de `CustomWebChromeClient` en la configuración del WebView
  - Manejo automático de resultados del selector de archivos
  - Validación de URIs antes de pasar al WebView

- **CustomWebChromeClient.kt**: Nueva clase especializada
  - Implementación de `onShowFileChooser()` para manejar eventos de input file
  - Creación de intent para `ACTION_GET_CONTENT` con tipos MIME
  - Callback automático de resultados seleccionados
  - Manejo de errores y cancelaciones del usuario

- **AndroidManifest.xml**: 
  - Agregados permisos modernos `READ_MEDIA_IMAGES`, `READ_MEDIA_AUDIO`, `READ_MEDIA_VIDEO`
  - Fallback a `READ_EXTERNAL_STORAGE` con `maxSdkVersion="32"`
  - Agregada declaración `<queries>` para content providers (Android 11+)

#### ✅ Experiencia de Usuario
- Adjuntar archivos directamente desde ChatGPT en la app
- Selector de archivos nativo y familiar para usuarios Android
- Sin popups emergentes o instalación de apps externas
- Soporte para múltiples tipos de archivo
- Cancela automáticamente si el usuario no selecciona nada

#### 🎯 Beneficios Técnicos
- Implementación moderna usando ActivityResultLauncher
- Permisos granulares y seguridad mejorada
- Compatible con Android 10+ (scoped storage)
- No impacta en funcionalidades existentes (cookies, sesión, fullscreen, cache)
- Código modular y reutilizable

#### 🔒 Seguridad y Privacidad
- Acceso restringido solo a archivos seleccionados por el usuario
- No se guardan rutas de archivos sin permiso
- Compatibilidad con scoped storage de Android
- Permisos solicitados solo cuando se intenta adjuntar archivo

---

## [1.2] - 2025-12-XX

### Modernización de APIs y Mejora de Fullscreen

#### ✨ Nuevas Características
- **Fullscreen Moderno con WindowInsetsControllerCompat**: Reemplazado el sistema deprecated `systemUiVisibility` por APIs modernas de Android
  - Implementado `WindowCompat` y `WindowInsetsControllerCompat` para control de fullscreen
  - Configurado `BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE` para permitir mostrar barras con swipe
  - Fullscreen permanente sin salir del modo cuando aparece el teclado
  - Sin parpadeos ni cambios visuales bruscos

- **Ajuste Automático del Teclado con WindowInsetsCompat**: Sistema moderno de manejo de insets del IME
  - Implementado `ViewCompat.setOnApplyWindowInsetsListener()` para detectar el teclado
  - Uso de `WindowInsetsCompat.Type.ime()` para detectar insets del teclado
  - Ajuste dinámico del padding del layout cuando aparece/desaparece el teclado
  - El campo de texto nunca queda oculto por el teclado
  - Transiciones suaves sin interrupciones visuales

#### 🔧 Mejoras Técnicas
- **APIs Modernas de Android**: Migración completa a androidx.core
  - `WindowCompat.setDecorFitsSystemWindows()` para control de insets
  - `WindowInsetsControllerCompat` para gestión de barras del sistema
  - `ViewCompat.setOnApplyWindowInsetsListener()` para manejo de insets del IME
  - Compatible con Android 10+ (API 29+)
  - Preparado para futuras versiones de Android

- **Eliminación de Código Deprecated**: Removido código obsoleto
  - Eliminado `systemUiVisibility` (deprecated desde API 30)
  - Removido `android:windowSoftInputMode` del AndroidManifest (reemplazado por insets)
  - Código más limpio y mantenible

#### 📝 Cambios en el Código
- **MainActivity.kt**: 
  - Reemplazado `systemUiVisibility` por `WindowCompat` y `WindowInsetsControllerCompat`
  - Implementado `ViewCompat.setOnApplyWindowInsetsListener()` en el root layout
  - Manejo dinámico de padding basado en insets del IME y system bars
  - Agregados imports: `androidx.core.view.ViewCompat`, `WindowCompat`, `WindowInsetsCompat`, `WindowInsetsControllerCompat`

- **activity_main.xml**: 
  - Agregado `android:id="@+id/rootLayout"` al LinearLayout principal
  - Agregado `android:fitsSystemWindows="false"` para control manual de insets

- **AndroidManifest.xml**: 
  - Removido `android:windowSoftInputMode="adjustResize"` (ya no necesario con insets modernos)

#### ✅ Experiencia de Usuario
- Fullscreen permanente sin interrupciones
- El teclado empuja el contenido hacia arriba automáticamente
- Sin parpadeos ni cambios visuales bruscos
- Transiciones suaves al mostrar/ocultar el teclado
- Mejor experiencia en dispositivos con diferentes tamaños de pantalla
- Compatible con gestos de navegación modernos de Android

#### 🎯 Beneficios Técnicos
- Código más moderno y mantenible
- Mejor rendimiento con APIs optimizadas
- Preparado para futuras actualizaciones de Android
- Sin warnings de deprecación
- Compatible con Android 10+ (API 29+)

---

## [1.1] - 2025-12-XX

### Corrección Crítica

#### 🐛 Correcciones de Errores
- **Corrección de WebView.setDataDirectorySuffix()**: Resuelto el error `IllegalStateException: Can't set data directory suffix: WebView already initialized`
  - Movida la configuración de `setDataDirectorySuffix()` desde `MainActivity` a `ChatGPTApplication`
  - La configuración ahora se ejecuta antes de inicializar cualquier WebView
  - Implementada verificación de proceso principal (multi-process check)
  - Solo se configura en procesos que no sean el proceso principal

#### 🔧 Mejoras Técnicas
- **Nueva Clase Application**: Creada `ChatGPTApplication.kt` para manejar la inicialización de WebView
  - Configuración centralizada de WebView antes de cualquier inicialización
  - Manejo de errores mejorado con try-catch para evitar crashes
  - Verificación de nombre de proceso para compatibilidad multi-proceso

#### 📝 Cambios en el Código
- **MainActivity.kt**: Eliminada llamada a `WebView.setDataDirectorySuffix()` del método `setupCacheAndStorage()`
- **AndroidManifest.xml**: Agregado atributo `android:name=".ChatGPTApplication"` al tag `<application>`
- **ChatGPTApplication.kt**: Nueva clase Application con método `getCurrentProcessName()` para verificación de procesos

#### ✅ Estabilidad
- Eliminado error de compilación relacionado con conflictos de nombres de métodos
- Mejorada la inicialización de WebView para evitar errores en tiempo de ejecución
- Compatibilidad mejorada con aplicaciones multi-proceso

---

## [1.0] - 2025-12-02

### Lanzamiento Inicial

#### ✨ Características Agregadas

##### Funcionalidad Principal
- **WebView Integrado**: Carga la versión web de ChatGPT directamente en Android
- **Acceso a chatgpt.com**: URL dinámica compatible con VPN y cambios de región
- **Interfaz Limpia**: Pantalla completa sin barras de distracción

##### Gestión de Sesiones
- **Persistencia de Sesión**: La sesión se mantiene activa incluso después de cerrar la app
- **Keep-Alive Automático**: Script JavaScript que envía ping cada 5 minutos
- **Preservación de Cookies**: Almacenamiento seguro de cookies de autenticación
- **DOM Storage Persistente**: Mantiene localStorage y sessionStorage entre sesiones

##### Optimización de Rendimiento
- **Sistema de Cache de 200MB**: Almacenamiento optimizado para carga rápida
- **Cache-First Loading**: Prefiere cargar desde cache cuando está disponible
- **Pausa de Timers**: Pausa procesos en background para ahorrar batería
- **Database Local**: SQLite integrado para datos persistentes

##### Interfaz de Usuario
- **Pantalla Completa Inmersiva**: Oculta barras del sistema automáticamente
- **Action Bar Oculta**: Maximiza el espacio de visualización
- **Soporte de Zoom**: Zoom configurable con botones flotantes
- **Control de Navegación**: Botón atrás funcional para historial de ChatGPT

##### Compatibilidad
- **Android 12+**: Compatible con API level 31 y superiores
- **User Agent Móvil**: Configurado como navegador mobile para mejor renderizado
- **Soporte Multimedia**: Reproducción de audio y video sin gestos requeridos
- **Mixed Content**: Permite contenido mixto HTTP/HTTPS

##### Seguridad
- **HTTPS Seguro**: Todas las conexiones encriptadas
- **Almacenamiento Privado**: Datos guardados en directorio privado de la app
- **Gestión de Permisos**: Permisos necesarios para funcionalidad completa
- **WebView Moderno**: Chrome WebView actualizado

#### 🔧 Configuraciones Técnicas

##### Permisos Android
- `android.permission.INTERNET` - Acceso a internet
- `android.permission.ACCESS_NETWORK_STATE` - Estado de conexión
- `android.permission.ACCESS_FINE_LOCATION` - Geolocalización
- `android.permission.ACCESS_COARSE_LOCATION` - Ubicación aproximada
- `android.permission.WRITE_EXTERNAL_STORAGE` - Escritura de almacenamiento
- `android.permission.READ_EXTERNAL_STORAGE` - Lectura de almacenamiento

##### Dependencias
- AndroidX Core KTX
- AndroidX AppCompat
- Material Design
- Android WebView (nativo)

##### Configuración del Build
- **SDK Compilado**: 36
- **Min SDK**: 31 (Android 12)
- **Target SDK**: 36 (Android 15)
- **Java Target**: 11
- **Kotlin JVM Target**: 11

#### 📋 Componentes Principales

##### MainActivity.kt
- Actividad principal que gestiona el WebView
- Configuración de cache y almacenamiento
- Gestión del ciclo de vida de sesión
- CustomWebViewClient para inyección de scripts

##### activity_main.xml
- Layout principal con WebView fullscreen
- Estructura simple y optimizada

##### AndroidManifest.xml
- Declaración de permisos
- Configuración de la actividad
- Orientación portrait fija
- Manejo de cambios de configuración

#### 🐛 Notas de Construcción

- Primera versión de producción
- Testeado en dispositivos con Android 12-14
- Compatible con emuladores de Android Studio
- Optimizado para pantallas de 5" a 6.7"

#### 📦 Tamaño de Distribución

- APK Size: ~50MB (varía según arquitectura)
- Cache Máximo: 200MB
- Datos de App: ~10-50MB (según uso)

#### ⚠️ Limitaciones Conocidas

- Requiere conexión constante a internet
- Las funciones de geolocalización dependen de los permisos del sistema
- Algunos sitios externos pueden no cargar correctamente
- La aplicación no funciona en modo offline

#### 🔍 Testing

- Testeado en Google Play Services
- Verificado en múltiples dispositivos Android
- Compatible con Android 12 (API 31) a Android 15 (API 35)
- Funcionalidad validada con VPN activa

---

## Viajando Futuro

### Próximas Versiones Planeadas
- Mejoras de rendimiento
- Notificaciones de mensajes (si OpenAI lo permite)
- Tema oscuro adaptable
- Acceso offline a mensajes guardados
- Soporte para múltiples cuentas

---

**Formato basado en**: [Keep a Changelog](https://keepachangelog.com/en/1.0.0/)  
**Versionamiento basado en**: [Semantic Versioning](https://semver.org/spec/v2.0.0.html)
