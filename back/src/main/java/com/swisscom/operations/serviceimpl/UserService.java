package com.swisscom.operations.serviceimpl;

import com.swisscom.operations.entity.UserDTO;
import com.swisscom.operations.enums.Roles;
import com.swisscom.operations.enums.Status;
import com.swisscom.operations.exceptions.IllegalRolesException;
import com.swisscom.operations.model.*;
import com.swisscom.operations.repository.IUserRepository;
import com.swisscom.operations.service.IUserService;
import com.swisscom.operations.util.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.swisscom.operations.constant.UserModelGen.*;
import static com.swisscom.operations.mapper.UserMapper.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements IUserService {

    private static final List<String> ALL_COLUMN = Arrays.asList(ID, NAME, EMAIL, STATUS, ROLES);

    private final IUserRepository userRepository;
    private final AppUtil appUtil;

    @Override
    public Result<User, Failure> login(Credentials credentials) {
        try {
            log.debug("logging user with email {}", credentials.getEmail());
            credentials.setPassword(appUtil.encryptPassword(credentials.getPassword()));
            Tuple user = userRepository.login(credentials, ALL_COLUMN);
            log.debug("User LoggedIn Successfully with id {}", user.get(0));
            User response = parseFromTuple(user, ALL_COLUMN);
            response.setToken(appUtil.generateToken(response.getId(), response.getRoles()));
            return Result.success(response);
        } catch (Exception e) {
            log.error("an error occurred while logging user", e);
            return Result.failure(new Failure(e.getMessage()));
        }
    }

    @Override
    public Result<List<User>, Failure> getAllUsers(int index) {
        try {
            log.debug("Fetching all user with start index {}", index);
            List<Tuple> users = userRepository.getAllUsers(index, ALL_COLUMN);
            log.debug("Fetched {} users", users.size());
            return Result.success(parseFromTupleList(users, ALL_COLUMN));
        } catch (Exception e) {
            log.error("an error occurred while fething all users", e);
            return Result.failure(new Failure(e.getMessage()));
        }
    }

    @Override
    public Result<UpdateResponse, Failure> updateUser(Map<String, Object> update) {
        try {
            log.debug("Updating user with id {}", update.get(ID));
            update.computeIfPresent(PASSWORD, (k, v) -> {
                throw new IllegalStateException("Invalid Key Passed => { Password }");
            });
            int updates = userRepository.updateUser(update);
            log.debug("User Updated {}, total updates {}", updates > 0, updates);
            return Result.success(new UpdateResponse(updates));
        } catch (Exception e) {
            log.error("an error occurred while updating user", e);
            return Result.failure(new Failure(e.getMessage()));
        }
    }

    @Override
    public Result<DeleteResponse, Failure> deleteUser(String userId) {
        try {
            log.debug("Deleting user with id {}", userId);
            int deletes = userRepository.deleteUser(userId);
            log.debug("User Deleted {}, total Deletes {}", deletes > 0, deletes);
            return Result.success(new DeleteResponse(deletes));
        } catch (Exception e) {
            log.error("an error occurred while deleting user", e);
            return Result.failure(new Failure(e.getMessage()));
        }
    }

    @Override
    public Result<User, Failure> addUser(User user) {
        try {
            log.debug("Adding new user with email {}", user.getEmail());
            appUtil.valueCheck(Roles.asList(), user.getRoles(), () -> new IllegalRolesException("Invalid Role Passed Valid Values are => " + Roles.asList()));
            user.setPassword(appUtil.encryptPassword(user.getPassword()));
            user.setStatus(Status.ACTIVE.toString());
            UserDTO added = userRepository.addUser(parseFromPOJO(user));
            log.debug("User Added Successfully with id {}", added.getId());
            return Result.success(parseFromDTO(added));
        } catch (Exception e) {
            log.error("an error occurred while adding user", e);
            return Result.failure(new Failure(e));
        }
    }
}
