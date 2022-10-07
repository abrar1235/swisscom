package com.swisscom.operations.controller;

import com.swisscom.operations.model.DeleteResponse;
import com.swisscom.operations.model.Maintenance;
import com.swisscom.operations.model.Result;
import com.swisscom.operations.service.IMaintenanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class MaintenanceControllerTest {

    @InjectMocks
    MaintenanceController subject;

    @Mock
    IMaintenanceService maintenanceService;

    Maintenance input;

    Maintenance output;

    @BeforeEach
    void init() {
        input = Maintenance.builder().build();
        output = Maintenance.builder().id(UUID.randomUUID().toString()).build();
    }

    @Test
    void getMaintenanceListTest() {
        when(maintenanceService.getMaintenanceList(anyInt()))
                .thenReturn(Result.success(List.of(output)));
        assertNotNull(subject.getMaintenanceList(0));
    }

    @Test
    void getMaintenanceByIdTest(){
        when(maintenanceService.getMaintenanceById(anyString()))
                .thenReturn(Result.success(output));
        assertNotNull(subject.getMaintenanceById(UUID.randomUUID().toString()));
    }

    @Test
    void addMaintenanceTest(){
        when(maintenanceService.addMaintenance(any()))
                .thenReturn(Result.success(output));
        assertNotNull(subject.addMaintenance(input));
    }

    @Test
    void deleteMaintenanceTest(){
        when(maintenanceService.removeMaintenance(anyString()))
                .thenReturn(Result.success(new DeleteResponse(1)));
        assertNotNull(subject.deleteMaintenance(UUID.randomUUID().toString()));
    }
}
