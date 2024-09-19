package com.chattest.repository;

import com.chattest.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    @Query("SELECT f FROM Feed f WHERE f.name like :name")
    List<Feed> findByName(@Param("name") String name);

}
