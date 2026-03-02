package pt.cinzarosa.ajudante.entity

import jakarta.persistence.*

@Entity
@Table(name = "cleaning_shift_employee")
class CleaningShiftEmployee(

    @EmbeddedId
    var id: CleaningShiftEmployeeId = CleaningShiftEmployeeId(),

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("cleaningShiftId")
    @JoinColumn(name = "cleaning_shift_id")
    var shift: CleaningShift? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    var employee: Employee? = null
)