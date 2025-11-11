package com.app.journeylink.screens

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object LanguageUtils {

    const val TAG_ES = "es"
    const val TAG_EN = "en"
    const val TAG_SYSTEM = "" // vacío = seguir idioma del sistema

    /**
     * Cambia el idioma de la app. Usa:
     *  - "es" para Español
     *  - "en" para English
     *  - ""  (o null) para usar el idioma del sistema
     */
    fun setAppLanguage(langTag: String?) {
        val appLocales = if (langTag.isNullOrBlank()) {
            LocaleListCompat.getEmptyLocaleList()  // vuelve a idioma del sistema
        } else {
            LocaleListCompat.forLanguageTags(langTag)
        }
        AppCompatDelegate.setApplicationLocales(appLocales)
        // AppCompat recrea automáticamente la Activity (si es AppCompatActivity)
    }

    /** Atajo para volver explícitamente al idioma del sistema. */
    fun useSystemLanguage() = setAppLanguage(TAG_SYSTEM)

    /**
     * Devuelve el tag actual:
     *  - "es", "en" o "" si está usando el idioma del sistema.
     */
    fun getCurrentLanguageTag(): String {
        val tags = AppCompatDelegate.getApplicationLocales().toLanguageTags()
        return if (tags.isNullOrBlank()) TAG_SYSTEM else tags
    }

    /** Texto legible para mostrar en UI (chip del menú). */
    fun getCurrentLanguageDisplayName(): String = when (getCurrentLanguageTag()) {
        TAG_ES -> "Español"
        TAG_EN -> "English"
        else   -> "System"
    }
}
