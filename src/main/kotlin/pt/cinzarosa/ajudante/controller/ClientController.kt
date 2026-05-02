package pt.cinzarosa.ajudante.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pt.cinzarosa.ajudante.dto.ClientResponse
import pt.cinzarosa.ajudante.repository.ClientRepository

@RestController
@RequestMapping("/clients")
@Tag(name = "Clients", description = "Operations related to clients")
class ClientController(
    private val clientRepository: ClientRepository
) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all clients")
    fun fetchAllClients(): List<ClientResponse> {
        return clientRepository.findAll().map {
            ClientResponse(id = it.id!!, name = it.name)
        }
    }
}

