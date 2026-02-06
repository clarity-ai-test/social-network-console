package parsing

sealed interface Command {
    data class Post(
        val user: String,
        val message: String,
    ) : Command

    data class Read(
        val user: String,
    ) : Command

    data class Follow(
        val user: String,
        val other: String,
    ) : Command

    data class Wall(
        val user: String,
    ) : Command
}
