# Changelog

Todos los cambios notables en este proyecto serán documentados en este archivo.

## [1.2] - 2025-12-XX

### Mejora de Experiencia de Usuario

#### ✨ Nuevas Características
- **Ajuste Automático del Teclado**: Implementado `android:windowSoftInputMode="adjustResize"` en AndroidManifest.xml
  - El WebView se redimensiona automáticamente cuando aparece el teclado
  - La interfaz se ajusta para que el contenido no quede oculto
  - Mejora significativa en la experiencia de escritura

#### 🔧 Mejoras Técnicas
- **Corrección de Elementos Fixed**: Script JavaScript mejorado que corrige elementos con `position: fixed`
  - Detecta y convierte elementos fixed en la parte inferior a `position: sticky`
  - Asegura que la barra de escritura siempre sea visible cuando aparece el teclado
  - Observador de mutaciones del DOM para aplicar correcciones dinámicamente
  - Compatible con la estructura de elementos de ChatGPT

#### 📝 Cambios en el Código
- **AndroidManifest.xml**: Agregado atributo `android:windowSoftInputMode="adjustResize"` a la actividad MainActivity
- **MainActivity.kt**: Mejorado el script JavaScript inyectado en `onPageFinished()`
  - Mantiene toda la funcionalidad de sesión existente
  - Agrega función `fixFixedElements()` para corrección de elementos fixed
  - Implementa `MutationObserver` para correcciones dinámicas en tiempo real

#### ✅ Experiencia de Usuario
- La barra de escritura de ChatGPT ya no queda tapada por el teclado
- Redimensionamiento suave y automático del contenido
- Mejor accesibilidad al escribir mensajes largos
- Compatibilidad mejorada con diferentes tamaños de pantalla

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
