package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionDeleteResponse;
import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.api.model.QuestionEditRequest;
import com.upgrad.quora.api.model.QuestionEditResponse;
import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    /**
     * Create a question
     *
     * @param questionRequest This object has the actual question content
     * @param accessToken     access token to authenticate user.
     * @return UUID of the question created in DB.
     * @throws AuthorizationFailedException In case the access token is invalid.
     */
    @PostMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(@RequestHeader("authorization") String accessToken, QuestionRequest questionRequest) throws AuthorizationFailedException {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent(questionRequest.getContent());
        questionEntity = questionService.createQuestion(questionEntity, accessToken);
        QuestionResponse questionResponse = new QuestionResponse();
        questionResponse.setId(questionEntity.getUuid().toString());
        questionResponse.setStatus("QUESTION CREATED");
        return new ResponseEntity<>(questionResponse, HttpStatus.CREATED);
    }

    /**
     * Get all questions
     *
     * @param accessToken access token to authenticate user.
     * @return List of all the questions created in DB.
     * @throws AuthorizationFailedException In case the access token is invalid.
     */
    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") String accessToken) throws AuthorizationFailedException {
        List<QuestionEntity> questions = questionService.getAllQuestions(accessToken);

        List<QuestionDetailsResponse> questionDetailsResponses = questions
                .stream()
                .map(this::buildQuestionDetailsResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(questionDetailsResponses, HttpStatus.OK);

    }

    /**
     * Edit a question
     *
     * @param accessToken         access token to authenticate user.
     * @param questionId          id of the question which has to be edited.
     * @param questionEditRequest new content for the question.
     * @return Id and status of the question edited.
     * @throws AuthorizationFailedException In case the access token is invalid.
     * @throws InvalidQuestionException     if question with questionId doesn't exist.
     */
    @PutMapping(path = "/edit/{questionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestion(@RequestHeader("authorization") String accessToken, @PathVariable("questionId") String questionId, QuestionEditRequest questionEditRequest) throws AuthorizationFailedException, InvalidQuestionException {
        QuestionEntity questionEntity = questionService.editQuestion(accessToken, questionId, questionEditRequest.getContent());
        QuestionEditResponse questionEditResponse = new QuestionEditResponse();
        questionEditResponse.setId(questionEntity.getUuid().toString());
        questionEditResponse.setStatus("QUESTION EDITED");
        return new ResponseEntity<>(questionEditResponse, HttpStatus.OK);
    }

    /**
     * Deletes a question
     *
     * @param accessToken access token to authenticate user.
     * @param questionId  id of the question which has to be deleted.
     * @return Id and status of the deleted question.
     * @throws AuthorizationFailedException In case the access token is invalid.
     * @throws InvalidQuestionException     if question with questionId doesn't exist.
     */
    @DeleteMapping(path = "/delete/{questionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@RequestHeader("authorization") String accessToken, @PathVariable("questionId") String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        QuestionEntity questionEntity = questionService.deleteQuestion(accessToken, questionId);
        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse();
        questionDeleteResponse.setId(questionEntity.getUuid().toString());
        questionDeleteResponse.setStatus("QUESTION DELETED");
        return new ResponseEntity<>(questionDeleteResponse, HttpStatus.OK);
    }


    /**
     * Gets all questions posted by a specific user
     *
     * @param accessToken access token to authenticate user.
     * @param userId      id of the user whose questions has to be deleted.
     * @return List of  Id and status of the questions posted by the user
     * @throws AuthorizationFailedException In case the access token is invalid.
     * @throws InvalidQuestionException     if question with questionId doesn't exist.
     */
    @GetMapping(path = "/all/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser(@RequestHeader("authorization") String accessToken, @PathVariable("userId") String userId) throws AuthorizationFailedException, UserNotFoundException {
        List<QuestionEntity> questions = questionService.getAllQuestionsByUser(accessToken, userId);

        List<QuestionDetailsResponse> questionDetailsResponses = questions
                .stream()
                .map(this::buildQuestionDetailsResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(questionDetailsResponses, HttpStatus.OK);
    }


    private QuestionDetailsResponse buildQuestionDetailsResponse(QuestionEntity questionEntity) {
        QuestionDetailsResponse questionDetailResponse = new QuestionDetailsResponse();
        questionDetailResponse.setId(questionEntity.getUuid().toString());
        questionDetailResponse.setContent(questionEntity.getContent());
        return questionDetailResponse;
    }

}
