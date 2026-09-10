package com.mruraza.ims.Utils.Objects

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

object InputValidator {

    /** Keep only digits (integers only) */
    fun String.onlyDigits(): String {
        return this.filter { it.isDigit() }
    }

    /** Allow digits + single decimal point (for doubles) */
    fun String.onlyDouble(): String {
        var dotCount = 0
        return this.filter { ch ->
            if (ch.isDigit()) true
            else if (ch == '.' && dotCount == 0) {
                dotCount++
                true
            } else false
        }
    }

    /** Phone number: only digits, max 10 characters */
    fun String.onlyPhone(): String {
        return this.filter { it.isDigit() }.take(10)
    }

    /** Alphabets only (if needed) */
    fun String.onlyAlphabets(): String {
        return this.filter { it.isLetter() }
    }

    /** Alphanumeric only */
    fun String.onlyAlphaNumeric(): String {
        return this.filter { it.isLetterOrDigit() }
    }

    /** Custom max length with digits only */
    fun String.onlyDigits(maxLength: Int): String {
        return this.filter { it.isDigit() }.take(maxLength)
    }
    /** Strict date in yyyy-MM-dd format (String-based) */
    // 1) String extension that turns any string into yyyy-mm-dd style (max 8 digits)
    fun String.onlyDate(): String {
        val digits = this.filter { it.isDigit() }.take(8) // keep at most yyyy(4)+MM(2)+dd(2)
        val sb = StringBuilder()
        for (i in digits.indices) {
            sb.append(digits[i])
            if (i == 3 || i == 5) sb.append('-') // after yyyy and after yyyyMM
        }
        return sb.toString()
    }

    // 2) TextFieldValue extension that formats and places the cursor at the end
    fun TextFieldValue.formatAsDateKeepCursorAtEnd(): TextFieldValue {
        val formatted = this.text.onlyDate()
        val end = formatted.length
        // drop composition to avoid weird IME state; place selection at end
        return TextFieldValue(text = formatted, selection = TextRange(end))
    }
}
