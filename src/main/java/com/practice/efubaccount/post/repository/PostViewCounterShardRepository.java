package com.practice.efubaccount.post.repository;

import com.practice.efubaccount.post.domain.PostViewCounterShard;
import com.practice.efubaccount.post.domain.PostViewCounterShardId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostViewCounterShardRepository
        extends JpaRepository<PostViewCounterShard, PostViewCounterShardId> {

    @Modifying
    @Query(value = """
            INSERT INTO post_view_counter_shard (post_id, shard_id, view_count)
            VALUES (:postId, :shardId, 1)
            ON DUPLICATE KEY UPDATE view_count = view_count + 1
            """, nativeQuery = true)
    int increaseViewCount(
            @Param("postId") Long postId,
            @Param("shardId") int shardId
    );
}