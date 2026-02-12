package pt.cinzarosa.ajudante.bot

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import org.telegram.telegrambots.meta.generics.TelegramClient

@Component
class TelegramExecutor(
    private val client: TelegramClient
) {
    fun ack(callbackId: String) {
        try {
            client.execute(AnswerCallbackQuery(callbackId))
        } catch (_: Exception) {
            // ignore ack failures for MVP
        }
    }

    fun execute(responses: List<BotResponse>) {
        responses.forEach { response ->
            when (response) {
                is BotResponse.Send -> safeExecute(response.message)
                is BotResponse.Edit -> safeExecuteEdit(response.message)
            }
        }
    }

    private fun safeExecute(msg: SendMessage) {
        client.execute(msg)
    }

    private fun safeExecuteEdit(edit: EditMessageText) {
        try {
            client.execute(edit)
        } catch (e: TelegramApiRequestException) {
            if (e.message?.contains("message is not modified") == true) return
            throw e
        }
    }
}