package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupBusinessService {
    @Autowired
    private UserDao userDao;

    @Autowired
    PasswordCryptographyProvider cryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity){
      String password=userEntity.getPassword();
      String [] encryptedText=cryptographyProvider.encrypt(password);
      userEntity.setPassword(encryptedText[1]);
      userEntity.setSalt(encryptedText[0]);
      return userDao.createUser(userEntity);
     }

     public UserEntity getUserByUserName(final String userName){
        return userDao.getUserByUserName(userName);
       }

    public UserEntity getUserByEmail(final String email){
        return userDao.getUserByEmail(email);
    }

}
