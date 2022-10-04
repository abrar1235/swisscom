package com.swisscom.operations.controller;

import com.swisscom.operations.model.*;
import com.swisscom.operations.service.IMaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    private final IMaintenanceService maintenanceService;

    @GetMapping("/getMaintenanceList")
    public Result<List<Maintenance>, Failure> getMaintenanceList(@RequestParam int index){
        return maintenanceService.getMaintenanceList(index);
    }

    @GetMapping("/getMaintenanceById")
    public Result<Maintenance, Failure> getMaintenanceById(@RequestParam String maintenanceId){
        return maintenanceService.getMaintenanceById(maintenanceId);
    }

    @PostMapping("/addMaintenance")
    public Result<Maintenance, Failure> addMaintenance(@RequestBody @Valid Maintenance maintenance){
        return maintenanceService.addMaintenance(maintenance);
    }

    @PutMapping("/updateMaintenance")
    public Result<UpdateResponse, Failure> updateMaintenance(@RequestBody Map<String, Object> updates){
        return maintenanceService.updateMaintenance(updates);
    }

    @DeleteMapping("/deleteMaintenance")
    public Result<DeleteResponse, Failure> deleteMaintenance(@RequestParam String maintenanceId){
        return maintenanceService.removeMaintenance(maintenanceId);
    }
}
