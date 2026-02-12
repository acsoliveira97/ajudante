package pt.cinzarosa.ajudante.bot

interface SessionStore {
    fun get(chatId: Long): BotSession
    fun reset(chatId: Long)
    fun remove(chatId: Long)
}