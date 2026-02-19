package pt.cinzarosa.ajudante.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class CleaningShiftHouseId(
    @Column(name = "cleaning_shift_id")
    val cleaningShiftId: Int = 0,

    @Column(name = "house_id")
    val houseId: Int = 0
) : Serializable