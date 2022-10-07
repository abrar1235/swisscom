package com.swisscom.operations.respositoryimpl;

import com.swisscom.operations.entity.UserDTO;
import com.swisscom.operations.model.Credentials;
import com.swisscom.operations.repositoryimpl.UserRepository;
import com.swisscom.operations.util.AppUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserRespositoryImplTest {

    @InjectMocks
    UserRepository subject;

    @Mock
    EntityManager entityManager;

    @Mock
    CriteriaBuilder builder;

    @Mock
    TypedQuery typedQuery;

    @Mock
    AppUtil appUtil;

    @Test
    void loginTests(){
        Credentials credentials = Credentials.builder().email("test@test.com").password("12345678").build();
        CriteriaQuery query = mock(CriteriaQuery.class);
        Root root = mock(Root.class);
        Tuple tuple = mock(Tuple.class);
        Expression expression = mock(Expression.class);
        when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        when(builder.createTupleQuery()).thenReturn(query);
        when(query.from(UserDTO.class)).thenReturn(root);
        when(query.multiselect(anyList())).thenReturn(query);
        when(query.where(any(Expression.class))).thenReturn(query);
        when(appUtil.parseColumns(anyList(), any(Root.class))).thenReturn(List.of(expression));
        when(entityManager.createQuery(any(CriteriaQuery.class))).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(tuple);
        assertNotNull(subject.login(credentials, List.of("id")));
    }

    @Test
    void getAllUsersTest(){
        CriteriaQuery query = mock(CriteriaQuery.class);
        Root root = mock(Root.class);
        Tuple tuple = mock(Tuple.class);
        Expression expression = mock(Expression.class);
        when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        when(builder.createTupleQuery()).thenReturn(query);
        when(query.from(UserDTO.class)).thenReturn(root);
        when(query.multiselect(anyList())).thenReturn(query);
        when(appUtil.parseColumns(anyList(), any(Root.class))).thenReturn(List.of(expression));
        when(entityManager.createQuery(any(CriteriaQuery.class))).thenReturn(typedQuery);
        when(typedQuery.setFirstResult(anyInt())).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(anyInt())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(tuple));
        assertNotNull(subject.getAllUsers(0, List.of("id")));
    }

    @Test
    void updateUserTest(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", UUID.randomUUID());
        map.put("email", "test@test.com");
        CriteriaUpdate update = mock(CriteriaUpdate.class);
        Root root = mock(Root.class);
        when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        when(builder.createCriteriaUpdate(any())).thenReturn(update);
        when(update.from(UserDTO.class)).thenReturn(root);
        when(update.where(any(Expression.class))).thenReturn(update);
        when(entityManager.createQuery(any(CriteriaUpdate.class))).thenReturn(typedQuery);
        when(typedQuery.executeUpdate()).thenReturn(1);
        assertEquals(1, subject.updateUser(map));
    }

    @Test
    void deleteUserTest(){
        CriteriaDelete delete = mock(CriteriaDelete.class);
        Root root = mock(Root.class);
        when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        when(builder.createCriteriaDelete(any())).thenReturn(delete);
        when(delete.from(UserDTO.class)).thenReturn(root);
        when(delete.where(any(Expression.class))).thenReturn(delete);
        when(entityManager.createQuery(any(CriteriaDelete.class))).thenReturn(typedQuery);
        when(typedQuery.executeUpdate()).thenReturn(1);
        assertEquals(1, subject.deleteUser(UUID.randomUUID().toString()));
    }

    @Test
    void addUserTest(){
        UserDTO user = UserDTO.builder().email("test@test.com").name("Test").id(UUID.randomUUID().toString()).build();
        doNothing().when(entityManager).persist(any());
        assertNotNull(subject.addUser(user));
    }

    @ParameterizedTest
    @CsvSource({
            "123456, true",
            " , false",
            "'', false"
    })
    void isUserExistTest(String output, boolean answer){
        CriteriaQuery query = mock(CriteriaQuery.class);
        Root root = mock(Root.class);
        when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        when(builder.createQuery(any())).thenReturn(query);
        when(query.from(UserDTO.class)).thenReturn(root);
        when(query.select(any())).thenReturn(query);
        when(entityManager.createQuery(any(CriteriaQuery.class))).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(output);
        assertEquals(answer, subject.isUserExist(UUID.randomUUID().toString()));
    }
}
