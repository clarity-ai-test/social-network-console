package domain.repo

import domain.model.Post
import domain.model.UserName
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import java.time.Instant

class InMemoryPostRepositoryFreeSpec :
    FreeSpec({

        "InMemoryPostRepository" - {

            "returns empty timeline for unknown user" {
                val repo = InMemoryPostRepository()
                repo.timeline(UserName("Alice")).shouldBeEmpty()
            }

            "stores and returns posts for a user" {
                val repo = InMemoryPostRepository()
                val t0 = Instant.parse("2026-02-06T10:00:00Z")

                repo.add(Post(UserName("Alice"), "Hello", t0))

                repo.timeline(UserName("Alice")) shouldBe
                    listOf(
                        Post(UserName("Alice"), "Hello", t0),
                    )
            }

            "does not mix posts between users" {
                val repo = InMemoryPostRepository()
                val t0 = Instant.parse("2026-02-06T10:00:00Z")

                repo.add(Post(UserName("Alice"), "A1", t0))
                repo.add(Post(UserName("Bob"), "B1", t0))

                repo.timeline(UserName("Alice")).map { it.message } shouldBe listOf("A1")
                repo.timeline(UserName("Bob")).map { it.message } shouldBe listOf("B1")
            }

            "returns a defensive copy (mutating result doesn't affect repository)" {
                val repo = InMemoryPostRepository()
                val t0 = Instant.parse("2026-02-06T10:00:00Z")

                repo.add(Post(UserName("Alice"), "A1", t0))

                val timeline = repo.timeline(UserName("Alice")).toMutableList()
                timeline.clear()

                repo.timeline(UserName("Alice")).size shouldBe 1
            }
        }
    })
