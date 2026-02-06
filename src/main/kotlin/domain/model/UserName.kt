package domain.model

@JvmInline
value class UserName(
    val value: String,
) {
    init {
        require(value.isNotBlank())
    }
}
