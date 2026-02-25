package com.BookLover.demo.service;


import com.BookLover.demo.model.entity.User;

import java.util.List;

public interface UserService {

    //Регистрация нового пользователя
    User registerUser(User user);

    // Поиск пользователя по ID
    User getUserById(Long id);

    // Поиск пользователя по username
    User getUserByUsername(String username);

    // Получить всех пользователей
    List<User> getAllUsers();

    //Обновить данные пользователя
    User updateUser(Long id, User updatedUser);

    // Удалить пользователя
    void deleteUser(Long id);
}
