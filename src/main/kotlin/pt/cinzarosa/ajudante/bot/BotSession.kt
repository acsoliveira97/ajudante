package pt.cinzarosa.ajudante.bot

data class BotSession(
    var step: WizardStep = WizardStep.NONE,
    var day: Int? = null,
    val team: MutableSet<String> = mutableSetOf(),
    var lastMenuMessageId: Int? = null,
    var houseQuery: String = "",
    var allHouses: List<HouseOption> = emptyList(),
    val selectedHouseIds: MutableSet<Int> = mutableSetOf()
)
