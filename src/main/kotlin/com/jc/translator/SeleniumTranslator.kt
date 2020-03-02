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
        const val ID_TEXTFIELD_DST = "//span[@class='tlid-translation translation']"
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
        return translateAttempt()
    }

    private fun translateAttempt(): String {
        //Trying to parse google translation in 1.2 seconds. If result doesnt ready - mark it as failed
        val timeout = System.currentTimeMillis() + 1200
        var isTranslationSuccessful = false

        //Prepare message
        var translationResult = "Sorry, i cant :("

        //Trying to read translation while it is not successful or waiting time is not running out
        while (System.currentTimeMillis() < timeout && !isTranslationSuccessful) {
            try {
                val destField = seleniumDriver.findElement(By.xpath(ID_TEXTFIELD_DST))
                println("Translation completed")
                translationResult = destField.text
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
