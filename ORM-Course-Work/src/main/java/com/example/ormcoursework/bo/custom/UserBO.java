package com.example.ormcoursework.bo.custom;

import com.example.ormcoursework.bo.SuperBO;
import com.example.ormcoursework.dto.UserDTO;

public interface UserBO extends SuperBO {

    boolean add(UserDTO userDTO);

    boolean isExist(UserDTO userDTO);

    UserDTO search(String userName);

    boolean updatePassword(UserDTO userDTO1);

    boolean changeUserDetailsOne(String[] data, String[] column);

    boolean changeUserDetailsTwo(String[] data, String[] column);

    boolean changeUserDetailsThree(String[] data, String[] column);

//    boolean changeUserDetailsFour(String[] data, String[] column);
}
