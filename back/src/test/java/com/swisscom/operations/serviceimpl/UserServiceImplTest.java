package com.swisscom.operations.serviceimpl;

import com.swisscom.operations.entity.UserDTO;
import com.swisscom.operations.model.Credentials;
import com.swisscom.operations.model.User;
import com.swisscom.operations.repository.IUserRepository;
import com.swisscom.operations.util.AppUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.Tuple;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserService subject;

    @Mock
    IUserRepository userRepository;

    @Mock
    AppUtil appUtil;

    @Test
    void loginTest() throws Exception {
        Credentials credentials = Credentials.builder().email("test@test.com").password("12345678").build();
        Tuple tuple = mock(Tuple.class);
        when(appUtil.encryptPassword(anyString())).thenReturn(UUID.randomUUID().toString());
        when(userRepository.login(any(), anyList())).thenReturn(tuple);
        when(appUtil.generateToken(anyString(), anyString())).thenReturn(UUID.randomUUID().toString());
        when(tuple.get(anyInt(), any())).thenReturn("test");
        assertNotNull(subject.login(credentials));
    }

    @Test
    void loginExceptionTest() throws Exception {
        Credentials credentials = Credentials.builder().email("test@test.com").password("12345678").build();
        when(appUtil.encryptPassword(anyString())).thenThrow(new RuntimeException("Test"));
        assertNotNull(subject.login(credentials));
    }

    @Test
    void getAllUsersTest() {
        Tuple tuple = mock(Tuple.class);
        when(userRepository.getAllUsers(anyInt(), anyList())).thenReturn(List.of(tuple));
        when(tuple.get(anyInt(), any())).thenReturn("test");
        assertNotNull(subject.getAllUsers(0));
    }

    @Test
    void getAllUsersExceptionTest() {
        Tuple tuple = mock(Tuple.class);
        when(userRepository.getAllUsers(anyInt(), anyList())).thenThrow(new RuntimeException("test"));
        when(tuple.get(anyInt(), any())).thenReturn("test");
        assertNotNull(subject.getAllUsers(0));
    }

    @ParameterizedTest
    @CsvSource({
            "userId, test",
            "password, 123456"
    })
    void updateUserTest(String key, Object value) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("id", UUID.randomUUID().toString());
        updates.put(key, value);
        when(userRepository.updateUser(anyMap())).thenReturn(1);
        assertNotNull(subject.updateUser(updates));
    }

    @Test
    void deleteUserTest() {
        when(userRepository.deleteUser(anyString())).thenReturn(1);
        assertNotNull(subject.deleteUser(UUID.randomUUID().toString()));
    }

    @Test
    void deleteUserExceptionTest() {
        when(userRepository.deleteUser(anyString())).thenThrow(new RuntimeException("test"));
        assertNotNull(subject.deleteUser(UUID.randomUUID().toString()));
    }

    @Test
    void addUserTest() throws Exception {
        User user = User.builder().name("test").email("test@test.com").password("12345678").roles("ADMIN").build();
        UserDTO userDTO = UserDTO.builder().id(UUID.randomUUID().toString()).name("test").email("test@test.com").password("12345678").roles("ADMIN").build();
        doNothing().when(appUtil).valueCheck(anyList(), anyString(), any());
        when(userRepository.addUser(any())).thenReturn(userDTO);
        assertNotNull(subject.addUser(user));
    }

    @Test
    void addUserExceptionTest() throws Exception {
        User user = User.builder().name("test").email("test@test.com").password("12345678").roles("ADMIN").build();
        doThrow(new RuntimeException("test")).when(appUtil).valueCheck(anyList(), anyString(), any());
        assertNotNull(subject.addUser(user));
    }
}
