# ChatGPT WebAPP for Android

Una aplicación nativa de Android que proporciona acceso fluido a la versión web de ChatGPT directamente desde tu dispositivo móvil.

**✅ Versión Optimizada**: v1.1 (Diciembre, 2025)
- 0 Errores de compilación
- 0 Warnings
- APIs modernizadas a Android 15 (API 36)
- Corrección crítica de WebView.setDataDirectorySuffix()
- Listo para producción

> 📚 **Leer documentación**: Comienza por [QUICK_START.md](./QUICK_START.md) o [DOCUMENTATION_INDEX.md](./DOCUMENTATION_INDEX.md)

## 📋 Características

### 🚀 Características Principales
- **Acceso a ChatGPT Web**: Carga la interfaz completa de chatgpt.com en modo webapp
- **Pantalla Completa**: Interfaz inmersiva sin barras de distracción
- **Sesión Persistente**: Mantiene tu sesión activa incluso después de cerrar la app
- **Cache Inteligente**: Sistema de almacenamiento optimizado de 200MB para navegación fluida

### 💾 Gestión de Datos
- **Almacenamiento Local**: DOM Storage y SQLite para persistencia de datos
- **Cookies Persistentes**: Mantiene tus credenciales y preferencias
- **Keep-Alive de Sesión**: Ping automático cada 5 minutos para prevenir expiración
- **Sincronización de Estado**: Guarda automáticamente el estado al minimizar la app

### 🌐 Compatibilidad
- **Soporte Multi-Región**: Compatible con VPN y cambios de ubicación
- **User Agent Móvil**: Optimizado para visualización correcta en mobile
- **JavaScript Habilitado**: Soporta todas las funciones interactivas de ChatGPT
- **Multimedia**: Reproducción de audio y contenido multimedia

### ⚙️ Rendimiento
- **Carga Rápida**: Sistema de cache que prefiere contenido almacenado
- **Bajo Consumo**: Pausa timers cuando la app está minimizada
- **Zoom Adaptable**: Soporte para zoom con botones flotantes
- **Optimización Android**: Configurado para Android 14+

## 📱 Requisitos

- **Android**: 12.0 (API level 31) o superior
- **Memoria RAM**: Mínimo 2GB recomendado
- **Conexión**: Internet activa
- **Almacenamiento**: 200MB disponibles para cache

## 🔧 Instalación

1. Clona el repositorio:
```bash
git clone https://github.com/tu-usuario/chatgpt-webapp-android.git
cd ChatGPT\ WebAPP
```

2. Abre el proyecto en Android Studio

3. Sincroniza las dependencias de Gradle

4. Compila y ejecuta en tu dispositivo o emulador:
```bash
./gradlew assembleDebug
```

## 📖 Uso

1. Inicia la aplicación
2. Espera a que cargue ChatGPT
3. Inicia sesión con tu cuenta de OpenAI
4. ¡Comienza a chatear!

### Características de Navegación
- **Botón Atrás**: Navega hacia atrás en el historial de ChatGPT
- **Zoom**: Usa los botones de zoom o los gestos del dispositivo
- **Minimizar**: Tu sesión se guardará automáticamente
- **Reanudar**: Al volver a abrir, tu sesión estará activa

## 🔐 Seguridad y Privacidad

- Todas las cookies y datos de sesión se almacenan localmente en tu dispositivo
- La comunicación con OpenAI se realiza a través de conexiones HTTPS seguras
- Los datos de caché se almacenan en el directorio privado de la aplicación
- No se recopilan ni comparten datos personales adicionales

## 🌍 Permisos Requeridos

- `INTERNET`: Para acceder a ChatGPT
- `ACCESS_NETWORK_STATE`: Para detectar conexión de red
- `ACCESS_FINE_LOCATION`: Para geolocalización (opcional, requerido por algunas funciones)
- `ACCESS_COARSE_LOCATION`: Localización aproximada
- `WRITE_EXTERNAL_STORAGE`: Para ciertos tipos de descarga
- `READ_EXTERNAL_STORAGE`: Para lectura de archivos

## 🛠️ Desarrollo

### Stack Tecnológico
- **Lenguaje**: Kotlin
- **Target SDK**: Android 14 (API 34)
- **Compilación**: Gradle 8.x
- **WebView**: Chrome WebView (Android nativo)

### Estructura del Proyecto
```
app/
├── src/
│   ├── main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/ia/ankherth/chatgpt/
│   │   │   ├── MainActivity.kt
│   │   │   └── ChatGPTApplication.kt
│   │   └── res/
│   │       ├── layout/
│   │       │   └── activity_main.xml
│   │       └── values/
│   │           ├── colors.xml
│   │           ├── strings.xml
│   │           └── themes.xml
│   ├── androidTest/
│   └── test/
└── build.gradle.kts
```

## 📝 Licencia

Este proyecto está bajo licencia MIT. Ver archivo `LICENSE` para más detalles.

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor:

1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📧 Contacto

Para preguntas o sugerencias, por favor abre un issue en el repositorio.

## ⚠️ Disclaimer

Esta aplicación es un cliente no oficial de ChatGPT. No está afiliada ni respaldada por OpenAI. Úsalo bajo tu propio riesgo y cumpliendo con los términos de servicio de OpenAI.

---

**Versión Actual**: 1.1  
**Última Actualización**: Diciembre 2025
# Chat-GPT-WEBAPP
