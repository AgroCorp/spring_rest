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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringRestApplication.class)
class LogUtilTests {

    User testUser;
    @Mock
    private LogUtil logger;

    @Mock
    private final Logger log = LogManager.getLogger(getClass());

    @BeforeEach
    void SetUp() {
        doNothing().when(log).debug(anyString());
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
        doReturn(true).when(log).isDebugEnabled();
        doReturn(true).when(log).isErrorEnabled();
        doReturn(true).when(log).isWarnEnabled();
        logger.debug("test", new String[]{"test"}, new Object[]{testUser});
        logger.warn("test", new String[]{"test"}, new Object[]{testUser});
        logger.error("test", new String[]{"test"}, new Object[]{testUser});

        logger.debug(null, new String[]{"test"}, new Object[]{testUser});
        logger.debug("", new String[]{"test"}, new Object[]{testUser});
        logger.error(null, new String[]{"test"}, new Object[]{testUser});
        logger.error("", new String[]{"test"}, new Object[]{testUser});
        logger.warn(null, new String[]{"test"}, new Object[]{testUser});
        logger.warn("", new String[]{"test"}, new Object[]{testUser});

        logger.debug("test", (String[]) null, new Object[]{testUser});
        logger.debug("test", new String[]{null}, new Object[]{testUser});

        logger.debug("test", new String[]{"test"}, new Object[]{null});
        logger.debug("test", new String[]{"test"}, null);

        assertThrows(InvalidParameterException.class, () -> logger.debug("test", new String[]{"test"}, new Object[]{testUser, "test"}));
        assertThrows(InvalidParameterException.class, () -> logger.debug("test", new String[]{"test", "hello"}, new Object[]{testUser}));
        logger.debug("test", new String[]{}, new Object[]{});

        doReturn(false).when(log).isDebugEnabled();
        doReturn(false).when(log).isErrorEnabled();
        doReturn(false).when(log).isWarnEnabled();
        logger.debug("test", new String[]{"test"}, new Object[]{testUser});
        logger.error("test", new String[]{"test"}, new Object[]{testUser});
        logger.warn("test", new String[]{"test"}, new Object[]{testUser});
        logger.debug(null, new String[]{"test"}, new Object[]{testUser});
        logger.error(null, new String[]{"test"}, new Object[]{testUser});
        logger.warn(null, new String[]{"test"}, new Object[]{testUser});
        logger.debug("", new String[]{"test"}, new Object[]{testUser});
        logger.warn("", new String[]{"test"}, new Object[]{testUser});
        logger.error("", new String[]{"test"}, new Object[]{testUser});


        verify(log, times(6)).debug(anyString());
        verify(log, times(1)).warn(anyString());
        verify(log, times(1)).error(anyString());
    }
}
