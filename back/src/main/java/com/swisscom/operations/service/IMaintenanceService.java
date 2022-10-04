package com.swisscom.operations.service;

import com.swisscom.operations.model.*;

import java.util.List;
import java.util.Map;

public interface IMaintenanceService {

    Result<Maintenance, Failure> addMaintenance(Maintenance maintenance);
    Result<UpdateResponse, Failure> updateMaintenance(Map<String, Object> maintenance);
    Result<DeleteResponse, Failure> removeMaintenance(String maintenanceId);
    Result<List<Maintenance>, Failure> getMaintenanceList(int index);
    Result<Maintenance, Failure> getMaintenanceById(String maintenanceId);
}
