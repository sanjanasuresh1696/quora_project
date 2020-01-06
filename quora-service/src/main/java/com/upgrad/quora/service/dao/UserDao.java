package com.upgrad.quora.service.dao;



import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import java.util.UUID;


@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;


    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    public UserEntity getUserByName(final String userName) {
        try{
            return entityManager.createNamedQuery("getUserByUserName",UserEntity.class).setParameter("userName",userName).getSingleResult();

        } catch (NoResultException nre) {
            return null;
        }
    }

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


    public UserEntity getUserByUUID(String uuid) {
        try{
            return(UserEntity) entityManager.createNamedQuery("userByUuid",UserEntity.class).setParameter("uuid",uuid).getSingleResult();
        } catch(NoResultException nre) {
            return null;
        }
    }

}




