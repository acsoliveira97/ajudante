package pt.cinzarosa.ajudante.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class CleaningShiftEmployeeId(
    @Column(name = "cleaning_shift_id")
    var cleaningShiftId: Int? = null,

    @Column(name = "employee_id")
    var employeeId: Int? = null
) : Serializable