package com.winelab.test.repository;

import com.winelab.test.model.Wine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WineRepository extends JpaRepository<Wine, Long> {
    Wine findByName(String name);
}
