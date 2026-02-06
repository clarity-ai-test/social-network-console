package application

import domain.model.Post
import domain.model.UserName
import domain.repo.FollowRepository
import domain.repo.PostRepository
import java.time.Clock

class SocialNetworkService(
    private val posts: PostRepository,
    private val follows: FollowRepository,
    private val clock: Clock,
) {
    fun post(
        user: String,
        message: String,
    ) {
        val author = UserName(user.trim())
        val msg = message.trim()
        require(msg.isNotBlank())
        posts.add(Post(author, msg, clock.instant()))
    }

    fun read(user: String): List<Post> {
        val u = UserName(user.trim())
        return posts.timeline(u).sortedByDescending { it.timestamp }
    }

    fun follow(
        user: String,
        other: String,
    ) {
        val follower = UserName(user.trim())
        val followee = UserName(other.trim())
        follows.follow(follower, followee)
    }

    fun wall(user: String): List<Post> {
        val u = UserName(user.trim())
        val followees = follows.followeesOf(u)

        val allAuthors =
            buildSet {
                add(u)
                addAll(followees)
            }

        return allAuthors
            .flatMap { posts.timeline(it) }
            .sortedByDescending { it.timestamp }
    }
}
