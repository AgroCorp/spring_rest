package me.agronaut.springrest.Controller;

import lombok.extern.log4j.Log4j2;
import me.agronaut.springrest.Model.Password;
import me.agronaut.springrest.Model.User;
import me.agronaut.springrest.Service.PasswordService;
import me.agronaut.springrest.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/password")
@CrossOrigin
public class PasswordController {
    private final PasswordService passwordService;
    private final UserService userService;

    @Autowired
    public PasswordController(PasswordService passwordService, UserService userService) {
        this.passwordService = passwordService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<Password> addPassword(@RequestBody Password password, HttpServletRequest request) {
        User currentUser = userService.getByUsername(request.getUserPrincipal().getName());
        password.setUser(currentUser);
        return new ResponseEntity<>(passwordService.addPassword(password), HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Password> getById(@PathVariable("id")String encodedId) {
        Long decodedId = Long.parseLong(URLDecoder.decode(encodedId, StandardCharsets.UTF_8));

        return new ResponseEntity<>(passwordService.getById(decodedId), HttpStatus.OK);
    }

    @GetMapping("/getAllByUser")
    public ResponseEntity<List<Password>> getAllByUser(HttpServletRequest request) {
        User currentUser = userService.getByUsername(request.getUserPrincipal().getName());
        return new ResponseEntity<>(passwordService.getAllByUser(currentUser), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Password> updatePassword(@RequestBody Password password) {
        return new ResponseEntity<>(passwordService.updatePassword(password), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePassword(@PathVariable("id")String encodedId) {
        Long decodedId = Long.parseLong(URLDecoder.decode(encodedId, StandardCharsets.UTF_8));
        passwordService.deletePassword(decodedId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
