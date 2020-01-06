package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

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
            return entityManager.createNamedQuery("getAnswerById", AnswerEntity.class).setParameter("answerId", answerId).getSingleResult();
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


    /**
     * Delete a answer by given answerId from the DB.
     *
     * @param answerId Id of the answer whose information is to be fetched.
     * @return Answer details which is to be deleted if exist in the DB else null.
     */

    public AnswerEntity deleteAnswer(final String answerId) {
        AnswerEntity deleteAnswer = getAnswerById(answerId);
        if (deleteAnswer != null) {
            entityManager.remove(deleteAnswer);
        }
        return deleteAnswer;
    }

    /**
     * @param questionId
     * @return the lsit of answers for the question
     */
    public List<AnswerEntity> getAllAnswersToQuestion(final String questionId) {
        return entityManager.createNamedQuery("getAllAnswersToQuestion", AnswerEntity.class).setParameter("uuid", questionId).getResultList();
    }
}
