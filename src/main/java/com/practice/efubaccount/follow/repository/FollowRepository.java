package com.practice.efubaccount.follow.repository;

import com.practice.efubaccount.account.domain.Account;
import com.practice.efubaccount.follow.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowee(Account requester, Account target);

    Optional<Follow> findByFollowerAndFollowee(Account follower, Account followee);

    @Query("SELECT f FROM Follow f JOIN FETCH f.follower WHERE f.followee = :requester")
    List<Follow> findAllByFollowee(@Param("requester") Account requester);

    @Query("SELECT f FROM Follow f JOIN FETCH f.followee WHERE f.follower = :requester")
    List<Follow> findAllByFollower(@Param("requester") Account requester);
}
