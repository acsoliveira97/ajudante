package pt.cinzarosa.ajudante.mapper

import org.assertj.core.api.Assertions.assertThat
import org.instancio.Instancio
import org.junit.jupiter.api.Test
import pt.cinzarosa.ajudante.entity.Client as ClientEntity

class ClientMapperTest {

    private val mapper = ClientMapper()

    @Test
    fun `Map from client entity to domain`() {
        val clientEntity = Instancio.create(ClientEntity::class.java)

        val domain = with(mapper) { clientEntity.toDomain() }

        assertThat(domain.id).isEqualTo(clientEntity.id)
        assertThat(domain.name).isEqualTo(clientEntity.name)
        assertThat(domain.nif).isEqualTo(clientEntity.nif)
        assertThat(domain.email).isEqualTo(clientEntity.email)
        assertThat(domain.address).isEqualTo(clientEntity.address)
    }
}

