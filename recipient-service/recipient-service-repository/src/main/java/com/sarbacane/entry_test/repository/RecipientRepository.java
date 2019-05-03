package com.sarbacane.entry_test.repository;

import com.sarbacane.entry_test.model.RecipientEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipientRepository extends CrudRepository<RecipientEntity, Long> {
    boolean existsByName(@Param("name") String name);
    RecipientEntity findByName(@Param("name") String name);
    void deleteByName(@Param("name") String name);
}