package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.UserAuthEntity;
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
     * @param userEntity
     * @return Persisted user
     */
    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    /**
     * @param userName
     * @return User with the given name
     */
    public UserEntity getUserByName(final String userName) {
        try {
            return entityManager.createNamedQuery("getUserByUserName", UserEntity.class).setParameter("userName", userName).getSingleResult();

        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * @param email
     * @return User with the given email
     */
    public UserEntity getUserByEmail(final String email) {
        try {
            return entityManager.createNamedQuery("getUserByEMail", UserEntity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

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


    public UserAuthEntity createAuthToken(final UserAuthEntity userAuthEntity) {
        entityManager.persist(userAuthEntity);
        return userAuthEntity;
    }

    public void updateUser(final UserEntity updatedUserEntity) {
        entityManager.merge(updatedUserEntity);
    }

    /**
     * @param uuid
     * @return User with the given UUID
     */
    public UserEntity getUserByUUID(String uuid) {
        try {
            return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * @param userId
     * @return Deleted user entity
     */
    public UserEntity deleteUser(final String userId) {
        UserEntity deleteUser = getUserById(userId);
        if (deleteUser != null) {
            this.entityManager.remove(deleteUser);
        }
        return deleteUser;
    }

}




