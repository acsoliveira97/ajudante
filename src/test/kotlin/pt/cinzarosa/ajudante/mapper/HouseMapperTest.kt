package pt.cinzarosa.ajudante.mapper

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.instancio.Instancio
import org.instancio.Select
import org.junit.jupiter.api.Test
import pt.cinzarosa.ajudante.model.House
import pt.cinzarosa.ajudante.entity.House as HouseEntity

class HouseMapperTest {

    private val clientMapper = ClientMapper()
    private val mapper = HouseMapper(clientMapper)

    @Test
    fun `Map from house entity to domain`() {
        val houseEntity = Instancio.create(HouseEntity::class.java)

        val domain = with(mapper) { houseEntity.toDomain() }

        assertThat(domain.id).isEqualTo(houseEntity.id)
        assertThat(domain.name).isEqualTo(houseEntity.name)
        assertThat(domain.shortName).isEqualTo(houseEntity.shortName)
        assertThat(domain.pricePerClean).isEqualTo(houseEntity.pricePerClean)
        assertThat(domain.client.id).isEqualTo(houseEntity.client.id)
        assertThat(domain.client.name).isEqualTo(houseEntity.client.name)
    }

    @Test
    fun `Map from house entity list to domain list`() {
        val houseEntities = listOf(
            Instancio.create(HouseEntity::class.java),
            Instancio.create(HouseEntity::class.java),
            Instancio.create(HouseEntity::class.java)
        )

        val domainList = with(mapper) { houseEntities.toDomainList() }

        assertThat(domainList).hasSize(houseEntities.size)

        domainList.forEachIndexed { index, domain ->
            assertThat(domain.id).isEqualTo(houseEntities[index].id)
            assertThat(domain.name).isEqualTo(houseEntities[index].name)
            assertThat(domain.shortName).isEqualTo(houseEntities[index].shortName)
            assertThat(domain.pricePerClean).isEqualTo(houseEntities[index].pricePerClean)
            assertThat(domain.client.id).isEqualTo(houseEntities[index].client.id)
        }
    }

    @Test
    fun `Map from house domain to response`() {
        val house = Instancio.create(House::class.java)

        val response = with(mapper) { house.toHouseResponse() }

        assertThat(response.id).isEqualTo(house.id)
        assertThat(response.name).isEqualTo(house.name)
        assertThat(response.shortName).isEqualTo(house.shortName)
        assertThat(response.pricePerClean).isEqualTo(house.pricePerClean)
        assertThat(response.clientName).isEqualTo(house.client.name)
    }

    @Test
    fun `Map from house domain to response throws exception when id is null`() {
        val house = Instancio.of(House::class.java)
            .set(Select.field(House::class.java, "id"), null)
            .create()

        assertThatThrownBy { with(mapper) { house.toHouseResponse() } }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("House id is null")
    }

    @Test
    fun `Map from house domain list to response list`() {
        val houses = listOf(
            Instancio.create(House::class.java),
            Instancio.create(House::class.java),
            Instancio.create(House::class.java)
        )

        val responseList = with(mapper) { houses.toHouseResponseList() }

        assertThat(responseList)
            .hasSize(houses.size)

        responseList.forEachIndexed { index, response ->
            assertThat(response.id).isEqualTo(houses[index].id)
            assertThat(response.name).isEqualTo(houses[index].name)
            assertThat(response.shortName).isEqualTo(houses[index].shortName)
            assertThat(response.pricePerClean).isEqualTo(houses[index].pricePerClean)
            assertThat(response.clientName).isEqualTo(houses[index].client.name)
        }
    }
}

