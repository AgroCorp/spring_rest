package me.agronaut.springrest.Controller;

import lombok.extern.log4j.Log4j2;
import me.agronaut.springrest.Model.User;
import me.agronaut.springrest.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin
@Log4j2
public class LoginController {
    private final UserService service;
    @Autowired
    public LoginController(UserService service) {
        this.service = service;
    }


    @PostMapping("/register")
    public User register(@RequestBody User newUser) throws UserService.UserExistByEmailException
    {
        return service.register(newUser);
    }

    @GetMapping("/activate/{token}")
    public ResponseEntity<?> activate(@PathVariable String token) {
        log.debug("{}:\n\t[{}]\t{}","raw token", "token", token);
        Long userId = Long.valueOf(new String(Base64Utils.decodeFromUrlSafeString(token)));

        service.activate(userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public User login(@RequestBody User loginUser) throws UserService.NotActiveUserException
    {
        log.debug("requestben kapott user: \n\t{}", loginUser);
        return service.login(loginUser);
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<?> forgotPassword(@RequestBody String email) {
        log.debug("{}\n\t[{}]\t{}", "requestben kapott email", "email",email);
        service.reset_password(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping ("/set_new_password")
    public User setNewPassword(@RequestBody Map<String,String> jsonData) {
        log.debug("{}\n\t[{}]\t{}\n\t[{}]\t{}", "requestben kapott adatok", "token",jsonData.get("token"), "password", jsonData.get("password"));
        Long userId = Long.valueOf(new String(Base64Utils.decodeFromUrlSafeString(jsonData.get("token"))));
        return service.set_new_password(jsonData.get("password"), userId);
    }

    @Secured("ADMIN")
    @PostMapping(value = "/list_all_user", consumes = "application/json", produces = "application/json")
    public List<User> getAll(@RequestBody User user) {
        log.info("user to search: " + user);

        return service.getAll(user);
    }
}
