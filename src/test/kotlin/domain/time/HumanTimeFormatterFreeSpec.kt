package domain.time

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import support.MutableClock
import java.time.Instant

class HumanTimeFormatterFreeSpec :
    FreeSpec({

        "HumanTimeFormatter" - {

            "formats seconds" {
                val clock = MutableClock(Instant.parse("2026-02-06T10:00:10Z"))
                val formatter = HumanTimeFormatter(clock)

                formatter.formatSince(Instant.parse("2026-02-06T10:00:05Z")) shouldBe
                    "5 seconds ago"
            }

            "formats 1 minute (singular)" {
                val clock = MutableClock(Instant.parse("2026-02-06T10:02:00Z"))
                val formatter = HumanTimeFormatter(clock)

                formatter.formatSince(Instant.parse("2026-02-06T10:01:00Z")) shouldBe
                    "1 minute ago"
            }

            "formats minutes (plural)" {
                val clock = MutableClock(Instant.parse("2026-02-06T10:05:00Z"))
                val formatter = HumanTimeFormatter(clock)

                formatter.formatSince(Instant.parse("2026-02-06T10:01:00Z")) shouldBe
                    "4 minutes ago"
            }

            "formats hours" {
                val clock = MutableClock(Instant.parse("2026-02-06T12:00:00Z"))
                val formatter = HumanTimeFormatter(clock)

                formatter.formatSince(Instant.parse("2026-02-06T10:00:00Z")) shouldBe
                    "2 hours ago"
            }

            "formats days" {
                val clock = MutableClock(Instant.parse("2026-02-08T10:00:00Z"))
                val formatter = HumanTimeFormatter(clock)

                formatter.formatSince(Instant.parse("2026-02-06T10:00:00Z")) shouldBe
                    "2 days ago"
            }

            "handles future instants" {
                val clock = MutableClock(Instant.parse("2026-02-06T10:00:00Z"))
                val formatter = HumanTimeFormatter(clock)

                formatter.formatSince(Instant.parse("2026-02-06T10:00:10Z")) shouldBe
                    "10 seconds ago"
            }
        }
    })
