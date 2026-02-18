package pt.cinzarosa.ajudante.bot

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow

@Component
class UiBuilder {

    // MVP hardcoded data (later comes from DB)
    private val teamMembers = listOf("Ana", "Catarina", "Joana", "Rita")

    fun mainMenu(chatId: Long): SendMessage {
        val msg = SendMessage(chatId.toString(), "Olá! 👋 O que queres fazer?")

        val keyboard = listOf(
            InlineKeyboardRow(btn("➕ Novo registo", "MENU_NEW_ENTRY")),
            InlineKeyboardRow(btn("📅 Ver dia", "MENU_VIEW_DAY")),
            InlineKeyboardRow(btn("📊 Relatório mensal", "MENU_MONTHLY_REPORT")),
        )

        msg.replyMarkup = InlineKeyboardMarkup.builder().keyboard(keyboard).build()
        return msg
    }

    fun dayPicker(chatId: Long): SendMessage {
        // MVP: first 7 days only (pagination later)
        val rows = (1..7).map { day ->
            InlineKeyboardRow(btn(day.toString(), "DAY_$day"))
        }
        val msg = SendMessage(chatId.toString(), "📅 Escolhe o dia:")
        msg.replyMarkup = InlineKeyboardMarkup.builder().keyboard(rows).build()
        return msg
    }

    fun teamPickerMarkup(session: BotSession): InlineKeyboardMarkup {
        val rows = teamMembers.map { member ->
            val selected = if (session.team.contains(member)) "✅ " else ""
            InlineKeyboardRow(btn(selected + member, "TEAM_$member"))
        }.toMutableList()

        rows.add(InlineKeyboardRow(btn("Confirmar equipa", "TEAM_CONFIRM")))

        return InlineKeyboardMarkup.builder().keyboard(rows).build()
    }

    fun teamPickerSend(chatId: Long, session: BotSession): SendMessage {
        val msg = SendMessage(chatId.toString(), "👥 Escolhe a equipa:")
        msg.replyMarkup = teamPickerMarkup(session)
        return msg
    }

    fun teamPickerEdit(chatId: Long, messageId: Int, session: BotSession): EditMessageText =
        EditMessageText.builder()
            .chatId(chatId.toString())
            .messageId(messageId)
            .text("👥 Escolhe a equipa:")
            .replyMarkup(teamPickerMarkup(session))
            .build()

    /**
     * Houses selection UI (filtered results).
     * This assumes WizardService filters houses in-memory and passes the current "result list" here.
     */
    fun housesFilteredSend(
        chatId: Long,
        session: BotSession,
        selected: List<HouseOption>,
        results: List<HouseOption>
    ): SendMessage {
        val msg = SendMessage(
            chatId.toString(),
            "🏠 Casas selecionadas: ${session.selectedHouseIds.size}\n" +
                    "Escreve para filtrar (ex: 'Art', 'Almada')."
        )
        msg.replyMarkup = housesFilteredMarkup( selected, results)
        return msg
    }

    fun housesFilteredEdit(
        chatId: Long,
        messageId: Int,
        session: BotSession,
        selected: List<HouseOption>,
        results: List<HouseOption>
    ): EditMessageText =
        EditMessageText.builder()
            .chatId(chatId.toString())
            .messageId(messageId)
            .text(
                "🏠 Casas selecionadas: ${session.selectedHouseIds.size}\n" +
                        "Escreve para filtrar (ex: 'Art', 'Almada')."
            )
            .replyMarkup(housesFilteredMarkup(selected, results))
            .build()

    private fun housesFilteredMarkup(
        selected: List<HouseOption>,
        results: List<HouseOption>
    ): InlineKeyboardMarkup {

        val rows = mutableListOf<InlineKeyboardRow>()

        // --- Secção: Selecionadas (sempre visíveis)
        if (selected.isNotEmpty()) {
            rows += InlineKeyboardRow(btn("✅ Selecionadas", "IGNORE")) // “header” (não faz nada)
            selected.forEach { h ->
                rows += InlineKeyboardRow(btn("✅ ${h.shortName}", "HOUSE_${h.id}")) // toggle para remover
            }
        }

        // --- Secção: Resultados do filtro
        rows += InlineKeyboardRow(btn("🔎 Resultados", "IGNORE"))
        results.forEach { h ->
            rows += InlineKeyboardRow(btn(h.shortName, "HOUSE_${h.id}")) // toggle para adicionar
        }

        rows += InlineKeyboardRow(btn("✅ Confirmar casas", "HOUSE_CONFIRM"))

        return InlineKeyboardMarkup.builder().keyboard(rows).build()
    }

    fun finalConfirmation(chatId: Long, session: BotSession): SendMessage {
        val houseNames = session.allHouses
            .filter { session.selectedHouseIds.contains(it.id) }
            .sortedBy { it.shortName.lowercase() }
            .joinToString(", ") { it.shortName }

        val summary = """
        📝 Confirmar registo:
        
        📅 Dia: ${session.day}
        👥 Equipa: ${session.team.joinToString()}
        🏠 Casas: $houseNames
        """.trimIndent()

        val keyboard = listOf(
            InlineKeyboardRow(
                btn("✅ Confirmar", "FINAL_CONFIRM"),
                btn("❌ Cancelar", "FINAL_CANCEL")
            )
        )

        val msg = SendMessage(chatId.toString(), summary)
        msg.replyMarkup = InlineKeyboardMarkup.builder().keyboard(keyboard).build()
        return msg
    }

    private fun btn(text: String, data: String): InlineKeyboardButton =
        InlineKeyboardButton.builder()
            .text(text)
            .callbackData(data)
            .build()
}