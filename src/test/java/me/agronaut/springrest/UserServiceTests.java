package me.agronaut.springrest;


import me.agronaut.springrest.Model.User;
import me.agronaut.springrest.Repository.UserRepository;
import me.agronaut.springrest.Service.EmailService;
import me.agronaut.springrest.Service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringRestApplication.class)
class UserServiceTests {
   @MockBean
    private UserRepository userRepository;
   @MockBean
   private EmailService emailSD;

   private final User testUser = new User();

   @Autowired
   private BCryptPasswordEncoder encoder;

   @Autowired
   private UserService userSD;

    @Test
    void testRegister() throws UserService.UserExistByEmailException {

        // new user does not exist
        testUser.setPassword("testPassword");
        testUser.setEmail("test@user.com");
        testUser.setId(1L);
//        when(testUser.getPassword()).thenReturn("testPassword");
//        when(testUser.getEmail()).thenReturn("test@user.com");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userRepository.existsUserByEmail("test@user.com")).thenReturn(false);
        doNothing().when(emailSD).sendEmail(anyString(),anyString(),anyString(),anyString());

        Long insertedId = userSD.register(testUser).getId();
        assertNotNull(insertedId);

        // new user exist
        when(userRepository.existsUserByEmail("test@user.com")).thenReturn(true);
        assertThrows(UserService.UserExistByEmailException.class, () -> userSD.register(testUser));
    }

    @Test
    void testLogin() {
//        when(testUser.getActive()).thenReturn(true);
//        when(testUser.getUsername()).thenReturn("testUser");
//        when(testUser.getPassword()).thenReturn("testPassword");

        testUser.setActive(true);
        testUser.setUsername("testUser");
        testUser.setPassword("testPassword");

        User mockUser = new User();
        mockUser.setPassword(encoder.encode("testPassword"));
        mockUser.setActive(true);
        mockUser.setUsername("testUser");
        when(userRepository.getUserByUsername(anyString())).
                thenReturn(Optional.of(mockUser));
        // everything ok
        assertDoesNotThrow(() -> userSD.login(testUser));

        // not active user
        mockUser.setActive(false);
        assertThrows(UserService.NotActiveUserException.class, () -> userSD.login(testUser));
        mockUser.setActive(true);

        // null user
        assertThrows(EntityNotFoundException.class,()-> userSD.login(null));

        // pad password
        mockUser.setPassword(encoder.encode("testPasswordBad"));
        assertThrows(EntityNotFoundException.class,()-> userSD.login(testUser));
    }
}
