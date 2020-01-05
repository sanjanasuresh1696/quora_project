package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;


    /**
     * Get the user given the id.
     *
     * @param userId id of the required user.
     * @return UserEntity if user with given id is found else null.
     */
    public UserEntity getUserById(String userId) {
        try {
            return entityManager.createNamedQuery("getUserById", UserEntity.class).setParameter("uuid", userId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserEntity deleteUser(final String userId) {
        UserEntity deleteUser = getUserById(userId);
        if (deleteUser != null) {
            this.entityManager.remove(deleteUser);
        }
        return deleteUser;
    }
}