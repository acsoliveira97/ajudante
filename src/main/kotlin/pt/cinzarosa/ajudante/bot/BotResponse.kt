package pt.cinzarosa.ajudante.bot

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText

sealed class BotResponse {
    data class Send(val message: SendMessage) : BotResponse()
    data class Edit(val message: EditMessageText) : BotResponse()
}