package me.agronaut.springrest.Service;

import lombok.extern.log4j.Log4j2;
import me.agronaut.springrest.Model.Password;
import me.agronaut.springrest.Model.User;
import me.agronaut.springrest.Repository.PasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import me.agronaut.springrest.Util.Utils;

import java.util.List;

@Service
@Log4j2
public class PasswordService {
    private final PasswordRepository passwordRepository;

    @Autowired
    public PasswordService(PasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }

    public List<Password> getAllByUser(User user) {
        log.debug(Utils.SIMPLE_LOG_PATTERN, "user adatai:", "user", user.toString());
        List<Password> list = passwordRepository.findAllByUser(user);
        log.debug(Utils.SIMPLE_LOG_PATTERN,"Password lista merete:", "Lenght", list.size());
        return list;
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
