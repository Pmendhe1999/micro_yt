package com.quiz.controllers;

import com.quiz.config.IdentityClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/quiz-test")
public class TestController {

    @Autowired
    private IdentityClient identityClient;

    @GetMapping
    public String quizTest( @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");
        Map<String, Object> authData = identityClient.validateToken(token);
        String createdBy = (String) authData.get("username");
        String role = (String) authData.get("role");

        return "This is quiz test controller";
    }
}
