package com.swisscom.operations.serviceimpl;

import com.swisscom.operations.entity.MaintenanceDTO;
import com.swisscom.operations.model.Maintenance;
import com.swisscom.operations.repository.IMaintenanceRepository;
import com.swisscom.operations.repository.IUserRepository;
import com.swisscom.operations.util.AppUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.concurrent.ListenableFuture;

import javax.persistence.Tuple;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class MaintenanceServiceTest {

    @InjectMocks
    MaintenanceService subject;

    @Mock
    IMaintenanceRepository maintenanceRepository;

    @Mock
    IUserRepository userRepository;

    @Mock
    AppUtil appUtil;

    @Mock
    SimpMessagingTemplate messagingTemplate;

    @Mock
    KafkaTemplate kafkaTemplate;

    Maintenance input;

    MaintenanceDTO output;

    @BeforeEach
    void init() {
        input = Maintenance.builder()
                .userId(UUID.randomUUID().toString())
                .description("test")
                .build();

        output = MaintenanceDTO.builder().id(UUID.randomUUID().toString()).startTime(new Date()).endTime(new Date()).userId(UUID.randomUUID().toString()).description("Test").build();
    }

    @ParameterizedTest
    @CsvSource({
            "true, 2022-01-01 01:00:00.00, 2022-01-01 02:00:00.00",
            "false, 2022-01-01 01:00:00.00, 2022-01-01 02:00:00.00",
            "true, 2022-01-01 01:00:00.00, 2022-01-01 01:00:00.00",
            "true, 2022-01-01 02:00:00.00, 2022-01-01 01:00:00.00"
    })
    void addMaintenanceTest(boolean userExist, String startTime, String endTime) throws Exception {
        input.setStartTime(startTime);
        input.setEndTime(endTime);
        ListenableFuture future = mock(ListenableFuture.class);
        when(userRepository.isUserExist(anyString())).thenReturn(userExist);
        doNothing().when(appUtil).dateCheck(any(), any(), anyList());
        when(maintenanceRepository.addMaintenance(any())).thenReturn(output);
        doNothing().when(messagingTemplate).convertAndSend(anyString(), any(Object.class));
        when(kafkaTemplate.send(anyString(), any())).thenReturn(future);
        assertNotNull(subject.addMaintenance(input));
    }

    @Test
    void removeMaintenanceTest(){
        when(maintenanceRepository.removeMaintenance(anyString())).thenReturn(1);
        doNothing().when(messagingTemplate).convertAndSend(anyString(), any(Object.class));
        assertNotNull(subject.removeMaintenance(UUID.randomUUID().toString()));
    }

    @Test
    void getMaintenanceListTest() throws Exception{
        Tuple tuple = mock(Tuple.class);
        when(maintenanceRepository.getMaintenanceList(anyInt(), anyList())).thenReturn(List.of(tuple));
        when(tuple.get(anyInt(), any())).thenReturn("test","test", new Date(), new Date(), "test", "test");
        assertNotNull(subject.getMaintenanceList(0));
    }

    @Test
    void getMaintenanceByIdTest(){
        Tuple tuple = mock(Tuple.class);
        when(maintenanceRepository.getMaintenanceById(anyString(), anyList())).thenReturn(tuple);
        when(tuple.get(anyInt(), any())).thenReturn("test","test", new Date(), new Date(), "test", "test");
        assertNotNull(subject.getMaintenanceById(UUID.randomUUID().toString()));
    }

    @Test
    void removeMaintenanceExceptionTest(){
        when(maintenanceRepository.removeMaintenance(anyString())).thenThrow(new RuntimeException("test"));
        assertNotNull(subject.removeMaintenance(UUID.randomUUID().toString()));
    }

    @Test
    void getMaintenanceListExceptionTest() throws Exception{
        when(maintenanceRepository.getMaintenanceList(anyInt(), anyList())).thenThrow(new RuntimeException("test"));
        assertNotNull(subject.getMaintenanceList(0));
    }

    @Test
    void getMaintenanceByIdExceptionTest(){
        when(maintenanceRepository.getMaintenanceById(anyString(), anyList())).thenThrow(new RuntimeException("test"));
        assertNotNull(subject.getMaintenanceById(UUID.randomUUID().toString()));
    }
}
