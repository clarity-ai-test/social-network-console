package app

import application.SocialNetworkService
import domain.repo.InMemoryFollowRepository
import domain.repo.InMemoryPostRepository
import domain.time.HumanTimeFormatter
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import parsing.CommandParser
import support.IncrementalClock
import support.MutableClock
import java.io.BufferedReader
import java.io.PrintWriter
import java.io.StringReader
import java.io.StringWriter
import java.time.Instant

class ConsoleAppIntegrationFreeSpec :
    FreeSpec({

        data class Scenario(
            val name: String,
            val input: String,
            val expectedLines: List<String>,
        )

        fun runScenario(input: String): List<String> {
            val reader = BufferedReader(StringReader(input.trimIndent()))
            val writer = StringWriter()

            val serviceClock = IncrementalClock(Instant.parse("2026-02-06T10:00:00Z"))
            val formatterClock = MutableClock(Instant.parse("2026-02-06T10:00:10Z"))

            val service =
                SocialNetworkService(
                    InMemoryPostRepository(),
                    InMemoryFollowRepository(),
                    serviceClock,
                )

            val app =
                ConsoleApp(
                    CommandParser(),
                    service,
                    HumanTimeFormatter(formatterClock),
                    reader,
                    PrintWriter(writer),
                )

            app.run()

            val out = writer.toString().trim()
            return if (out.isBlank()) emptyList() else out.lines()
        }

        val scenarios =
            listOf(
                Scenario(
                    name = "post + read (single message)",
                    input = """
                Alice -> Hello
                Alice
            """,
                    expectedLines =
                        listOf(
                            "Alice - Hello (10 seconds ago)",
                        ),
                ),
                Scenario(
                    name = "read orders timeline desc",
                    input = """
                Alice -> A1
                Alice -> A2
                Alice
            """,
                    expectedLines =
                        listOf(
                            "Alice - A2 (9 seconds ago)",
                            "Alice - A1 (10 seconds ago)",
                        ),
                ),
                Scenario(
                    name = "wall includes own + followees ordered desc",
                    input = """
                Alice -> Hello
                Bob -> Hi
                Alice follows Bob
                Alice wall
            """,
                    expectedLines =
                        listOf(
                            "Bob - Hi (9 seconds ago)",
                            "Alice - Hello (10 seconds ago)",
                        ),
                ),
                Scenario(
                    name = "wall excludes non-followed users",
                    input = """
                Alice -> A1
                Bob -> B1
                Alice wall
            """,
                    expectedLines =
                        listOf(
                            "Alice - A1 (10 seconds ago)",
                        ),
                ),
                Scenario(
                    name = "follow is idempotent (no duplicates on wall)",
                    input = """
                Bob -> B1
                Alice follows Bob
                Alice follows Bob
                Alice wall
            """,
                    expectedLines =
                        listOf(
                            "Bob - B1 (10 seconds ago)",
                        ),
                ),
                Scenario(
                    name = "wall aggregates multiple followees in global time order",
                    input = """
                Alice -> A1
                Bob -> B1
                Charlie -> C1
                Alice follows Bob
                Alice follows Charlie
                Alice wall
            """,
                    expectedLines =
                        listOf(
                            "Charlie - C1 (8 seconds ago)",
                            "Bob - B1 (9 seconds ago)",
                            "Alice - A1 (10 seconds ago)",
                        ),
                ),
                Scenario(
                    name = "multiple reads and walls print in command order",
                    input = """
                Alice -> A1
                Alice
                Bob -> B1
                Alice follows Bob
                Alice wall
                Bob
            """,
                    expectedLines =
                        listOf(
                            "Alice - A1 (10 seconds ago)",
                            "Bob - B1 (9 seconds ago)",
                            "Alice - A1 (10 seconds ago)",
                            "Bob - B1 (9 seconds ago)",
                        ),
                ),
                Scenario(
                    name = "messy spacing supported",
                    input = """
                   Alice    ->     Hello
                Bob->Hi
                Alice   follows   Bob
                  Alice    wall
            """,
                    expectedLines =
                        listOf(
                            "Bob - Hi (9 seconds ago)",
                            "Alice - Hello (10 seconds ago)",
                        ),
                ),
                Scenario(
                    name = "invalid commands and blanks are ignored",
                    input = """
                
                ->
                wall
                follows Bob
                Alice -> OK
                
                Alice
            """,
                    expectedLines =
                        listOf(
                            "Alice - OK (10 seconds ago)",
                        ),
                ),
                Scenario(
                    name = "wall for user with no posts prints nothing",
                    input = """
                Alice wall
            """,
                    expectedLines = emptyList(),
                ),
            )

        "ConsoleApp integration - scenarios" {
            scenarios.forEach { s ->
                runScenario(s.input) shouldBe s.expectedLines
            }
        }
    })
