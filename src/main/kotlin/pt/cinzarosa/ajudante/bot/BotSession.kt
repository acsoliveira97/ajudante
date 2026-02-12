package pt.cinzarosa.ajudante.bot

data class BotSession(
    var step: WizardStep = WizardStep.NONE,
    var day: Int? = null,
    val team: MutableSet<String> = mutableSetOf(),
    val houses: MutableSet<String> = mutableSetOf(),
    var lastMenuMessageId: Int? = null
)
