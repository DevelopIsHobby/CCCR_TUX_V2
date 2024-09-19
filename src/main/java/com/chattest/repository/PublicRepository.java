package com.chattest.repository;

import com.chattest.entity.Feed;
import com.chattest.entity.Public;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PublicRepository extends JpaRepository<Public, Long> {
    @Query("SELECT f FROM Public f WHERE f.writer like 'admin' order by f.pno desc")
    List<Public> findByName();
}
