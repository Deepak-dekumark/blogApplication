package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class AnswerDao {

    @PersistenceContext
    EntityManager entityManager;

    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    public AnswerEntity getAnswerByUuid(final String uuid) {
        try {
            return entityManager.createQuery("getAnswerByUUID", AnswerEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;

        }
    }

    public AnswerEntity updateAnswer(final AnswerEntity updatedAnswerEntity) {
        entityManager.merge(updatedAnswerEntity);
       return updatedAnswerEntity;
    }

    public AnswerEntity deleteAnswer(final AnswerEntity answerToBeDeleted){
        entityManager.remove(answerToBeDeleted);
        return answerToBeDeleted;
    }
}
