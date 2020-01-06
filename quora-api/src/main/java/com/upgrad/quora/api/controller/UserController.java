package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.SignInBusinessService;
import com.upgrad.quora.service.business.SignUpBusinessService;
import com.upgrad.quora.service.business.SignOutBusinessService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class UserController {


    @Autowired
    private SignUpBusinessService signUpBusinessService;

    @Autowired
    private SignInBusinessService signInBusinessService;

    @Autowired
    private SignOutBusinessService signOutBusinessService;

    @RequestMapping(method = RequestMethod.POST, path = "/user/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> SignUp(final SignupUserRequest signupUserRequest) throws SignUpRestrictedException {

        final UserEntity userEntity = new UserEntity();
        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setFirstName(signupUserRequest.getFirstName());
        userEntity.setLastName(signupUserRequest.getLastName());
        userEntity.setUserName(signupUserRequest.getUserName());
        userEntity.setEmail(signupUserRequest.getEmailAddress());
        userEntity.setPassword(signupUserRequest.getPassword());
        userEntity.setCountry(signupUserRequest.getCountry());
        userEntity.setAboutMe(signupUserRequest.getAboutMe());
        userEntity.setContactNumber(signupUserRequest.getContactNumber());

        final UserEntity createdUserEntity = signUpBusinessService.signup(userEntity);
        SignupUserResponse userResponse = new SignupUserResponse()
                .id(createdUserEntity.getUuid().toString())
                .status("USER SUCCESSFULLY REGISTERED");

        return new ResponseEntity<SignupUserResponse>(userResponse, HttpStatus.CREATED);
    }


    @RequestMapping(method = RequestMethod.POST, path = "/user/signin", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> Signin(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {
        byte[] decode = Base64.getDecoder().decode(authorization);
        String decodeText = new String(decode);
        String[] decodeArray = decodeText.split(":");

        UserAuthEntity userAuthEntity = signInBusinessService.authenticate(decodeArray[0],decodeArray[1]);

        UserEntity user = userAuthEntity.getUserEntity();

        SigninResponse signinresponse = new SigninResponse().id(user.getUuid().toString()).message("SIGNED IN SUCCESSFULLY");
        HttpHeaders headers = new HttpHeaders();
        headers.add("access_token",userAuthEntity.getAccessToken());
        return new ResponseEntity<SigninResponse>(signinresponse,headers,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/user/signout", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> Signout(@RequestHeader("access_token") final String accesstoken) throws SignOutRestrictedException {
        UserAuthEntity userAuthEntity= signOutBusinessService.authenticate(accesstoken);
        SignoutResponse userSignOutResponse= new SignoutResponse()
                .id(userAuthEntity.getUuid().toString())
                .message("SIGNED OUT SUCCESSFULLY");
        return new ResponseEntity<SignoutResponse>(userSignOutResponse,HttpStatus.OK);
    }

}


