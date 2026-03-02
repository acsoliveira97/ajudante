package pt.cinzarosa.ajudante.exception

class HouseAlreadyAssignedException(
    val date: java.time.LocalDate,
    val houses: List<String>
) : RuntimeException()