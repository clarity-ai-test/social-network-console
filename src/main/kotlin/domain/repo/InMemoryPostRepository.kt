package domain.repo

import domain.model.Post
import domain.model.UserName
import java.util.concurrent.ConcurrentHashMap

class InMemoryPostRepository : PostRepository {
    private val postsByUser: MutableMap<UserName, MutableList<Post>> = ConcurrentHashMap()

    override fun add(post: Post) {
        postsByUser.computeIfAbsent(post.author) { mutableListOf() }.add(post)
    }

    override fun timeline(user: UserName): List<Post> = postsByUser[user]?.toList().orEmpty()
}
