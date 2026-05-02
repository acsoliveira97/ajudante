package pt.cinzarosa.ajudante.service

import org.springframework.stereotype.Service
import pt.cinzarosa.ajudante.dto.CreateHouseRequest
import pt.cinzarosa.ajudante.dto.HouseResponse
import pt.cinzarosa.ajudante.mapper.HouseMapper
import pt.cinzarosa.ajudante.repository.ClientRepository
import pt.cinzarosa.ajudante.repository.HouseRepository

@Service
class HouseService(
    private val houseMapper: HouseMapper,
    private val houseRepository: HouseRepository,
    private val clientRepository: ClientRepository
) {

    fun fetchAll(): List<HouseResponse> {
        val domainHouses = with(houseMapper) { houseRepository.findAll().toDomainList() }

        return with(houseMapper) { domainHouses.toHouseResponseList() }
    }

    fun create(request: CreateHouseRequest): HouseResponse {
        val client = clientRepository.findById(request.clientId)
            .orElseThrow { NoSuchElementException("Client with id ${request.clientId} not found") }

        val entity = with(houseMapper) { request.toEntity(client) }
        val saved = houseRepository.save(entity)
        val domain = with(houseMapper) { saved.toDomain() }

        return with(houseMapper) { domain.toHouseResponse() }
    }
}