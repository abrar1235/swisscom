package com.swisscom.operations.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "maintenance")
public class MaintenanceDTO implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID-Generator")
    @GenericGenerator(name = "UUID-Generator", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "description")
    private String description;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "time_zone")
    private String timeZone;

    @Column(name = "user_id")
    private String userId;
}