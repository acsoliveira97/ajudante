package pt.cinzarosa.ajudante.bot

import org.springframework.stereotype.Component
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class AjudanteBot(
    private val props: TelegramProps,
    private val wizard: WizardService,
    private val executor: TelegramExecutor
) : SpringLongPollingBot {

    override fun getBotToken(): String = props.token

    override fun getUpdatesConsumer() = LongPollingSingleThreadUpdateConsumer { update ->
        onUpdate(update)
    }

    private fun onUpdate(update: Update) {
        when {
            update.hasMessage() -> {
                val msg = update.message ?: return
                val text = msg.text?.trim() ?: return
                val responses = wizard.onText(msg.chatId, text)
                executor.execute(responses)
            }

            update.hasCallbackQuery() -> {
                val cb = update.callbackQuery ?: return
                executor.ack(cb.id)

                val chatId = cb.message.chatId
                val data = cb.data ?: return
                val action = CallbackActionParser.parse(data)

                val responses = wizard.onAction(
                    chatId = chatId,
                    action = action,
                    messageId = cb.message.messageId
                )
                executor.execute(responses)
            }
        }
    }
}