package pt.cinzarosa.ajudante.service

import org.springframework.stereotype.Service
import pt.cinzarosa.ajudante.dto.HouseResponse
import pt.cinzarosa.ajudante.mapper.HouseMapper
import pt.cinzarosa.ajudante.repository.HouseRepository

@Service
class HouseService(
    private val houseMapper: HouseMapper,
    private val houseRepository: HouseRepository
) {

    fun fetchAll(): List<HouseResponse> {
        val domainHouses = with(houseMapper) { houseRepository.findAll().toDomainList() }

        return with(houseMapper) { domainHouses.toHouseResponseList() }
    }
}