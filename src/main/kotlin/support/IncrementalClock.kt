package support

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class IncrementalClock(
    start: Instant,
    private val stepSeconds: Long = 1L,
    private val zone: ZoneId = ZoneId.of("UTC"),
) : Clock() {
    private var current = start

    override fun instant(): Instant {
        val now = current
        current = current.plusSeconds(stepSeconds)
        return now
    }

    override fun getZone(): ZoneId = zone

    override fun withZone(zone: ZoneId): Clock = IncrementalClock(current, stepSeconds, zone)
}
