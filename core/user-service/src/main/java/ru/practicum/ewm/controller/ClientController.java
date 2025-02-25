package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.client.UserClient;
import ru.practicum.ewm.dto.user.UserShortDto;
import ru.practicum.ewm.service.UserService;

@RestController
@RequestMapping(path = "/internal/api/users")
@RequiredArgsConstructor
public class ClientController implements UserClient {
    private final UserService userService;

    @Override
    @GetMapping("/{userId}")
    public UserShortDto getById(@PathVariable Long userId) {
        return userService.getById(userId);
    }
}
