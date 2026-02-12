package pt.cinzarosa.ajudante.bot

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@Component
class WizardService(
    private val sessions: SessionStore,
    private val ui: UiBuilder
) {

    fun onText(chatId: Long, text: String): List<BotResponse> {
        // Telegram cannot trigger on "open chat".
        // So for now: show menu for any message.
        return listOf(BotResponse.Send(ui.mainMenu(chatId)))
    }

    fun onAction(chatId: Long, action: CallbackAction, messageId: Int?): List<BotResponse> {
        val session = sessions.get(chatId)

        return when (action) {
            CallbackAction.MenuNewEntry -> {
                sessions.reset(chatId)
                listOf(BotResponse.Send(ui.dayPicker(chatId)))
            }

            is CallbackAction.Day -> {
                session.day = action.day
                session.step = WizardStep.SELECT_TEAM
                listOf(BotResponse.Send(ui.teamPickerSend(chatId, session)))
            }

            is CallbackAction.TeamToggle -> {
                toggleTeam(session, action.name)
                // Edit current team message if we have messageId
                if (messageId != null) listOf(BotResponse.Edit(ui.teamPickerEdit(chatId, messageId, session)))
                else listOf(BotResponse.Send(ui.teamPickerSend(chatId, session)))
            }

            CallbackAction.TeamConfirm -> {
                if (session.team.isEmpty()) {
                    listOf(BotResponse.Send(SendMessage(chatId.toString(), "Escolhe pelo menos 1 pessoa 🙂")))
                } else {
                    session.step = WizardStep.SELECT_HOUSES
                    listOf(BotResponse.Send(ui.housesPickerSend(chatId, session)))
                }
            }

            is CallbackAction.HouseToggle -> {
                toggleHouse(session, action.id)
                if (messageId != null) listOf(BotResponse.Edit(ui.housesPickerEdit(chatId, messageId, session)))
                else listOf(BotResponse.Send(ui.housesPickerSend(chatId, session)))
            }

            CallbackAction.HouseConfirm -> {
                if (session.houses.isEmpty()) {
                    listOf(BotResponse.Send(SendMessage(chatId.toString(), "Escolhe pelo menos 1 casa 🙂")))
                } else {
                    session.step = WizardStep.CONFIRM
                    listOf(BotResponse.Send(ui.finalConfirmation(chatId, session)))
                }
            }

            CallbackAction.FinalConfirm -> {
                // For now simulate saving
                sessions.remove(chatId)
                listOf(
                    BotResponse.Send(SendMessage(chatId.toString(), "🎉 Registo guardado com sucesso!")),
                    BotResponse.Send(ui.mainMenu(chatId))
                )
            }

            CallbackAction.FinalCancel -> {
                sessions.remove(chatId)
                listOf(
                    BotResponse.Send(SendMessage(chatId.toString(), "❌ Registo cancelado.")),
                    BotResponse.Send(ui.mainMenu(chatId))
                )
            }

            CallbackAction.MenuViewDay -> listOf(
                BotResponse.Send(SendMessage(chatId.toString(), "📅 Em breve: ver registos de um dia.")),
                BotResponse.Send(ui.mainMenu(chatId))
            )

            CallbackAction.MenuMonthlyReport -> listOf(
                BotResponse.Send(SendMessage(chatId.toString(), "📊 Em breve: gerar relatório mensal em Excel.")),
                BotResponse.Send(ui.mainMenu(chatId))
            )

            is CallbackAction.Unknown -> listOf(
                BotResponse.Send(SendMessage(chatId.toString(), "Não percebi 😅")),
                BotResponse.Send(ui.mainMenu(chatId))
            )
        }
    }

    private fun toggleTeam(session: BotSession, name: String) {
        if (session.team.contains(name)) session.team.remove(name) else session.team.add(name)
    }

    private fun toggleHouse(session: BotSession, id: String) {
        if (session.houses.contains(id)) session.houses.remove(id) else session.houses.add(id)
    }
}