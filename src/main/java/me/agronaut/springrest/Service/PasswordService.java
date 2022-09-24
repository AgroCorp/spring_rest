package me.agronaut.springrest.Service;

import me.agronaut.springrest.Model.Password;
import me.agronaut.springrest.Model.User;
import me.agronaut.springrest.Repository.PasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordService {
    private final PasswordRepository passwordRepository;

    @Autowired
    public PasswordService(PasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }

    public List<Password> getAllByUser(User user) {
        return passwordRepository.findAllByUser(user);
    }

    public Password getById(Long id) {
        return passwordRepository.getById(id);
    }

    public Password addPassword(Password pw) {
        return passwordRepository.save(pw);
    }

    public Password updatePassword(Password newPassword) {
        return passwordRepository.save(newPassword);
    }

    public void deletePassword(Long id) {
        Password deletedPassword = passwordRepository.getById(id);
        passwordRepository.delete(deletedPassword);
    }
}
