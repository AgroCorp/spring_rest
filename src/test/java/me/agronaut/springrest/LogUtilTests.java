package me.agronaut.springrest;

import me.agronaut.springrest.model.User;
import me.agronaut.springrest.util.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.InvalidParameterException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringRestApplication.class)
class LogUtilTests {

    User testUser;
    @Mock
    private LogUtil logger;

    private Logger log = LogManager.getLogger(getClass());

    @BeforeEach
    void SetUp() {
        ReflectionTestUtils.setField(logger, "log", log);

        doCallRealMethod().when(logger).debug(anyString());
        doCallRealMethod().when(logger).warn(anyString());
        doCallRealMethod().when(logger).error(anyString());

        doCallRealMethod().when(logger).warn(nullable(String.class), nullable(String.class), nullable(Object.class));
        doCallRealMethod().when(logger).error(nullable(String.class), nullable(String.class), nullable(Object.class));
        doCallRealMethod().when(logger).debug(nullable(String.class), nullable(String.class), nullable(Object.class));

        doCallRealMethod().when(logger).warn(nullable(String.class), nullable(String[].class), nullable(Object[].class));
        doCallRealMethod().when(logger).error(nullable(String.class), nullable(String[].class), nullable(Object[].class));
        doCallRealMethod().when(logger).debug(nullable(String.class), nullable(String[].class), nullable(Object[].class));

        testUser = new User();
        testUser.setId(5L);
        testUser.setUsername("tesuser");
        testUser.setEmail("test@test.com");
        testUser.setPassword("vblansuibv67v5a8s76v");
        testUser.setFirstName("test");
        testUser.setLastName("user");
    }
    @Test
    void testConstruct() {
        assertDoesNotThrow(() -> new LogUtil(this.getClass()));
        assertDoesNotThrow(() -> new LogUtil(null));
    }

    @Test
    void testSimpleLogging() {
        logger.debug("simple debug");
        logger.warn("simple warn");
        logger.error("simple error");
        logger.debug(null);
        logger.warn(null);
        logger.error(null);

        verify(logger, times(2)).debug(nullable(String.class));
        verify(logger, times(2)).warn(nullable(String.class));
        verify(logger, times(2)).error(nullable(String.class));
    }

    @Test
    void testOneParameterLogging() {
        logger.debug("test3", null, testUser);
        logger.debug("test3", "parameter3", null);
        logger.debug(null, "parameter3", "test");
        logger.warn("test3", null, testUser);
        logger.warn("test3", "parameter3", null);
        logger.warn(null, "parameter3", "test");
        logger.error("test3", null, testUser);
        logger.error("test3", "parameter3", null);
        logger.error(null, "parameter3", "test");

        verify(logger, times(3)).debug(nullable(String.class), nullable(String.class), nullable(Object.class));
        verify(logger, times(3)).error(nullable(String.class), nullable(String.class), nullable(Object.class));
        verify(logger, times(3)).warn(nullable(String.class), nullable(String.class), nullable(Object.class));
    }

    @Test
    void testArrayParameterLogging() {
        logger.debug("test", new String[]{"hello"}, new Object[]{testUser});
        assertThrows(InvalidParameterException.class, () -> {logger.debug("test", new String[]{"hello", "test"}, new Object[]{testUser});});
        assertThrows(InvalidParameterException.class, () -> {logger.debug("test", new String[]{"hello"}, new Object[]{testUser, "test"});});
        logger.debug("test", new String[]{"hello"}, new Object[]{null});
        logger.debug("test", new String[]{null}, new Object[]{"test"});
        logger.debug(null, new String[]{"hello"}, new Object[]{"test"});
        logger.debug(null, new String[]{}, new Object[]{});
        logger.debug("test", new String[]{}, new Object[]{});

        logger.warn("test", new String[]{"hello"}, new Object[]{testUser});
        assertThrows(InvalidParameterException.class, () -> {logger.warn("test", new String[]{"hello", "test"}, new Object[]{testUser});});
        assertThrows(InvalidParameterException.class, () -> {logger.warn("test", new String[]{"hello"}, new Object[]{testUser, "test"});});
        logger.warn("test", new String[]{"hello"}, new Object[]{null});
        logger.warn("test", new String[]{null}, new Object[]{"test"});
        logger.warn(null, new String[]{"hello"}, new Object[]{"test"});
        logger.warn(null, new String[]{}, new Object[]{});
        logger.warn("test", new String[]{}, new Object[]{});

        logger.error("test", new String[]{"hello"}, new Object[]{testUser});
        assertThrows(InvalidParameterException.class, () -> {logger.error("test", new String[]{"hello", "test"}, new Object[]{testUser});});
        assertThrows(InvalidParameterException.class, () -> {logger.error("test", new String[]{"hello"}, new Object[]{testUser, "test"});});
        logger.error("test", new String[]{"hello"}, new Object[]{null});
        logger.error("test", new String[]{null}, new Object[]{"test"});
        logger.error(null, new String[]{"hello"}, new Object[]{"test"});
        logger.error(null, new String[]{}, new Object[]{});
        logger.error("test", new String[]{}, new Object[]{});

        verify(logger, times(8)).debug(nullable(String.class), nullable(String[].class), nullable(Object[].class));
        verify(logger, times(8)).warn(nullable(String.class), nullable(String[].class), nullable(Object[].class));
        verify(logger, times(8)).error(nullable(String.class), nullable(String[].class), nullable(Object[].class));
    }
}
