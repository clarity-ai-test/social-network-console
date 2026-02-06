package parsing

class CommandParser {

    private val post = Regex("""^(?<user>.+?)\s*->\s*(?<msg>.+?)$""")
    private val wall = Regex("""^(?<user>\S.+?)\s+wall$""", RegexOption.IGNORE_CASE)
    private val follows = Regex("""^(?<user>\S.+?)\s+follows\s+(?<other>\S.+?)$""", RegexOption.IGNORE_CASE)
    private val singleToken = Regex("""^\S+$""")

    fun parse(line: String): Command? {
        val trimmed = line.trim()
        if (trimmed.isBlank()) return null

        post.matchEntire(trimmed)?.let { m ->
            val user = m.groups["user"]!!.value.trim()
            val msg = m.groups["msg"]!!.value.trim()
            if (user.isBlank() || msg.isBlank()) return null
            return Command.Post(user, msg)
        }

        wall.matchEntire(trimmed)?.let { m ->
            val user = m.groups["user"]!!.value.trim()
            if (user.isBlank() || user.equals("wall", ignoreCase = true)) return null
            return Command.Wall(user)
        }

        follows.matchEntire(trimmed)?.let { m ->
            val user = m.groups["user"]!!.value.trim()
            val other = m.groups["other"]!!.value.trim()
            if (user.isBlank() || other.isBlank()) return null
            if (user.equals("follows", ignoreCase = true)) return null
            return Command.Follow(user, other)
        }

        if (trimmed.equals("wall", ignoreCase = true)) return null
        if (trimmed.equals("follows", ignoreCase = true)) return null
        if (trimmed.startsWith("->") || trimmed.endsWith("->")) return null

        if (!singleToken.matches(trimmed)) return null
        return Command.Read(trimmed)
    }
}