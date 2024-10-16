package com.rdv.server.core.repository.custom;


import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;


@Repository
public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final EntityManager entityManager;

    public CustomUserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
