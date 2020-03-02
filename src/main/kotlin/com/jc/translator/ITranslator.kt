package com.jc.translator

import com.jc.LanguageCode

interface ITranslator {

    /**
     * Translate some text for specific languages
     *
     * @param languageCodeSrc language of source text
     * @param languageCodeDest language to translate to
     * @param textToTranslate text to translate
     *
     * @return text translated to specified language
     */
    fun translate(languageCodeSrc: LanguageCode, languageCodeDest: LanguageCode, textToTranslate: String): String
}
