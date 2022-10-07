package com.swisscom.operations.mapper;

import com.swisscom.operations.entity.MaintenanceDTO;
import com.swisscom.operations.model.Maintenance;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Tuple;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.swisscom.operations.constant.MaintenanceModelGen.*;

@Slf4j
public class MaintenanceMapper {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Maintenance parseFromDTO(MaintenanceDTO maintenance) {
        return Maintenance.builder()
                .id(maintenance.getId())
                .description(maintenance.getDescription())
                .startTime(formatDate(maintenance.getStartTime()))
                .endTime(formatDate(maintenance.getEndTime()))
                .timeZone(maintenance.getTimeZone())
                .userId(maintenance.getUserId())
                .build();
    }

    public static MaintenanceDTO parseFromPOJO(Maintenance maintenance) {
        return MaintenanceDTO.builder()
                .description(maintenance.getDescription())
                .startTime(parseDate(maintenance.getStartTime()))
                .endTime(parseDate(maintenance.getEndTime()))
                .timeZone(getTimeZone(parseDate(maintenance.getStartTime())))
                .userId(maintenance.getUserId())
                .build();
    }

    public static Maintenance parseFromTuple(Tuple tuple, List<String> columns) {
        return internalMapper(tuple, columns);
    }

    public static List<Maintenance> parseFromTupleList(List<Tuple> tuple, List<String> columns) {
        return tuple.stream().map(entry -> parseFromTuple(entry, columns)).toList();
    }

    private static Maintenance internalMapper(Tuple tuple, List<String> columns) {
        Maintenance maintenance = new Maintenance();
        int index = 0;
        for (String column : columns) {
            switch (column) {
                case ID -> {
                    maintenance.setId(tuple.get(index, String.class));
                    index++;
                }
                case DESCRIPTION -> {
                    maintenance.setDescription(tuple.get(index, String.class));
                    index++;
                }
                case START_TIME -> {
                    maintenance.setStartTime(formatDate(tuple.get(index, Date.class)));
                    index++;
                }
                case END_TIME -> {
                    maintenance.setEndTime(formatDate(tuple.get(index, Date.class)));
                    index++;
                }
                case TIME_ZONE -> {
                    maintenance.setTimeZone(tuple.get(index, String.class));
                    index++;
                }
                case USER_ID -> {
                    maintenance.setUserId(tuple.get(index, String.class));
                    index++;
                }
                default -> {
                    //default
                }
            }
        }
        return maintenance;
    }

    private static Date parseDate(String date) {
        try {
            return formatter.parse(date);
        } catch (Exception e) {
            log.error("an error occurred while parsing date", e);
            throw new IllegalArgumentException(String.format("Invalid Date Format: %s, expected Format: %s" , date, formatter.toPattern()));
        }
    }

    private static String formatDate(Date date) {
        try {
            return formatter.format(date);
        } catch (Exception e) {
            log.error("an error occurred while parsing date", e);
            throw new IllegalArgumentException(String.format("Invalid Date Format: %s, expected Format: %s" , date, formatter.toPattern()));
        }
    }

    private static String getTimeZone(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeZone().toZoneId().getId() + "" + calendar.getTimeZone().toZoneId().getRules().getOffset(Instant.now()).getId();
    }

    private MaintenanceMapper() {
    }
}
