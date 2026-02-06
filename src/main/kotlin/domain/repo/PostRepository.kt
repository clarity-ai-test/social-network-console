package domain.repo

import domain.model.Post
import domain.model.UserName

interface PostRepository {
    fun add(post: Post)
    fun timeline(user: UserName): List<Post>
}
