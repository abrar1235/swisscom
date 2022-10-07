package com.swisscom.operations.repository;

import com.swisscom.operations.entity.MaintenanceDTO;

import javax.persistence.Tuple;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface IMaintenanceRepository {

    MaintenanceDTO addMaintenance(MaintenanceDTO maintenance);
    int removeMaintenance(String maintenanceId);
    List<Tuple> getMaintenanceList(int index, List<String> columns) throws ParseException;
    Tuple getMaintenanceById(String maintenanceId,List<String> columns);
    List<Tuple> maintenanceTimes(String startTime, String endTime) throws ParseException;
}
