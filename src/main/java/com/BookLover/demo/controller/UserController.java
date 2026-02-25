package com.BookLover.demo.controller;

import com.BookLover.demo.dto.mapper.UserMapper;
import com.BookLover.demo.dto.request.UserCreateRequest;
import com.BookLover.demo.dto.request.UserUpdateRequest;
import com.BookLover.demo.dto.response.UserResponse;
import com.BookLover.demo.model.entity.User;
import com.BookLover.demo.model.entity.UserBook;
import com.BookLover.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    //POST /api/users/register - регистрация нового пользователя
    @PostMapping("register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody UserCreateRequest request) {  // @Valid для валидации

        User user = userMapper.toEntity(request);

        User createdUser = userService.registerUser(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userMapper.toResponse(createdUser));
    }

    //GET /api/users/{id} - получить пользователя по ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    //GET /api/users/username/{username} - получить по username
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    //GET /api/users - список всех пользователей
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponse> responses = users.stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    //PUT /api/users/{id} - обновить пользователя
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {

        User existingUser = userService.getUserById(id);
        User updatedUser = userMapper.updateEntity(existingUser, request);
        updatedUser = userService.updateUser(id, updatedUser);

        return ResponseEntity.ok(userMapper.toResponse(updatedUser));
    }

    //DELETE /api/users/{id} - удалить пользователя
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) { // НЕ УДАЛЯЕТ ПОЛЬЗОВАТЕЛЯ!
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
