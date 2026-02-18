package pt.cinzarosa.ajudante.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "house")
class House(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column(nullable = false)
    var name: String,

    @Column(name = "short_name", nullable = false, unique = true)
    var shortName: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    var client: Client,

    @Column(name = "price_per_clean", nullable = false)
    var pricePerClean: BigDecimal

) {
    constructor() : this(
        name = "",
        shortName = "",
        client = Client(),
        pricePerClean = BigDecimal.ZERO
    )
}