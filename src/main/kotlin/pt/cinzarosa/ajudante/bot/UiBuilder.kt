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
    private val housesList = (1..20).map { it.toString() }

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

    fun housesPickerMarkup(session: BotSession): InlineKeyboardMarkup {
        val rows = housesList.map { house ->
            val selected = if (session.houses.contains(house)) "✅ " else ""
            InlineKeyboardRow(btn(selected + house, "HOUSE_$house"))
        }.toMutableList()

        rows.add(InlineKeyboardRow(btn("Confirmar casas", "HOUSE_CONFIRM")))

        return InlineKeyboardMarkup.builder().keyboard(rows).build()
    }

    fun housesPickerSend(chatId: Long, session: BotSession): SendMessage {
        val msg = SendMessage(chatId.toString(), "🏠 Escolhe as casas:")
        msg.replyMarkup = housesPickerMarkup(session)
        return msg
    }

    fun housesPickerEdit(chatId: Long, messageId: Int, session: BotSession): EditMessageText =
        EditMessageText.builder()
            .chatId(chatId.toString())
            .messageId(messageId)
            .text("🏠 Escolhe as casas:")
            .replyMarkup(housesPickerMarkup(session))
            .build()

    fun finalConfirmation(chatId: Long, session: BotSession): SendMessage {
        val summary = """
            📝 Confirmar registo:
            
            📅 Dia: ${session.day}
            👥 Equipa: ${session.team.joinToString()}
            🏠 Casas: ${session.houses.joinToString()}
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
        InlineKeyboardButton.builder().text(text).callbackData(data).build()
}