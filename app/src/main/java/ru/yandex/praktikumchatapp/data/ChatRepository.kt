package ru.yandex.praktikumchatapp.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.retryWhen
import kotlin.math.pow

class ChatRepository(
    private val api: ChatApi = ChatApi()
) {

    fun getReplyMessage(): Flow<String> {
        return api.getReply().retryWhen { cause, attempt ->
            if (cause is Exception) {
                val currentDelay = (INITIAL_DELAY_MS * DELAY_FACTOR.pow(attempt.toInt()))
                    .toLong()
                    .coerceAtMost(MAX_DELAY_MS)
                delay(currentDelay)
                true
            } else {
                false
            }
        }
    }

    private companion object {
        const val DELAY_FACTOR = 2.0
        const val INITIAL_DELAY_MS = 5000L
        const val MAX_DELAY_MS = 100_000L
    }
}