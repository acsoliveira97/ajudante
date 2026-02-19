package pt.cinzarosa.ajudante.model

data class Client(
    val id: Int? = null,
    val name: String,
    val email: String,
    val nif: String,
    val address: String,
)