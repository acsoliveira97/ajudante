package pt.cinzarosa.ajudante.entity

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table

@Entity
@Table(
    name = "cleaning_shift_employee",
    indexes = [Index(name = "idx_shift_employee_employee", columnList = "employee_id")]
)
class CleaningShiftEmployee(

    @EmbeddedId
    var id: CleaningShiftEmployeeId = CleaningShiftEmployeeId(),

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("cleaningShiftId")
    @JoinColumn(name = "cleaning_shift_id", nullable = false)
    var shift: CleaningShift? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id", nullable = false)
    var employee: Employee? = null
)