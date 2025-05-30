# Aplicación Móvil "Fix Calkiní

Integrantes del equipo:
-
- Oscar Iván Noh Garma - 7631
- Gildardo David Rubalcaba Cauich - 7649
- Pedro Raúl Chi Ek - 7614
- Andrés Guadalupe Laynes Flores - 7260


## Descripción del proyecto

Aplicación móvil para reportar problemas urbanos como baches, falta de iluminación, estancamientos de agua, y otros, en la ciudad de Calkiní. Los reportes se almacenan en Firebase Firestore y pueden ser visualizados por los encargados en un mapa.

## Requisitos

- Android Studio instalado (recomendado: versión Flamingo o posterior).
- SDK configurado (mínimo API nivel 21).
- Acceso a internet para conexión con Firebase.
- Ser dado de alta por el administrador de Firestore para poder acceder a la base de datos.

**IMPORTANTE:** El administrador ya debe tener configurado el proyecto y haberte dado permisos de acceso con tu correo electrónico.

## Configuración del proyecto

1. Clona este repositorio:

   ```bash
   git clone https://github.com/OscarNohGarma/fix-calkini-android-studio-java.git
   cd fix-calkini-android-studio-java
   ```

2. Abre el proyecto en Android Studio.

3. Asegúrate de tener el archivo `google-services.json` (proporcionado por el administrador) ubicado en:

   ```bash
   app/google-services.json
   ```

4. Generar las claves para usar el inicio de sesión de Google Services  
   Para generar las claves SHA-1 y SHA-256 (si se necesita agregar nuevas apps a Firebase), ejecuta:

   ```bash
   ./gradlew signingReport
   ```

   Esto muestra las claves que deben añadirse en Firebase en **Configuración del proyecto**, y luego **Agregar huella digital.**

## ▶Ejecutar la app

1. Confirma que el administrador de Firestore te haya dado de alta como usuario autorizado.  
2. Conecta un dispositivo físico o usa un emulador.  
3. Haz clic en el botón Run en Android Studio.  
4. La aplicación debería iniciarse y conectarse a Firestore automáticamente.

## Funcionalidades principales

- Reportar problemas con descripción, tipo y ubicación.
- Visualizar reportes enviados.
- Guardado en tiempo real en Firestore.
- Panel para encargados que muestra los reportes en mapa.

## Notas

- Las dependencias de Firebase están definidas en el archivo `build.gradle`.

## Contacto

Si tienes dudas puedes escribir a 7631@itescam.edu.mx (De ser necesario para agregar al usuario y otorgar los permisos necesarios).
