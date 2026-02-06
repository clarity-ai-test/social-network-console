package domain.time

import java.time.Instant

interface TimeFormatter {
    fun formatSince(instant: Instant): String
}
