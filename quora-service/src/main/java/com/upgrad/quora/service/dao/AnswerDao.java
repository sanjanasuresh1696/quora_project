package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Persist the answer in the DB.
     *
     * @param answerEntity to be persisted.
     * @return Persisted answer.
     */
    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    /**
     * Fetches an answer from DB based on the answerId
     *
     * @param answerId id of the answer to be fetched.
     * @return Answer if there exist one with that id in DB else null.
     */
    public AnswerEntity getAnswerById(final String answerId) {
        try {
            return entityManager.createNamedQuery("getAnswerById", AnswerEntity.class).setParameter("uuid", answerId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * updates the row of information in answer table of DB.
     *
     * @param answerEntity answer to be updated.
     */
    public void updateAnswer(AnswerEntity answerEntity) {
        entityManager.merge(answerEntity);
    }

}
