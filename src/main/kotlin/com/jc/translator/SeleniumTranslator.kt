package com.jc.translator

import com.jc.LanguageCode
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions


class SeleniumTranslator : ITranslator {

    private val seleniumDriver = prepareSeleniumDriver()

    /**
     * Helpful constants
     */
    private companion object {
        const val ID_TEXTFIELD_SRC = "source"
        const val CLASS_MARKER_DST = "//span[@class='tlid-translation translation']"
        const val CLASS_MARKER_TRANSLIT_TO = "//div[@class='result tlid-copy-target']/div[@class='tlid-result-transliteration-container result-transliteration-container transliteration-container']/div[@class='tlid-transliteration-content transliteration-content full']"
        const val CLASS_MARKER_TRANSLIT_FROM = "//div[@class='source-input']/div[@class='tlid-source-transliteration-container source-transliteration-container transliteration-container']/div[@class='tlid-transliteration-content transliteration-content full']"
    }

    /**
     * {@inheritDoc}
     */
    override fun translate(
        languageCodeSrc: LanguageCode,
        languageCodeDest: LanguageCode,
        textToTranslate: String
    ): String {
        val langFrom = languageCodeSrc.code
        val langTo = languageCodeDest.code

        println("Open Google translate with url")
        seleniumDriver.get("https://translate.google.com/?hl=ru#view=home&op=translate&sl=$langFrom&tl=$langTo")
        //Write text to the source box for automatic translation
        val sourceField = seleniumDriver.findElement(By.id(ID_TEXTFIELD_SRC))
        sourceField.sendKeys(textToTranslate)
        println("Input text: ".plus(sourceField.text))
        //Reading the translation
        println("Trying to translate")
        return translateAttempt(languageCodeSrc)
    }


    private fun translateAttempt(from: LanguageCode): String {
        //Trying to parse google translation in 1.2 seconds. If result doesnt ready - mark it as failed
        val timeout = System.currentTimeMillis() + 1200
        var isTranslationSuccessful = false

        //Prepare message
        var translationResult = "Sorry, i cant :("

        //Trying to read translation while it is not successful or waiting time is not running out
        while (System.currentTimeMillis() < timeout && !isTranslationSuccessful) {
            try {
                val destField = seleniumDriver.findElement(By.xpath(CLASS_MARKER_DST))

                //Выбираем поле с транслитом откуда читать. Зависит от того, в какю сторону переводим.
                // С японского - поле слева (source)
                // На японский - поле справа (result)
                val translitField =  if (from == LanguageCode.JAPANESE) seleniumDriver.findElement(By.xpath(CLASS_MARKER_TRANSLIT_FROM)) else seleniumDriver.findElement(By.xpath(CLASS_MARKER_TRANSLIT_TO))

                println("Translation completed")
                translationResult = destField.text.plus(" (").plus(translitField.text).plus(").")
                isTranslationSuccessful = true
            } catch (e: Exception) {
                println("Attempt failed")
            }
        }
        return translationResult
    }

    /**
     * Chrome driver initialization
     */
    private fun prepareSeleniumDriver(): WebDriver {
        val chromeOptions = ChromeOptions()
        chromeOptions.addArguments("start-maximized") // open Browser in maximized mode
        chromeOptions.addArguments("disable-infobars") // disabling infobars
        chromeOptions.addArguments("--disable-extensions"); // disabling extensions
        chromeOptions.addArguments("--disable-gpu") // applicable to windows os only
        chromeOptions.addArguments("--disable-dev-shm-usage") // overcome limited resource problems
        chromeOptions.addArguments("--no-sandbox") // Bypass OS security model
        chromeOptions.addArguments("--headless") // Bypass OS security model
        return ChromeDriver(chromeOptions)
    }
}
