package pt.cinzarosa.ajudante.bot

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "telegram.bot")
class TelegramProps {
    lateinit var username: String
    lateinit var token: String
}