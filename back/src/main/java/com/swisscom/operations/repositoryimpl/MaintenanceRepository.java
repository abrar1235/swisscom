package com.swisscom.operations.repositoryimpl;

import com.swisscom.operations.entity.MaintenanceDTO;
import com.swisscom.operations.repository.IMaintenanceRepository;
import com.swisscom.operations.util.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.swisscom.operations.constant.MaintenanceModelGen.*;

@Repository
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MaintenanceRepository implements IMaintenanceRepository {
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final EntityManager entityManager;
    private final AppUtil appUtil;

    @Override
    public MaintenanceDTO addMaintenance(MaintenanceDTO maintenance) {
        log.debug("Adding new Maintenance Scheduled with start date {} and end date {}"
                , maintenance.getStartTime(), maintenance.getEndTime());
        entityManager.persist(maintenance);
        log.debug("Maintenance Added with id {}", maintenance.getId());
        return maintenance;
    }

    @Override
    public int removeMaintenance(String maintenanceId) {
        log.debug("Deleting Maintenance Scheduled with id {}", maintenanceId);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaDelete<MaintenanceDTO> delete = builder.createCriteriaDelete(MaintenanceDTO.class);
        Root<MaintenanceDTO> root = delete.from(MaintenanceDTO.class);
        delete.where(builder.equal(root.get(ID), maintenanceId));
        int deletes = entityManager.createQuery(delete).executeUpdate();
        log.debug("Resource Deleted, total Deletes {}", deletes);
        return deletes;
    }

    @Override
    public List<Tuple> getMaintenanceList(int index, List<String> columns) throws ParseException {
        log.debug("Fetching Maintenance List start index {}", index);
        Date today = new Date();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        Root<MaintenanceDTO> root = query.from(MaintenanceDTO.class);
        query.multiselect(appUtil.parseColumns(columns, root))
                .where(builder.greaterThanOrEqualTo(root.get(START_TIME), simpleDateFormat.parse(simpleDateFormat.format(today))))
                .orderBy(builder.desc(root.get(START_TIME)));
        return entityManager.createQuery(query).setFirstResult(index).setMaxResults(20).getResultList();
    }

    @Override
    public Tuple getMaintenanceById(String maintenanceId, List<String> columns) {
        log.debug("Fetching Maintenance with id {}", maintenanceId);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        Root<MaintenanceDTO> root = query.from(MaintenanceDTO.class);
        query.multiselect(appUtil.parseColumns(columns, root)).where(builder.equal(root.get(ID), maintenanceId));
        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public List<Tuple> maintenanceTimes(String startTime, String endTime) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        Date start = simpleDateFormat.parse(startTime);
        Date end = simpleDateFormat.parse(endTime);
        if (start.equals(end)) {
            calendar.setTime(end);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            end = calendar.getTime();
        }
        log.info("Fetching Maintenance Start Date with startTime {} and endTime {}", start, end);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        Root<MaintenanceDTO> root = query.from(MaintenanceDTO.class);
        query.multiselect(root.get(START_TIME), root.get(END_TIME))
                .where(builder.greaterThanOrEqualTo(root.get(START_TIME), start), builder.lessThanOrEqualTo(root.get(END_TIME), end));
        return entityManager.createQuery(query).getResultList();
    }
}