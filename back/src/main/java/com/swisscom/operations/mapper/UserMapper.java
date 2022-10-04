package com.swisscom.operations.mapper;


import com.swisscom.operations.entity.UserDTO;
import com.swisscom.operations.model.User;

import javax.persistence.Tuple;
import java.util.List;

import static com.swisscom.operations.constant.UserModelGen.*;

public class UserMapper {

    public static User parseFromDTO(UserDTO user) {
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .status(user.getStatus())
                .roles(user.getRoles())
                .build();
    }

    public static UserDTO parseFromPOJO(User user) {
        return UserDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .status(user.getStatus())
                .roles(user.getRoles())
                .password(user.getPassword())
                .build();
    }

    public static User parseFromTuple(Tuple tuple, List<String> columns) {
        return internalMapper(tuple, columns);
    }

    public static List<User> parseFromTupleList(List<Tuple> tuple, List<String> columns) {
        return tuple.stream().map(x -> parseFromTuple(x, columns)).toList();
    }

    private static User internalMapper(Tuple tuple, List<String> columns) {
        User user = new User();
        int index = 0;
        for (String column : columns) {
            switch (column) {
                case ID -> {
                    user.setId(tuple.get(index, String.class));
                    index++;
                }
                case NAME -> {
                    user.setName(tuple.get(index, String.class));
                    index++;
                }
                case EMAIL -> {
                    user.setEmail(tuple.get(index, String.class));
                    index++;
                }
                case ROLES -> {
                    user.setRoles(tuple.get(index, String.class));
                    index++;
                }
                case STATUS -> {
                    user.setStatus(tuple.get(index, String.class));
                    index++;
                }
                default -> {
                    //default
                }
            }
        }
        return user;
    }

    private UserMapper() {
    }
}
