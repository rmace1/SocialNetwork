package com.revature.Project2.services;

import com.revature.Project2.models.User;
import com.revature.Project2.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jasypt.util.password.StrongPasswordEncryptor;


import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserService {
    private UserRepo userRepo;

    @Autowired

    public UserService(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    public User createUser(User user){
        User userFromDb = this.userRepo.findByUserName(user.getUserName());
        if(userFromDb != null)
            return null;

        //http://www.jasypt.org/index.html
        StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
        String encryptedPass = encryptor.encryptPassword(user.getPassword());
        user.setPassword(encryptedPass);

        return this.userRepo.save(user);
    }

    public List<User> getAllUsers(){
        return this.userRepo.findAll();
    }

    public User getOneUser(Integer userId){
        return this.userRepo.findById(userId).orElse(null);
    }

    public User updateUser(User user){

        StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
        String encryptedPass = encryptor.encryptPassword(user.getPassword());
        user.setPassword(encryptedPass);

        return userRepo.save(user);
    }

    public boolean delete(Integer userId){
        this.userRepo.deleteById(userId);
        User user = this.userRepo.findById(userId).orElse(null);
        if(user == null) {
            return true;
        }
        return false;
    }

    public User validateCredentials(User user){
        User userFromDb = this.userRepo.findByUserName(user.getUserName());

        if(userFromDb == null)
            return null;

        StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
        //checks to see if password matches encrypted password.
        if(!encryptor.checkPassword(user.getPassword(), userFromDb.getPassword())) {
            return null;
        }
        return userFromDb;
    }
}
