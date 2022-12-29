package me.agronaut.springrest.service;

import me.agronaut.springrest.model.Password;
import me.agronaut.springrest.model.User;
import me.agronaut.springrest.repository.PasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordService {
    private final PasswordRepository passwordRepository;

    @Autowired
    public PasswordService(PasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }

    public Page<Password> getAllByUser(User user, Pageable pageable) {
        List<Password> res = passwordRepository.findAllByUser(user);
        return new PageImpl<>(res , pageable, res.size());

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
