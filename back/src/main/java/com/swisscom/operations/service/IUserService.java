package com.swisscom.operations.service;


import com.swisscom.operations.model.*;

import java.util.List;
import java.util.Map;

public interface IUserService {

    Result<User, Failure> login(Credentials credentials);
    Result<List<User>, Failure> getAllUsers(int index);
    Result<UpdateResponse, Failure> updateUser(Map<String, Object> update);
    Result<DeleteResponse, Failure> deleteUser(String userId);
    Result<User, Failure> addUser(User user);
}
