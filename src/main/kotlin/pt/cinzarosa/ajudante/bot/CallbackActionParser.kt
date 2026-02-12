package pt.cinzarosa.ajudante.bot

object CallbackActionParser {
    fun parse(data: String): CallbackAction = when {
        data == "MENU_NEW_ENTRY" -> CallbackAction.MenuNewEntry
        data == "MENU_VIEW_DAY" -> CallbackAction.MenuViewDay
        data == "MENU_MONTHLY_REPORT" -> CallbackAction.MenuMonthlyReport

        data == "TEAM_CONFIRM" -> CallbackAction.TeamConfirm
        data == "HOUSE_CONFIRM" -> CallbackAction.HouseConfirm

        data == "FINAL_CONFIRM" -> CallbackAction.FinalConfirm
        data == "FINAL_CANCEL" -> CallbackAction.FinalCancel

        data.startsWith("DAY_") ->
            data.removePrefix("DAY_").toIntOrNull()?.let { CallbackAction.Day(it) }
                ?: CallbackAction.Unknown(data)

        data.startsWith("TEAM_") -> CallbackAction.TeamToggle(data.removePrefix("TEAM_"))
        data.startsWith("HOUSE_") -> CallbackAction.HouseToggle(data.removePrefix("HOUSE_"))

        else -> CallbackAction.Unknown(data)
    }
}