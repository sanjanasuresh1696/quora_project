package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    private UserBusinessService userBusinessService;


    /**
     * @param userUuid
     * @param accesstoken
     * @return Get User Details
     * @throws AuthorizationFailedException
     * @throws UserNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, path = "/userprofile/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> getUser(@PathVariable("userId") final String userUuid, @RequestHeader("authorization") final String accesstoken) throws AuthorizationFailedException, UserNotFoundException {
        final UserEntity userEntity = userBusinessService.getUserByUUID(userUuid);
        final UserAuthEntity userAuthEntity = userBusinessService.getAccessToken(accesstoken);
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
        userDetailsResponse.firstName(userEntity.getFirstName());
        userDetailsResponse.lastName(userEntity.getLastName());
        userDetailsResponse.userName(userEntity.getUserName());
        userDetailsResponse.emailAddress(userEntity.getEmail());
        userDetailsResponse.country(userEntity.getCountry());
        userDetailsResponse.aboutMe(userEntity.getAboutMe());
        userDetailsResponse.dob(userEntity.getDob());
        userDetailsResponse.contactNumber(userEntity.getContactNumber());
        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
    }
}
