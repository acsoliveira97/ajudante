package pt.cinzarosa.ajudante.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "client")
class Client(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var nif: String,

    @Column(nullable = false)
    var email: String,

    @Column(nullable = false)
    var address: String
){
    constructor() : this (
        name = "",
        nif = "",
        email = "",
        address = ""
    )
}