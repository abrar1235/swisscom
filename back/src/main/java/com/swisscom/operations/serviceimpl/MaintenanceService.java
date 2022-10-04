package com.swisscom.operations.serviceimpl;

import com.swisscom.operations.entity.MaintenanceDTO;
import com.swisscom.operations.exceptions.IllegalDateException;
import com.swisscom.operations.model.*;
import com.swisscom.operations.repository.IMaintenanceRepository;
import com.swisscom.operations.repository.IUserRepository;
import com.swisscom.operations.service.IMaintenanceService;
import com.swisscom.operations.util.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.swisscom.operations.constant.MaintenanceModelGen.*;
import static com.swisscom.operations.mapper.MaintenanceMapper.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MaintenanceService implements IMaintenanceService {

    private static final List<String> ALL_COLUMNS = Arrays.asList(ID, DESCRIPTION, START_TIME, END_TIME, TIME_ZONE, USER_ID);
    private final IMaintenanceRepository maintenanceRepository;
    private final AppUtil appUtil;
    private final IUserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public Result<Maintenance, Failure> addMaintenance(Maintenance maintenance) {
        try {
            log.debug("adding new maintenance job with optional description {}", maintenance.getDescription());
            MaintenanceDTO toBeSaved = parseFromPOJO(maintenance);
            if (!userRepository.isUserExist(maintenance.getUserId()))
                throw new IllegalArgumentException("User Invalid/Not Found");
            if (toBeSaved.getStartTime().equals(toBeSaved.getEndTime()) || toBeSaved.getEndTime().before(toBeSaved.getStartTime()))
                throw new IllegalDateException("End Time cannot be before Start time");
            List<MaintenanceDTO> scheduledTimes = maintenanceRepository.maintenanceTimes(maintenance.getStartTime(), maintenance.getEndTime())
                    .stream().map(x -> MaintenanceDTO.builder().startTime(x.get(0, Date.class)).endTime(x.get(1, Date.class)).build())
                    .toList();
            appUtil.dateCheck(toBeSaved.getStartTime(), toBeSaved.getEndTime(), scheduledTimes);
            toBeSaved = maintenanceRepository.addMaintenance(toBeSaved);
            log.debug("maintenance scheduled added {}", toBeSaved.getId());
            Maintenance added = parseFromDTO(toBeSaved);
            messagingTemplate.convertAndSend("/topic/maintenance", added);
            return Result.success(added);
        } catch (Exception e) {
            log.error("an error occurred while adding maintenance", e);
            return Result.failure(new Failure(e.getMessage()));
        }
    }

    @Override
    public Result<UpdateResponse, Failure> updateMaintenance(Map<String, Object> maintenance) {
        try {
            log.debug("updating maintenance schedule {}", maintenance.get(ID));
            int updates = maintenanceRepository.updateMaintenance(maintenance);
            log.debug("Maintenance Scheduled updated {}, total updates {}", updates > 0, updates);
            return Result.success(new UpdateResponse(updates));
        } catch (Exception e) {
            log.error("an error occurred while updating maintenance schedule", e);
            return Result.failure(new Failure(e.getMessage()));
        }
    }

    @Override
    public Result<DeleteResponse, Failure> removeMaintenance(String maintenanceId) {
        try {
            log.debug("deleting scheduled maintenance with id {}", maintenanceId);
            int deletes = maintenanceRepository.removeMaintenance(maintenanceId);
            log.debug("Scheduled deleted {}, total deletes {}", deletes > 0, deletes);
            return Result.success(new DeleteResponse(deletes));
        } catch (Exception e) {
            log.error("an error occurred while deleting maintenance schedule", e);
            return Result.failure(new Failure(e.getMessage()));
        }
    }

    @Override
    public Result<List<Maintenance>, Failure> getMaintenanceList(int index) {
        try {
            log.debug("Fetching Scheduled Maintenance with start index {}", index);
            List<Tuple> maintenances = maintenanceRepository.getMaintenanceList(index, ALL_COLUMNS);
            log.debug("Maintenance List Fetched, found {} records", maintenances.size());
            return Result.success(parseFromTupleList(maintenances, ALL_COLUMNS));
        } catch (Exception e) {
            log.error("an error occurred while fetching scheduled maintenance", e);
            return Result.failure(new Failure(e.getMessage()));
        }
    }

    @Override
    public Result<Maintenance, Failure> getMaintenanceById(String maintenanceId) {
        try {
            log.debug("Fetching Maintenance Details with id {}", maintenanceId);
            Tuple tuple = maintenanceRepository.getMaintenanceById(maintenanceId, ALL_COLUMNS);
            log.debug("Maintenance Details fetched");
            return Result.success(parseFromTuple(tuple, ALL_COLUMNS));
        } catch (Exception e) {
            log.error("an error occurred while fetching maintenance scheduled", e);
            return Result.failure(new Failure(e.getMessage()));
        }
    }
}
