package com.swisscom.operations.respositoryimpl;

import com.swisscom.operations.entity.MaintenanceDTO;
import com.swisscom.operations.repositoryimpl.MaintenanceRepository;
import com.swisscom.operations.util.AppUtil;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class MaintenanceRepositoryImplTest {

    @InjectMocks
    MaintenanceRepository subject;

    @Mock
    EntityManager entityManager;

    @Mock
    CriteriaBuilder builder;

    @Mock
    TypedQuery typedQuery;

    MaintenanceDTO input;

    @Mock
    AppUtil appUtil;


    @BeforeEach
    void init() {
        input = MaintenanceDTO.builder().startTime(new Date()).endTime(new Date()).build();
    }

    @Test
    void addMaintenanceTest() {
        doNothing().when(entityManager).persist(any());
        assertNotNull(subject.addMaintenance(input));
    }

    @Test
    void removeMaintenanceTest() {
        CriteriaDelete delete = mock(CriteriaDelete.class);
        Root root = mock(Root.class);
        when(entityManager.getCriteriaBuilder())
                .thenReturn(builder);
        when(builder.createCriteriaDelete(any())).thenReturn(delete);
        when(delete.from(MaintenanceDTO.class)).thenReturn(root);
        when(entityManager.createQuery(any(CriteriaDelete.class))).thenReturn(typedQuery);
        when(typedQuery.executeUpdate()).thenReturn(1);
        assertEquals(1, subject.removeMaintenance(UUID.randomUUID().toString()));
    }

    @Test
    void getMaintenanceByIdTest() {
        CriteriaQuery query = mock(CriteriaQuery.class);
        Root root = mock(Root.class);
        Selection selection = mock(Selection.class);
        Tuple tuple = mock(Tuple.class);
        when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        when(builder.createTupleQuery()).thenReturn(query);
        when(query.from(MaintenanceDTO.class)).thenReturn(root);
        when(appUtil.parseColumns(anyList(), any())).thenReturn(List.of(selection));
        when(query.multiselect(anyList())).thenReturn(query);
        when(query.where(any(Expression.class))).thenReturn(query);
        when(entityManager.createQuery(any(CriteriaQuery.class))).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(tuple);
        assertNotNull(subject.getMaintenanceById(UUID.randomUUID().toString(), List.of("id")));
    }

    @ParameterizedTest
    @CsvSource({
            "2022-01-01 01:00:00.00, 2022-01-01 02:00:00.00",
            "2022-01-01 01:00:00.00, 2022-01-01 01:00:00.00"
    })
    void maintenanceTimesTest(String startDate, String endDate) throws Exception {
        CriteriaQuery query = mock(CriteriaQuery.class);
        Root root = mock(Root.class);
        Tuple tuple = mock(Tuple.class);
        when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        when(builder.createTupleQuery()).thenReturn(query);
        when(query.from(MaintenanceDTO.class)).thenReturn(root);
        when(query.multiselect(any(), any())).thenReturn(query);
        when(query.where(any(Expression.class))).thenReturn(query);
        when(entityManager.createQuery(any(CriteriaQuery.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(tuple));
        assertNotNull(subject.maintenanceTimes(startDate, endDate));
    }
}
