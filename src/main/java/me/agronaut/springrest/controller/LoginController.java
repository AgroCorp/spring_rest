package me.agronaut.springrest.controller;

import me.agronaut.springrest.model.User;
import me.agronaut.springrest.model.UserDto;
import me.agronaut.springrest.service.UserService;
import me.agronaut.springrest.util.LogUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class LoginController {
    @Qualifier("UserService")
    private final UserService service;
    private final ModelMapper modelMapper;

    private final LogUtil log = new LogUtil(getClass());

    @Autowired
    public LoginController(UserService service, ModelMapper mapper) {
        this.service = service;
        this.modelMapper = mapper;
    }

    @PostMapping("/register")
    public User register(@RequestBody UserDto newUser) throws UserService.UserExistByEmailException
    {
        return service.register(modelMapper.map(newUser, User.class));
    }

    @GetMapping("/activate/{token}")
    public ResponseEntity<HttpStatus> activate(@PathVariable String token) {
        Long userId = Long.valueOf(new String(Base64Utils.decodeFromUrlSafeString(token)));

        service.activate(userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public User login(@RequestBody UserDto loginUser) throws UserService.NotActiveUserException
    {
        return service.login(modelMapper.map(loginUser, User.class));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<HttpStatus> forgotPassword(@RequestBody String email) {
        service.resetPassword(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping ("/set-new-password")
    public User setNewPassword(@RequestBody Map<String,String> jsonData) {
        Long userId = Long.valueOf(new String(Base64Utils.decodeFromUrlSafeString(jsonData.get("token"))));
        return service.setNewPassword(jsonData.get("password"), userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/list-all-user", consumes = "application/json", produces = "application/json")
    public Page<User> getAll(@RequestBody UserDto user, Pageable pageable) {
        log.debug("get-all-user endpoint data", "user", user);
        return service.getAll( modelMapper.map(user, User.class), pageable);
    }

    @GetMapping(value = "/get-all-by-role/{roleName}", produces = "application/json")
    public List<UserDto> getAllByRole(@PathVariable String roleName) {
        return service.getAllByRole(roleName);
    }
}
