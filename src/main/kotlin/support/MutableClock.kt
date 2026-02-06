package support

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class MutableClock(
    private var current: Instant,
    private val zone: ZoneId = ZoneId.of("UTC"),
) : Clock() {
    fun set(instant: Instant) {
        current = instant
    }

    fun advanceSeconds(seconds: Long) {
        current = current.plusSeconds(seconds)
    }

    override fun instant(): Instant = current

    override fun getZone(): ZoneId = zone

    override fun withZone(zone: ZoneId): Clock = MutableClock(current, zone)
}
