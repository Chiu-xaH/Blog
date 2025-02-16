package com.chiuxah.blog.mapper

import com.chiuxah.blog.model.bean.UserInfoSummary
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface FollowMapper {
    // 返回followee的粉丝数量
    @Select("SELECT COUNT(*) FROM user_follow WHERE followee_id = #{followeeId}")
    fun getFollowersCount(followeeId : Int) : Int
    // 返回follower的关注数量
     @Select("SELECT COUNT(*) FROM user_follow WHERE follower_id = #{followerId}")
    fun getFollowingCount(followerId: Int): Int
    // 返回followee的粉丝列表
    @Select("""
        SELECT u.id, u.username, u.photo
        FROM user_info u
        JOIN user_follow f ON u.id = f.follower_id
        WHERE f.followee_id = #{followeeId}
    """)
    fun getFollowersList(followeeId: Int): List<UserInfoSummary>
    // 返回follower的关注列表
    @Select("""
        SELECT u.id, u.username, u.photo
        FROM user_info u
        JOIN user_follow f ON u.id = f.followee_id
        WHERE f.follower_id = #{followerId}
    """)
    fun getFollowingList(followerId: Int): List<UserInfoSummary>
    // 关注
    // A关注B就是follow(A.id,B.id),A是B的粉丝，B是A的关注
    @Insert("INSERT INTO user_follow (follower_id, followee_id) VALUES (#{followerId}, #{followeeId})")
    fun follow(followerId: Int, followeeId: Int): Boolean
    // 取关
    @Delete("DELETE FROM user_follow WHERE follower_id = #{followerId} AND followee_id = #{followeeId}")
    fun unfollow(followerId: Int, followeeId: Int): Boolean
}

