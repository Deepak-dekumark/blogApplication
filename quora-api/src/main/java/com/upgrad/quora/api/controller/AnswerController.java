package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.AnswerEditRequest;
import com.upgrad.quora.api.model.AnswerRequest;
import com.upgrad.quora.api.model.AnswerResponse;
import com.upgrad.quora.service.business.AnswerBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AnswerController {

    @Autowired
    AnswerBusinessService answerBusinessService;

    @RequestMapping(method = RequestMethod.POST, value = "/question/{questionId}/answer/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(final AnswerRequest answerRequest, @PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        QuestionEntity question = answerBusinessService.getQuestionByUUId(questionId);
        UserAuthTokenEntity userAuthTokenEntity = answerBusinessService.getUserAuthTokenEntity(authorization);
        AnswerEntity answer = new AnswerEntity();
        answer.setUuid(UUID.randomUUID().toString());
        answer.setDate(ZonedDateTime.now());
        answer.setQuestion(question);
        answer.setAns(answerRequest.getAnswer());
        answer.setUser(userAuthTokenEntity.getUser());
        answerBusinessService.createAnswer(answer);
        AnswerResponse answerResponse = new AnswerResponse().id(answer.getUuid()).status("ANSWER CREATED");
        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method=RequestMethod.PUT,value="/answer/edit/{answerId}",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> editAnswerContent(AnswerEditRequest answerEditRequest, @PathVariable("answerId") final String answer_uuid,@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, AnswerNotFoundException {
     UserAuthTokenEntity authTokenEntity=answerBusinessService.getUserAuthTokenEntity(authorization);
     AnswerEntity answerEntity=answerBusinessService.getAnswerByUUId(answer_uuid);
     AnswerEntity updatedAnswerEntity =answerBusinessService.editAnswer(answerEntity,answer_uuid,answerEditRequest.getContent());
     AnswerResponse answerResponse=new AnswerResponse().id(updatedAnswerEntity.getUuid()).status("ANSWER EDITED");
        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.OK);
    }
}
