package domain.repo

import domain.model.UserName
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder

class InMemoryFollowRepositoryFreeSpec : FreeSpec({

    "InMemoryFollowRepository" - {

        "returns empty followees for unknown user" {
            val repo = InMemoryFollowRepository()
            repo.followeesOf(UserName("Charlie")).shouldBeEmpty()
        }

        "stores follows" {
            val repo = InMemoryFollowRepository()

            repo.follow(UserName("Charlie"), UserName("Alice"))
            repo.follow(UserName("Charlie"), UserName("Bob"))

            repo.followeesOf(UserName("Charlie")) shouldContainExactlyInAnyOrder setOf(
                UserName("Alice"),
                UserName("Bob")
            )
        }

        "is idempotent (following twice doesn't duplicate)" {
            val repo = InMemoryFollowRepository()

            repo.follow(UserName("Charlie"), UserName("Alice"))
            repo.follow(UserName("Charlie"), UserName("Alice"))

            repo.followeesOf(UserName("Charlie")) shouldContainExactlyInAnyOrder setOf(
                UserName("Alice")
            )
        }

        "returns a defensive copy (mutating result doesn't affect repository)" {
            val repo = InMemoryFollowRepository()

            repo.follow(UserName("Charlie"), UserName("Alice"))

            val followees = repo.followeesOf(UserName("Charlie")).toMutableSet()
            followees.clear()

            repo.followeesOf(UserName("Charlie")) shouldContainExactlyInAnyOrder setOf(
                UserName("Alice")
            )
        }
    }
})