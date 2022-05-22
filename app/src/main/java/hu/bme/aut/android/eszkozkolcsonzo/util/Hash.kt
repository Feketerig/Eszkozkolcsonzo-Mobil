package hu.bme.aut.android.eszkozkolcsonzo.util

import com.soywiz.krypto.sha256

fun String.sha256(): String = this.toByteArray().sha256().hexLower
