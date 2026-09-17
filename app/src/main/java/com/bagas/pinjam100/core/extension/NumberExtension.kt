package com.bagas.pinjam100.core.extension

import java.text.NumberFormat
import java.util.Locale

private val INDONESIAN_LOCALE = Locale.forLanguageTag("id-ID")

fun Int.formatCurrencyIDR(): String {
    val formatter = NumberFormat.getNumberInstance(INDONESIAN_LOCALE)
    return "Rp${formatter.format(this)}"
}

fun Long.formatCurrencyIDR(): String {
    val formatter = NumberFormat.getNumberInstance(INDONESIAN_LOCALE)
    return "Rp${formatter.format(this)}"
}