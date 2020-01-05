package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserAuthDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void updateUserAuth(UserAuthEntity userAuthEntity) {
        entityManager.merge(userAuthEntity);
    }

    public UserAuthEntity getAccessToken(String accessToken) {
        try {
            return entityManager.createNamedQuery("getUserAuthByAccessToken", UserAuthEntity.class).setParameter("accessToken", accessToken).getSingleResult();

        } catch (NoResultException nre) {
            return null;
        }

    }

}
