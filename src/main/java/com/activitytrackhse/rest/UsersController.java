package com.activitytrackhse.rest;

import com.activitytrackhse.service.inter.ProjectService;
import com.activitytrackhse.service.inter.UserService;
import com.activitytrackhse.service.inter.WorkService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;

import com.activitytrackhse.model.*;


@RestController
@CrossOrigin
@RequestMapping(value = "/users")
public class UsersController {

    private UserService userService;
    private ProjectService projectService;
    private WorkService workService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsersController(@Autowired UserService userService,
                           @Autowired BCryptPasswordEncoder bCryptPasswordEncoder,
                           @Autowired ProjectService projectService,
                           @Autowired WorkService workService) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.projectService = projectService;
        this.workService = workService;
    }

    @GetMapping
    public ResponseEntity<?> getUsers() {
        final List<User> users = userService.readAll();
        return users != null &&  !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(method = RequestMethod.POST, consumes="application/json")
    public ResponseEntity<?> createUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody User user) {
        System.out.println(user);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        try {
            User admin = userService.getByNickname(userDetails.getUsername());
            if(!(admin.getRole().equals("Admin")) || !(admin.getRole().equals("SuperAdmin"))) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            userService.create(user);
        } catch(DataIntegrityViolationException ex) {
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

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        if(id < 1){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            User user = userService.read(id);
            return user != null
                    ? new ResponseEntity<>(user, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        if(id < 1){
            return new ResponseEntity<>("Incorrect id = " + id, HttpStatus.BAD_REQUEST);
        }
        User user = userService.read(id);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.delete(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/have")
    public ResponseEntity<?> readProjectUserHave(@PathVariable int id) {
        if(id < 1){
            return new ResponseEntity<>("Incorrect id = " + id, HttpStatus.BAD_REQUEST);
        }
        User user = userService.read(id);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<Project> projects = projectService.findAllByOwner(user);
        return projects != null
                ? new ResponseEntity<>(projects, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}/consist_in")
    public ResponseEntity<?> readProjectUserConsistIn(@PathVariable int id) {
        if(id < 1){
            return new ResponseEntity<>("Incorrect id = " + id, HttpStatus.BAD_REQUEST);
        }
        User user = userService.read(id);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if(Objects.equals(user.getRole(), "SuperAdmin") || Objects.equals(user.getRole(), "Admin")) {
            final List<Project> projects = projectService.readAll();
            return projects != null &&  !projects.isEmpty()
                    ? new ResponseEntity<>(projects, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Project> projects = workService.findAllByUser(user).stream().map(Work::getProject).toList();
        //List<Project> projects = user.getProjects();
        //System.out.println(projects);
        return projects != null
                ? new ResponseEntity<>(projects, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}