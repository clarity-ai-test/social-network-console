package domain.repo

import domain.model.UserName
import java.util.concurrent.ConcurrentHashMap

class InMemoryFollowRepository : FollowRepository {
    private val graph: MutableMap<UserName, MutableSet<UserName>> = ConcurrentHashMap()

    override fun follow(follower: UserName, followee: UserName) {
        graph.computeIfAbsent(follower) { ConcurrentHashMap.newKeySet() }.add(followee)
    }

    override fun followeesOf(user: UserName): Set<UserName> =
        graph[user]?.toSet().orEmpty()
}
