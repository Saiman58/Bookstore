package com.BookLover.demo.service;

import com.BookLover.demo.exception.UserNotFoundException;
import com.BookLover.demo.model.entity.User;
import com.BookLover.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //Регистрация нового пользователя
    @Override
    @Transactional
    public User registerUser(User user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username" + user.getUsername() + "' уже занят");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email '" + user.getEmail() + "' уже зарегистрирован");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // Поиск пользователя по ID
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + id + " не найден"));
    }

    // Поиск пользователя по username
    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь '" + username + "' не найден"));
    }

    // Получить всех пользователей
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //Обновить данные пользователя
    @Transactional
    @Override
    public User updateUser(Long id, User updatedUser) {
        User existingUser = getUserById(id);

        if (updatedUser.getFirstName() != null) {
            existingUser.setFirstName(updatedUser.getFirstName());
        }

        if (updatedUser.getLastName() != null) {
            existingUser.setLastName(updatedUser.getLastName());
        }
        // Проверяем, что новый email не занят другим пользователем
        if (updatedUser.getEmail() != null) {
            userRepository.findByEmail(updatedUser.getEmail())
                    .ifPresent(user -> {
                        if (!user.getId().equals(id)) {
                            throw new RuntimeException("Email '" + updatedUser.getEmail() + "' уже используется");
                        }
                    });
            existingUser.setEmail(updatedUser.getEmail());
        }
        // Если меняется пароль - хэшируем
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.deleteById(id);
    }
}
