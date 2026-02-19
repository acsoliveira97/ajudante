package pt.cinzarosa.ajudante.entity

import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "cleaning_shift_house")
class CleaningShiftHouse(

    @EmbeddedId
    var id: CleaningShiftHouseId = CleaningShiftHouseId(),

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("cleaningShiftId")
    @JoinColumn(name = "cleaning_shift_id", nullable = false)
    var shift: CleaningShift? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("houseId")
    @JoinColumn(name = "house_id", nullable = false)
    var house: House? = null,

    @Column(name = "cleaning_date", nullable = false)
    var cleaningDate: LocalDate
)