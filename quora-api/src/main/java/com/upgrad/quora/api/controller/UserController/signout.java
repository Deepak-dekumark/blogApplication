package com.upgrad.quora.api.controller.UserController;

import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.service.business.SignOutBuisnessService;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class signout {

    @Autowired
    SignOutBuisnessService signOutBuisnessService;

    @RequestMapping(method= RequestMethod.POST,path="/user/signout",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> signout(@RequestHeader("authorization")final String authorization) throws SignOutRestrictedException {
       UserAuthTokenEntity authToken=signOutBuisnessService.getUserAuthToken(authorization);
        signOutBuisnessService.updateAuthToken(authToken);
        UserEntity signedUser=authToken.getUser();
        SignoutResponse signoutResponse=new SignoutResponse();
        signoutResponse.id(signedUser.getUuid()).message("SIGNED OUT SUCCESSFULLY");
       return new ResponseEntity<SignoutResponse> (signoutResponse, HttpStatus.OK);
    }

}
