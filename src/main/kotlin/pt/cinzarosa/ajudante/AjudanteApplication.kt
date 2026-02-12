package pt.cinzarosa.ajudante

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import pt.cinzarosa.ajudante.bot.TelegramProps

@EnableConfigurationProperties(TelegramProps::class)
@SpringBootApplication
class AjudanteApplication

fun main(args: Array<String>) {
	runApplication<AjudanteApplication>(*args)
}
