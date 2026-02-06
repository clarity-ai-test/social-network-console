package domain.repo

import domain.model.UserName

interface FollowRepository {
    fun follow(follower: UserName, followee: UserName)
    fun followeesOf(user: UserName): Set<UserName>
}
