package com.activitytrackhse.rest;

import com.activitytrackhse.model.*;
import com.activitytrackhse.security.JwtTokenProvider;
import com.activitytrackhse.service.inter.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class LoginController {
    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;

    public LoginController(@Autowired UserService userService,
                             @Autowired AuthenticationManager authenticationManager,
                             @Autowired JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping(value = "/login")
    public ResponseEntity<?> login(@RequestParam String nickname, @RequestParam String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(nickname, password));
        User user = userService.getByNickname(nickname);
        //System.out.println(user);
        Map<Object, Object> model = new HashMap<>();
        if (user != null) {
            List<String> roles = new ArrayList<>();
            roles.add(user.getRole());
            String token = jwtTokenProvider.createToken(nickname, roles);
            model.put("user", user);
            model.put("token", token);
            //model.put("role", user.getRole());
            return new ResponseEntity<>(model, HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
