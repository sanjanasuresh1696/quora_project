package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignUpBusinessService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    /**
     * @param userEntity
     * @return Persisted user entity
     * @throws SignUpRestrictedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {

        ///Encrypting the pass using PasswordCryptographyProvider by getting salt and encrypted password.
        String[] encrptedPassword = passwordCryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encrptedPassword[0]);
        userEntity.setPassword(encrptedPassword[1]);

        //Setting the user role by default as Nonadmin.
        userEntity.setRole("nonadmin");
        UserEntity existingUser = userDao.getUserByName(userEntity.getUserName());
        if (existingUser != null) {
            throw new SignUpRestrictedException("SGR-001", "Try any other Username,this Username has already been taken");
        } else {
            existingUser = userDao.getUserByEmail(userEntity.getEmail());
            if (existingUser != null)
                throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try any other emailId.");
        }
        return userDao.createUser(userEntity);

    }

}
