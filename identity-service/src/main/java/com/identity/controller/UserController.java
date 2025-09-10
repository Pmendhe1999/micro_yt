package com.identity.controller;

import com.identity.dto.UserRegisterDto;
import com.identity.entity.UserCredential;
import com.identity.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping("/use")
    public String getUser(){
        return "You got all users";
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody UserRegisterDto dto,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {

        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );
                return ResponseEntity.badRequest().body(errors);
            }

            String token = authHeader.replace("Bearer ", "");
            String response = userService.saveUser(dto, token);

            return ResponseEntity.ok(Collections.singletonMap("message", response));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Unexpected error: " + e.getMessage()));
        }
    }

    // READ All
    @GetMapping
    public ResponseEntity<List<UserCredential>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // READ By ID
// READ By ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        return userService.getUserById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "User not found")));
    }

    // UPDATE User
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id,
                                        @Valid @RequestBody UserRegisterDto dto,
                                        BindingResult bindingResult,
                                        @RequestHeader("Authorization") String authHeader) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        String token = authHeader.replace("Bearer ", "");
        String response = userService.updateUser(id, dto, token);
        return ResponseEntity.ok(Collections.singletonMap("message", response));
    }

    // DELETE User
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id,
                                        @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String response = userService.deleteUser(id, token);

            return ResponseEntity.ok(Collections.singletonMap("message", response));

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Unexpected error: " + e.getMessage()));
        }
    }

}
