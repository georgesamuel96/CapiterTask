package com.georgesamuel.capitertask

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import java.util.*

object Utils {

    @Suppress("DEPRECATION")
    fun setLocale(context: Context, lang: String) {
        val res: Resources = context.resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.setLocale(Locale(lang))
        res.updateConfiguration(conf, dm)
    }

}