package pt.cinzarosa.ajudante.mapper

import org.springframework.stereotype.Component
import pt.cinzarosa.ajudante.model.Client

@Component
class ClientMapper {

    fun pt.cinzarosa.ajudante.entity.Client.toDomain(): Client =
        Client(
            id = id,
            name = name,
            nif = nif,
            email = email,
            address = address
        )
}