package pt.cinzarosa.ajudante.dto

import java.math.BigDecimal

data class HouseResponse(
    val id: Int,
    val name: String,
    val shortName: String,
    val pricePerClean: BigDecimal,
    val clientName: String
)