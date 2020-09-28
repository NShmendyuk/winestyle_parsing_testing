package com.winelab.test.repository;

import com.winelab.test.model.Wine;
import org.springframework.data.repository.CrudRepository;

public interface WineRepository extends CrudRepository<Wine, Long> {
}
