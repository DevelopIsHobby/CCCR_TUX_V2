package com.machbase.tag.repository;

import com.machbase.tag.domain.Tag;
import com.machbase.tag.domain.TagPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, TagPK> {
    @Query(value = "SELECT * FROM tag WHERE name = :name", nativeQuery=true)
    List<Tag> findTagsByName(@Param("name") String name);
}
