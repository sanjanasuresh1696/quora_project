package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Persist the question in the DB.
     *
     * @param questionEntity question to be persisted.
     * @return Persisted question.
     */
    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    /**
     * Fetches all the questions in the DB.
     *
     * @return List of questions.
     */
    public List<QuestionEntity> getAllQuestions() {
        return entityManager.createNamedQuery("getAllQuestions", QuestionEntity.class).getResultList();
    }

    /**
     * Fetches all the questions posted by a specific user in the DB.
     *
     * @return List of questions.
     */
    public List<QuestionEntity> getAllQuestionsByUser() {
        return entityManager.createNamedQuery("getAllQuestionsByUser", QuestionEntity.class).getResultList();
    }

    /**
     * Get the question given its id.
     *
     * @param questionId id of the required question.
     * @return QuestionEntity if question with given id is found else null.
     */
    public QuestionEntity getQuestionById(String questionId) {
        try {
            return entityManager.createNamedQuery("getQuestionById", QuestionEntity.class).setParameter("questionId", questionId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Update the question with the new request
     *
     * @param questionEntity question entity to be updated.
     */
    public void updateQuestion(QuestionEntity questionEntity) {
        entityManager.merge(questionEntity);
    }


    /**
     * Deletes the question with the given id
     *
     * @param questionEntity question entity to be updated.
     */
    public void deleteQuestion(QuestionEntity questionEntity) {
        entityManager.remove(questionEntity);
    }

}
