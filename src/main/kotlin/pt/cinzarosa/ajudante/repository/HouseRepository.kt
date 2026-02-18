package pt.cinzarosa.ajudante.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.cinzarosa.ajudante.entity.House

interface HouseRepository : JpaRepository<House, Int>