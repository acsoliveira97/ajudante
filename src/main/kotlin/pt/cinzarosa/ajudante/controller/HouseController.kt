package pt.cinzarosa.ajudante.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pt.cinzarosa.ajudante.dto.CreateHouseRequest
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new house")
    fun createHouse(
        @Valid @RequestBody request: CreateHouseRequest
    ): HouseResponse {
        return houseService.create(request)
    }
}