package me.agronaut.springrest.Controller;

import lombok.extern.log4j.Log4j2;
import me.agronaut.springrest.Model.Password;
import me.agronaut.springrest.Model.User;
import me.agronaut.springrest.Service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/password")
@CrossOrigin
@Log4j2
public class PasswordController {
    private final PasswordService passwordService;

    @Autowired
    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping("/add")
    public ResponseEntity<Password> addPassword(@RequestBody Password password) {
        return new ResponseEntity<>(passwordService.addPassword(password), HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Password> getById(@PathVariable("id")String encodedId) {
        Long decodedId = Long.parseLong(URLDecoder.decode(encodedId, StandardCharsets.UTF_8));

        return new ResponseEntity<>(passwordService.getById(decodedId), HttpStatus.OK);
    }

    @GetMapping("/getAllByUser")
    public ResponseEntity<List<Password>> getAllByUser(@RequestBody User user) {
        return new ResponseEntity<>(passwordService.getAllByUser(user), HttpStatus.OK);
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
