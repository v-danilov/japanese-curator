package com.jc.translator

import com.jc.LanguageCode
import okhttp3.OkHttpClient
import okhttp3.Request

@Deprecated(message = "Use SeleniumTranslator instead")
class YandexTranslator : ITranslator {
    override fun translate(
        languageCodeSrc: LanguageCode,
        languageCodeDest: LanguageCode,
        textToTranslate: String
    ): String {
        val langFrom = languageCodeSrc.code
        val langTo = languageCodeDest.code
        val httpClient = OkHttpClient()
        val httpRequest = Request.Builder().url("https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20200226T093229Z.8418864de1d2f26e.80a5d8d4cb781a5ef6ffd3246fff9f81fca2f5be&text=$textToTranslate&lang=$langFrom-$langTo&format=plain").build()
        val responseString = httpClient.newCall(httpRequest).execute().body()!!.string()

        //Мне очень стыдно за это
        val slittedResponse = responseString.split(':')

        return slittedResponse[slittedResponse.size - 1].replace("\"", "")
            .replace("[", "")
            .replace("]", "")
            .replace("}", "")
    }
}
