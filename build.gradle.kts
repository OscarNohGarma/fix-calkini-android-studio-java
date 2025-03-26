// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // Agregar el plugin de google-services
    id("com.google.gms.google-services") version "4.4.0" apply false // Especificar la versión
}
