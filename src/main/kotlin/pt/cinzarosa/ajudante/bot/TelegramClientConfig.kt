package pt.cinzarosa.ajudante.bot

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient
import org.telegram.telegrambots.meta.generics.TelegramClient

@Configuration
class TelegramClientConfig {
    @Bean
    fun telegramClient(props: TelegramProps): TelegramClient = OkHttpTelegramClient(props.token)
}