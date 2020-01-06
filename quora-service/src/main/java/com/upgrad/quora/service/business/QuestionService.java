package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserAuthDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class QuestionService {

    @Autowired
    private UserAuthDao userAuthDao;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDao userDao;

    /**
     * Creates question in the DB if the accessToken is valid.
     *
     * @param accessToken accessToken of the user for valid authentication.
     * @return returns the persisted question.
     * @throws AuthorizationFailedException ATHR-001 - if user token is not present in DB. ATHR-002 if the user has already signed out.
     */
    @Transactional
    public QuestionEntity createQuestion(QuestionEntity questionEntity, String accessToken) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByAccessToken(accessToken);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
        }
        questionEntity.setDate(ZonedDateTime.now());
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setUserEntity(userAuthEntity.getUserEntity());
        return questionDao.createQuestion(questionEntity);
    }

    /**
     * Returns all the questions in the DB if the accessToken is valid.
     *
     * @param accessToken accessToken of the user for valid authentication.
     * @throws AuthorizationFailedException ATHR-001 - if user token is not present in DB. ATHR-002 if the user has already signed out.
     */
    public List<QuestionEntity> getAllQuestions(String accessToken) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByAccessToken(accessToken);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions");
        }
        return questionDao.getAllQuestions();
    }


    /**
     * Returns all the questions in the DB if the accessToken is valid.
     *
     * @param accessToken accessToken of the user for valid authentication.
     * @throws AuthorizationFailedException ATHR-001 - if user token is not present in DB. ATHR-002 if the user has already signed out.
     */
    public QuestionEntity getQuestionById(String questionId, String accessToken) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByAccessToken(accessToken);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions");
        }
        QuestionEntity questionEntity=questionDao.getQuestionById(questionId);
        if(questionEntity==null)
            throw new InvalidQuestionException("QUES-001", "The question with entered uuid whose details are to be seen does not exist");
        return questionEntity;
    }


    /**
     * Returns all the questions posted by a user in the DB if the accessToken is valid.
     *
     * @param accessToken accessToken of the user for valid authentication.
     * @throws AuthorizationFailedException ATHR-001 - if user token is not present in DB. ATHR-002 if the user has already signed out.
     */
    public List<QuestionEntity> getAllQuestionsByUser(String accessToken, String userId) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByAccessToken(accessToken);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions posted by a user");
        }
        UserEntity userEntity = userDao.getUserById(userId);
        if (userEntity == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid whose question details are to be seen does not exist");
        }
        return questionDao.getAllQuestionsByUser();
    }

    /**
     * * Edit a question with the new content
     *
     * @param accessToken accessToken of the user for valid authentication.
     * @param questionId  id of the question to be edited.
     * @param content     new content for the already existing question.
     * @return edited QuestionEntity
     * @throws AuthorizationFailedException ATHR-001 - if user token is not present in DB. ATHR-002 if the user has already signed out.
     * @throws InvalidQuestionException     if the question with id doesn't exist.
     */
    @Transactional
    public QuestionEntity editQuestion(String accessToken, String questionId, String content) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByAccessToken(accessToken);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit the question");
        }
        QuestionEntity questionEntity = questionDao.getQuestionById(questionId);
        checkQuestionExistsAndOwnership(userAuthEntity, questionEntity, "edit");
        questionEntity.setContent(content);
        questionDao.updateQuestion(questionEntity);
        return questionEntity;
    }

    /**
     * * Delete a question given its id
     *
     * @param accessToken accessToken of the user for valid authentication.
     * @param questionId  id of the question to be deleted.
     * @return deleted QuestionEntity
     * @throws AuthorizationFailedException ATHR-001 - if user token is not present in DB. ATHR-002 if the user has already signed out.
     * @throws InvalidQuestionException     if the question with id doesn't exist.
     */
    @Transactional
    public QuestionEntity deleteQuestion(String accessToken, String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByAccessToken(accessToken);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete the question");
        }
        QuestionEntity questionEntity = questionDao.getQuestionById(questionId);
        if(questionEntity==null)
            throw new InvalidQuestionException("QUES-001","Entered question uuid does not exist");
        if(questionEntity.getUserEntity().getId().equals(userAuthEntity.getUserEntity().getId()))
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        checkQuestionExistsAndOwnership(userAuthEntity, questionEntity, "delete");
        questionDao.deleteQuestion(questionEntity);
        return questionEntity;
    }

    private void checkQuestionExistsAndOwnership(UserAuthEntity userAuthEntity, QuestionEntity questionEntity, String action) throws InvalidQuestionException, AuthorizationFailedException {
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }
        if (!questionEntity.getUserEntity().getUuid().equals(userAuthEntity.getUserEntity().getUuid())) {
            throw new AuthorizationFailedException("ATHR-003", String.format("Only the question owner can %s the question", action));
        }
    }

}
