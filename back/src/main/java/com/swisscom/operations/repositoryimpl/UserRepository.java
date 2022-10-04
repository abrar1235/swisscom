package com.swisscom.operations.repositoryimpl;

import com.swisscom.operations.entity.UserDTO;
import com.swisscom.operations.enums.Status;
import com.swisscom.operations.model.Credentials;
import com.swisscom.operations.repository.IUserRepository;
import com.swisscom.operations.util.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Map;

import static com.swisscom.operations.constant.UserModelGen.*;

@Repository
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserRepository implements IUserRepository {

    private final EntityManager entityManager;
    private final AppUtil appUtil;

    @Override
    public Tuple login(Credentials credentials, List<String> columns) {
        log.debug("Logging in user with email {}", credentials.getEmail());
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        Root<UserDTO> root = query.from(UserDTO.class);
        query.multiselect(appUtil.parseColumns(columns, root))
                .where(
                        builder.equal(root.get(EMAIL), credentials.getEmail()),
                        builder.equal(root.get(PASSWORD), credentials.getPassword()),
                        builder.equal(root.get(STATUS), Status.ACTIVE.toString()));
        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public List<Tuple> getAllUsers(int index, List<String> columns) {
        log.debug("Fetching all user with start index {}", index);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        Root<UserDTO> root = query.from(UserDTO.class);
        query.multiselect(appUtil.parseColumns(columns, root));
        return entityManager.createQuery(query).setFirstResult(index).setMaxResults(20).getResultList();
    }

    @Override
    public int updateUser(Map<String, Object> update) {
        log.debug("Updating user with id {}", update.get(ID));
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<UserDTO> criteriaUpdate = builder.createCriteriaUpdate(UserDTO.class);
        Root<UserDTO> root = criteriaUpdate.from(UserDTO.class);
        update.entrySet().stream().filter(x -> !x.getKey().equalsIgnoreCase(ID)).forEach(e -> criteriaUpdate.set(root.get(e.getKey()), e.getValue()));
        criteriaUpdate.where(builder.equal(root.get(ID), update.get(ID)));
        int updates = entityManager.createQuery(criteriaUpdate).executeUpdate();
        log.debug("User Updated {}, total Updates {}", updates > 0, updates);
        return updates;
    }

    @Override
    public int deleteUser(String userId) {
        log.debug("Deleting user with id {}", userId);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaDelete<UserDTO> criteriaDelete = builder.createCriteriaDelete(UserDTO.class);
        Root<UserDTO> root = criteriaDelete.from(UserDTO.class);
        criteriaDelete.where(builder.equal(root.get(ID), userId));
        int deletes = entityManager.createQuery(criteriaDelete).executeUpdate();
        log.debug("User Deleted {}, total Deletes {}", deletes > 0, deletes);
        return deletes;
    }

    @Override
    public UserDTO addUser(UserDTO user) {
        log.debug("adding new user with email {}", user.getEmail());
        entityManager.persist(user);
        log.debug("User Added with id {}", user.getId());
        return user;
    }

    @Override
    public boolean isUserExist(String userId) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<String> query = builder.createQuery(String.class);
            Root<UserDTO> root = query.from(UserDTO.class);
            query.select(root.get(ID)).where(builder.equal(root.get(ID), userId), builder.equal(root.get(STATUS), Status.ACTIVE.toString()));
            return !entityManager.createQuery(query).getSingleResult().isEmpty();
        } catch (Exception e) {
            log.error("an error occurred while checking for user", e);
            return false;
        }
    }
}
