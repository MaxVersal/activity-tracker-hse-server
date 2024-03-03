package com.activitytrackhse.rest;

import com.activitytrackhse.service.inter.UserService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.activitytrackhse.model.*;


@RestController
@CrossOrigin
@RequestMapping(value = "/account")
public class AccountController {
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AccountController(@Autowired UserService userService,
                           @Autowired BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PutMapping(value = "/update")
    public ResponseEntity<?> updateMyInfo(@AuthenticationPrincipal UserDetails userDetails, @RequestBody User user) {
        User me = userService.getByNickname(userDetails.getUsername());
        if(me == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        me.setNewInfo(user);
        try{
            userService.save(me);
        }catch (DataIntegrityViolationException ex){
            ConstraintViolationException e = (ConstraintViolationException) ex.getCause();
            String message;
            if (e.getConstraintName().equals("user_phone_key")) {
                message = "A user with this phone has already been created!";
            }else if (e.getConstraintName().equals("user_nickname_key")) {
                message = "A user with this nickname has already been created!";
            }else if (e.getConstraintName().equals("user_email_key")) {
                message = "A user with this email has already been created!";
            }else {
                message = e.getSQLException().getMessage();
            }
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/update_password")
    public ResponseEntity<?> updatePassword(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestParam(name = "old_password") String oldPassword,
                                            @RequestParam(name = "new_password") String newPassword) {
        if(!bCryptPasswordEncoder.matches(oldPassword, userDetails.getPassword())){
            return new ResponseEntity<>("Invalid old password", HttpStatus.BAD_REQUEST);
        }
        if(newPassword.length() < 5 || newPassword.length() > 20){
            return new ResponseEntity<>("Invalid password", HttpStatus.BAD_REQUEST);
        }
        User user = userService.getByNickname(userDetails.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
