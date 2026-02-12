package pt.cinzarosa.ajudante.bot

import org.springframework.stereotype.Component

@Component
class InMemorySessionStore : SessionStore {
    private val sessions = mutableMapOf<Long, BotSession>()

    override fun get(chatId: Long): BotSession =
        sessions.computeIfAbsent(chatId) { BotSession() }

    override fun reset(chatId: Long) {
        sessions[chatId] = BotSession()
    }

    override fun remove(chatId: Long) {
        sessions.remove(chatId)
    }
}