package pt.cinzarosa.ajudante.bot

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import pt.cinzarosa.ajudante.repository.HouseRepository
import kotlin.collections.map
import kotlin.collections.sortedBy

@Component
class WizardService(
    private val sessions: SessionStore,
    private val ui: UiBuilder,
    private val houseRepository: HouseRepository
) {

    fun onText(chatId: Long, text: String): List<BotResponse> {
        val session = sessions[chatId]
        val trimmed = text.trim()

        // Telegram cannot trigger on "open chat".
        // For MVP: any message shows menu UNLESS we're inside the houses filter step.
        return when (session.step) {

            WizardStep.SELECT_HOUSES_QUERY -> {
                session.houseQuery = trimmed
                val (selected, results) = splitHouses(session)

                listOf(BotResponse.Send(ui.housesFilteredSend(chatId, session, selected, results)))
            }

            else -> listOf(BotResponse.Send(ui.mainMenu(chatId)))
        }
    }

    fun onAction(chatId: Long, action: CallbackAction, messageId: Int?): List<BotResponse> {
        val session = sessions[chatId]

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

                if (messageId != null) {
                    listOf(BotResponse.Edit(ui.teamPickerEdit(chatId, messageId, session)))
                } else {
                    listOf(BotResponse.Send(ui.teamPickerSend(chatId, session)))
                }
            }

            CallbackAction.TeamConfirm -> {
                if (session.team.isEmpty()) {
                    return listOf(
                        BotResponse.Send(SendMessage(chatId.toString(), "Escolhe pelo menos 1 pessoa 🙂"))
                    )
                }

                // Load all houses once (in-memory filter like React)
                val houses = houseRepository.findAll().sortedBy { it.shortName.lowercase() }

                session.allHouses = houses.map { HouseOption(it.id!!, it.shortName) }
                session.selectedHouseIds.clear()
                session.houseQuery = ""
                session.step = WizardStep.SELECT_HOUSES_QUERY

                listOf(
                    BotResponse.Send(
                        SendMessage(chatId.toString(), "🔎 Escreve para filtrar casas:")
                    )
                )
            }

            is CallbackAction.HouseToggle -> {
                val id = action.id.toIntOrNull()
                    ?: return listOf(
                        BotResponse.Send(SendMessage(chatId.toString(), "Casa inválida 😅"))
                    )

                if (session.selectedHouseIds.contains(id)) session.selectedHouseIds.remove(id)
                else session.selectedHouseIds.add(id)

                val (selected, results) = splitHouses(session)

                if (messageId != null)
                    listOf(BotResponse.Edit(ui.housesFilteredEdit(chatId, messageId, session, selected, results)))
                else
                    listOf(BotResponse.Send(ui.housesFilteredSend(chatId, session, selected, results)))
            }

            CallbackAction.HouseConfirm -> {
                if (session.selectedHouseIds.isEmpty()) {
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

    private fun splitHouses(session: BotSession): Pair<List<HouseOption>, List<HouseOption>> {
        val all = session.allHouses
        val selectedIds: Set<Int> = session.selectedHouseIds

        val selected = all
            .filter { selectedIds.contains(it.id) }
            .sortedBy { it.shortName.lowercase() }

        val q = session.houseQuery.trim()
        val filtered = if (q.isBlank()) {
            all
        } else {
            val needle = q.lowercase()
            all.filter { it.shortName.lowercase().contains(needle) }
        }
            .filter { !selectedIds.contains(it.id) } // evita duplicar: selecionadas não aparecem de novo em resultados
            .sortedBy { it.shortName.lowercase() }
            .take(10)

        return selected to filtered
    }
}