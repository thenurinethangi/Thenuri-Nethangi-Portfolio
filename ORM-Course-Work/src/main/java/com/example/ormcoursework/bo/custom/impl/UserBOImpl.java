package com.example.ormcoursework.bo.custom.impl;

import com.example.ormcoursework.bo.custom.UserBO;
import com.example.ormcoursework.dao.DAOFactory;
import com.example.ormcoursework.dao.custom.UserDAO;
import com.example.ormcoursework.dto.UserDTO;
import com.example.ormcoursework.entity.User;
import com.example.ormcoursework.util.DTOToEntity;
import com.example.ormcoursework.util.EntityToDTO;

public class UserBOImpl implements UserBO {

    private final UserDAO userDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.USER);

    @Override
    public boolean add(UserDTO userDTO) {

        return userDAO.add(DTOToEntity.ToUserEntity(userDTO));
    }

    @Override
    public boolean isExist(UserDTO userDTO) {

        return userDAO.isExist(DTOToEntity.ToUserEntity(userDTO));
    }

    @Override
    public UserDTO search(String userName) {

        User user = userDAO.search(userName);

        if(user==null){
            return null;
        }
        else{
            return EntityToDTO.ToUserDTO(user);
        }
    }

    @Override
    public boolean updatePassword(UserDTO userDTO1) {

        return userDAO.updatePassword(DTOToEntity.ToUserEntity(userDTO1));
    }

    @Override
    public boolean changeUserDetailsOne(String[] data, String[] column) {

        return userDAO.changeUserDetailsOne(data,column);
    }

    @Override
    public boolean changeUserDetailsTwo(String[] data, String[] column) {

        return userDAO.changeUserDetailsTwo(data,column);
    }

    @Override
    public boolean changeUserDetailsThree(String[] data, String[] column) {

        return userDAO.changeUserDetailsThree(data,column);
    }

}
