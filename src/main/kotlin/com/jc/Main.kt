package com.jc

import me.ivmg.telegram.bot
import me.ivmg.telegram.dispatch
import me.ivmg.telegram.dispatcher.message
import me.ivmg.telegram.extensions.filters.Filter


fun main(args: Array<String>) {

    if (args.isEmpty()) {
        println("Please specify telegram token.")
        return
    }

    println("Starting...")
    val japaneseService = JapaneseService ()

    val japaneseCurator = bot {
        token = args[0]
        dispatch {
            message(Filter.Text) { bot, data ->
                val message = data.message!!
                println("Message received: " + message.text!!)
                println("Starting translation.")
                val russianText = japaneseService.translate(message.text!!)
                println("Translation result: $russianText")
                if (russianText.isNotEmpty()) bot.sendMessage(message.chat.id, replyToMessageId = message.messageId, text = russianText)
            }
        }
    }

    japaneseCurator.startPolling()

}

