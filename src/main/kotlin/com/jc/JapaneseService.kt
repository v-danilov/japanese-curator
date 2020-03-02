package com.jc

import com.jc.translator.ITranslator
import com.jc.translator.SeleniumTranslator

class JapaneseService {

    private val translator: ITranslator = SeleniumTranslator()

    private val japaneseUnicodeBlocks =
        setOf<Character.UnicodeBlock>(
            Character.UnicodeBlock.HIRAGANA,
            Character.UnicodeBlock.KATAKANA,
            Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
        )

    fun translate(textToTranslate: String): String {
        if (textToTranslate.startsWith("\\-")) return translator.translate(LanguageCode.RUSSIAN, LanguageCode.JAPANESE, textToTranslate.substring(2))
        println("Extracting japanese symbols.")
        val onlyJapanese = extractJapanese(textToTranslate)
        return translateIfNotEmpty(onlyJapanese)
    }

    private fun translateIfNotEmpty(onlyJapanese: String): String {
        return if (onlyJapanese.isEmpty()) {
            println("Japanese symbols not found. Skipping translation step.")
            return onlyJapanese
        } else {
            translator.translate(LanguageCode.JAPANESE, LanguageCode.RUSSIAN, onlyJapanese)
        }
    }

    private fun extractJapanese(text: String): String {
        return text.filter { japaneseUnicodeBlocks.contains(Character.UnicodeBlock.of(it)) }
    }
}
