package com.machbase.lookup.repository;

import com.machbase.lookup.domain.Lookup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LookupRepository extends JpaRepository<Lookup, Long> {
    List<Lookup> findByName(String name);
}
