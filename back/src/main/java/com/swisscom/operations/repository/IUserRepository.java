package com.swisscom.operations.repository;

import com.swisscom.operations.entity.UserDTO;
import com.swisscom.operations.model.Credentials;


import javax.persistence.Tuple;
import java.util.List;
import java.util.Map;

public interface IUserRepository {

    Tuple login(Credentials credentials, List<String> columns);
    List<Tuple> getAllUsers(int index, List<String> columns);
    int updateUser(Map<String, Object> update);
    int deleteUser(String userId);
    UserDTO addUser(UserDTO user);
    boolean isUserExist(String userId);
}
