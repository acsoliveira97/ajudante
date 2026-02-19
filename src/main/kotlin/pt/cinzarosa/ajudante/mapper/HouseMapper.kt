package pt.cinzarosa.ajudante.mapper

import org.springframework.stereotype.Component
import pt.cinzarosa.ajudante.dto.HouseResponse
import pt.cinzarosa.ajudante.model.House

@Component
class HouseMapper(
    private val clientMapper: ClientMapper
) {

    fun List<House>.toHouseResponseList(): List<HouseResponse> =
        map { it.toHouseResponse() }

    fun List<pt.cinzarosa.ajudante.entity.House>.toDomainList(): List<House> =
        map { it.toDomain() }

    fun House.toHouseResponse(): HouseResponse =
        HouseResponse(
            id = requireNotNull(id) { "House id is null" },
            name = name,
            shortName = shortName,
            pricePerClean = pricePerClean,
            clientName = client.name
        )

    fun pt.cinzarosa.ajudante.entity.House.toDomain(): House =
        House(
            id = id,
            name = name,
            shortName = shortName,
            pricePerClean = pricePerClean,
            client = with(clientMapper) { client.toDomain() }
        )
}