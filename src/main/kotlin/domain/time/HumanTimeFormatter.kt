package domain.time

import java.time.Clock
import java.time.Duration
import java.time.Instant
import kotlin.math.abs

class HumanTimeFormatter(
    private val clock: Clock,
) : TimeFormatter {
    override fun formatSince(instant: Instant): String {
        val now = clock.instant()
        val seconds = abs(Duration.between(instant, now).seconds)

        return when {
            seconds < SECONDS_PER_MINUTE ->
                unit(seconds, "second")

            seconds < SECONDS_PER_HOUR ->
                unit(seconds / SECONDS_PER_MINUTE, "minute")

            seconds < SECONDS_PER_DAY ->
                unit(seconds / SECONDS_PER_HOUR, "hour")

            else ->
                unit(seconds / SECONDS_PER_DAY, "day")
        }
    }

    private fun unit(
        value: Long,
        name: String,
    ): String {
        val n = if (value <= 0) 0 else value
        val label = if (n == 1L) name else "${name}s"
        return "$n $label ago"
    }

    private companion object {
        private const val SECONDS_PER_MINUTE = 60L
        private const val SECONDS_PER_HOUR = 60L * SECONDS_PER_MINUTE
        private const val SECONDS_PER_DAY = 24L * SECONDS_PER_HOUR
    }
}
