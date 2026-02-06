package application

import domain.model.UserName
import domain.repo.InMemoryFollowRepository
import domain.repo.InMemoryPostRepository
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import support.MutableClock
import java.time.Instant

class SocialNetworkServiceFreeSpec :
    FreeSpec({

        "SocialNetworkService" - {

            "post + read" - {

                "post stores a post with current time" {
                    val clock = MutableClock(Instant.parse("2026-02-06T10:00:00Z"))
                    val posts = InMemoryPostRepository()
                    val follows = InMemoryFollowRepository()
                    val service = SocialNetworkService(posts, follows, clock)

                    service.post("Alice", "Hello")

                    val timeline = service.read("Alice")
                    timeline.size shouldBe 1
                    timeline[0].author shouldBe UserName("Alice")
                    timeline[0].message shouldBe "Hello"
                    timeline[0].timestamp shouldBe Instant.parse("2026-02-06T10:00:00Z")
                }

                "read returns posts in reverse chronological order" {
                    val clock = MutableClock(Instant.parse("2026-02-06T10:00:00Z"))
                    val posts = InMemoryPostRepository()
                    val follows = InMemoryFollowRepository()
                    val service = SocialNetworkService(posts, follows, clock)

                    service.post("Alice", "first")
                    clock.set(Instant.parse("2026-02-06T10:00:01Z"))
                    service.post("Alice", "second")
                    clock.set(Instant.parse("2026-02-06T10:00:02Z"))
                    service.post("Alice", "third")

                    service.read("Alice").map { it.message } shouldBe listOf("third", "second", "first")
                }

                "does not mix users" {
                    val clock = MutableClock(Instant.parse("2026-02-06T10:00:00Z"))
                    val posts = InMemoryPostRepository()
                    val follows = InMemoryFollowRepository()
                    val service = SocialNetworkService(posts, follows, clock)

                    service.post("Alice", "A1")
                    service.post("Bob", "B1")

                    service.read("Alice").map { it.message } shouldBe listOf("A1")
                    service.read("Bob").map { it.message } shouldBe listOf("B1")
                }
            }

            "follow + wall" - {

                "wall contains user's posts and followees' posts ordered by time desc" {
                    val clock = MutableClock(Instant.parse("2026-02-06T10:00:00Z"))
                    val posts = InMemoryPostRepository()
                    val follows = InMemoryFollowRepository()
                    val service = SocialNetworkService(posts, follows, clock)

                    service.post("Alice", "A1")
                    clock.set(Instant.parse("2026-02-06T10:00:01Z"))
                    service.post("Bob", "B1")
                    clock.set(Instant.parse("2026-02-06T10:00:02Z"))
                    service.post("Charlie", "C1")

                    service.follow("Alice", "Bob")
                    service.follow("Alice", "Charlie")

                    service.wall("Alice").map { it.message } shouldBe listOf("C1", "B1", "A1")
                }

                "wall includes own posts even if user follows nobody" {
                    val clock = MutableClock(Instant.parse("2026-02-06T10:00:00Z"))
                    val posts = InMemoryPostRepository()
                    val follows = InMemoryFollowRepository()
                    val service = SocialNetworkService(posts, follows, clock)

                    service.post("Alice", "A1")

                    service.wall("Alice").map { it.message } shouldBe listOf("A1")
                }

                "wall does not include posts from non-followed users" {
                    val clock = MutableClock(Instant.parse("2026-02-06T10:00:00Z"))
                    val posts = InMemoryPostRepository()
                    val follows = InMemoryFollowRepository()
                    val service = SocialNetworkService(posts, follows, clock)

                    service.post("Alice", "A1")
                    clock.set(Instant.parse("2026-02-06T10:00:01Z"))
                    service.post("Bob", "B1")

                    service.wall("Alice").map { it.message } shouldBe listOf("A1")
                }
            }
        }
    })
