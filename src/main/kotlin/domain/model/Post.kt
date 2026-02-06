package domain.model

import java.time.Instant

data class Post(
    val author: UserName,
    val message: String,
    val timestamp: Instant
) {
    init {
        require(message.isNotBlank())
    }
}
