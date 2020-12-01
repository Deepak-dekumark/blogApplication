package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class AnswerController {

    @Autowired
    AnswerBusinessService answerBusinessService;

    @RequestMapping(method = RequestMethod.POST, value = "/question/{questionId}/answer/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(final AnswerRequest answerRequest, @PathVariable("questionId") final String question_uuid, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        AnswerEntity newAnswer = answerBusinessService.createAnswer(answerRequest.getAnswer(), question_uuid, authorization);
        AnswerResponse answerResponse = new AnswerResponse().id(newAnswer.getUuid()).status("ANSWER CREATED");
        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);

    }

    @RequestMapping(method = RequestMethod.PUT, value = "/answer/edit/{answerId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswerContent(AnswerEditRequest answerEditRequest, @PathVariable("answerId") final String answer_uuid, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, AnswerNotFoundException {
        AnswerEntity updatedAnswerEntity = answerBusinessService.editAnswer(answer_uuid, answerEditRequest.getContent(),authorization);
        AnswerEditResponse answerEditResponse = new AnswerEditResponse().id(updatedAnswerEntity.getUuid()).status("ANSWER EDITED");
        return new ResponseEntity<AnswerEditResponse>(answerEditResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/answer/delete/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@PathVariable("answerId") final String answer_uuid, final String authorisation) throws AuthorizationFailedException, AnswerNotFoundException {
        AnswerEntity deletedAnswer=answerBusinessService.deleteAnswer(answer_uuid,authorisation);
        AnswerDeleteResponse answerDeleteResponse=new AnswerDeleteResponse().id(deletedAnswer.getUuid()).status("ANSWER DELETED");
        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);
    }

    @RequestMapping(method=RequestMethod.GET,value="answer/all/{questionId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDetailsResponse> getAllAnswersToQuestion(@PathVariable("questionId") final String question_uuid,final String authorisation) throws AuthorizationFailedException, InvalidQuestionException {
    List<AnswerEntity> getAllAnswers=answerBusinessService.getAllAnswersToQuestion(question_uuid,authorisation);
    QuestionEntity question=answerBusinessService.getQuestion(question_uuid);
    List<AnswerDetailsResponse> answersToQuestion=new ArrayList<>();
    for(AnswerEntity answerEntity:getAllAnswers){
        answersToQuestion.add(new AnswerDetailsResponse().id(answerEntity.getUuid()).
                questionContent(question.getUuid()).answerContent(answerEntity.getAns()));
    }
    }

}

