package pt.cinzarosa.ajudante.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(
    name = "cleaning_shift",
    indexes = [Index(name = "idx_cleaning_shift_date", columnList = "cleaning_date")]
)
class CleaningShift(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column(name = "cleaning_date", nullable = false)
    var cleaningDate: LocalDate,

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    var createdAt: Instant? = null,

    @OneToMany(mappedBy = "shift", cascade = [CascadeType.ALL], orphanRemoval = true)
    var teamMembers: MutableSet<CleaningShiftEmployee> = mutableSetOf(),

    @OneToMany(mappedBy = "shift", cascade = [CascadeType.ALL], orphanRemoval = true)
    var houses: MutableSet<CleaningShiftHouse> = mutableSetOf()
)