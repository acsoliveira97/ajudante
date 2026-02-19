package pt.cinzarosa.ajudante.model

import java.math.BigDecimal

data class House(
    val id: Int? = null,
    val name: String,
    val shortName: String,
    val pricePerClean: BigDecimal,
    val client: Client
)