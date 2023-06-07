package com.salatin.account.controller;

import com.salatin.account.model.dto.response.UserResponseDto;
import com.salatin.account.service.UserService;
import com.salatin.account.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final UsersResource usersResource;

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Mono<UserResponseDto> getInfo(@PathVariable String id) {

        return userService.findById(id)
            .map(userRepresentation -> userMapper.toDto(userRepresentation, usersResource));
    }

    @PatchMapping("/{userId}/add-role")
    @PreAuthorize(value = "hasRole('admin')")
    public Mono<Void> addRoleToUser(@PathVariable String userId,
                              @RequestParam("value") String role) {
        return userService.addRoleByUserId(userId, role);
    }

    @PatchMapping("/{userId}/delete-role")
    @PreAuthorize(value = "hasRole('admin')")
    public Mono<Void> deleteRoleFromUser(@PathVariable String userId,
                                         @RequestParam("value") String role) {
        return userService.deleteRoleByUserId(userId, role);
    }
}
