package pt.cinzarosa.ajudante.bot

sealed class CallbackAction {
    data object MenuNewEntry : CallbackAction()
    data object MenuViewDay : CallbackAction()
    data object MenuMonthlyReport : CallbackAction()

    data class Day(val day: Int) : CallbackAction()

    data class TeamToggle(val name: String) : CallbackAction()
    data object TeamConfirm : CallbackAction()

    data class HouseToggle(val id: String) : CallbackAction()
    data object HouseConfirm : CallbackAction()

    data object FinalConfirm : CallbackAction()
    data object FinalCancel : CallbackAction()

    data class Unknown(val raw: String) : CallbackAction()
}