package pt.cinzarosa.ajudante.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pt.cinzarosa.ajudante.dto.HouseResponse
import pt.cinzarosa.ajudante.service.HouseService

@RestController
@RequestMapping("/houses")
@Tag(name = "Houses", description = "Operations related to houses")
class HouseController(
    private val houseService: HouseService
) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all houses")
    fun fetchAllHouses(): List<HouseResponse> {
        return houseService.fetchAll()
    }
}