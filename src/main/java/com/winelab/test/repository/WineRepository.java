package com.winelab.test.repository;

import com.winelab.test.model.Wine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WineRepository extends JpaRepository<Wine, Long> {
    Wine findByName(String name);
    Wine findByUrl(String url);
}
