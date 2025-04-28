package com.example.ormcoursework.dao.custom.impl;

import com.example.ormcoursework.config.FactoryConfiguration;
import com.example.ormcoursework.dao.custom.UserDAO;
import com.example.ormcoursework.entity.User;
import com.example.ormcoursework.util.LogInData;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class UserDAOImpl implements UserDAO {


    @Override
    public boolean add(User user) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {

            session.persist(user);
            transaction.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            return false;
        }
        finally {
            session.close();
        }
    }


    @Override
    public boolean isExist(User user) {

        Session session = FactoryConfiguration.getInstance().getSession();

        try {

            User u = session.get(User.class,user.getUserName());
            return u != null;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            session.close();
        }
    }

    @Override
    public User search(String userName) {

        Session session = FactoryConfiguration.getInstance().getSession();

        try{

            User user = session.get(User.class,userName);
            return user;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            session.close();
        }
    }


    @Override
    public boolean updatePassword(User user) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            User u = session.get(User.class,user.getUserName());

            if(u==null){
                return false;
            }

            u.setPassword(user.getPassword());
            transaction.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            session.close();
        }
    }


    @Override
    public boolean changeUserDetailsOne(String[] data, String[] column) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            User user = session.get(User.class, LogInData.getUserName());

            if(user==null){
                return false;
            }

            if(column[0].equals("userName")){
                user.setUserName(data[0]);
            }
            else if(column[0].equals("name")){
                user.setFullName(data[0]);
            }
            else if(column[0].equals("email")){
                user.setEmail(data[0]);
            }

            transaction.commit();
            return true;
            //"userName" "name" "email"

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            session.close();
        }
    }


    @Override
    public boolean changeUserDetailsTwo(String[] data, String[] column) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            Query query = session.createNativeQuery(
                    "UPDATE user SET "+column[0] +" = ?, "+ column[1] +" = ? WHERE userName = ?"
            );
            query.setParameter(1, data[0]);
            query.setParameter(2, data[1]);
            query.setParameter(3, LogInData.getUserName());

            int updatedRows = query.executeUpdate();

            if(updatedRows>0){
                return true;
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            session.close();
        }

    }


    @Override
    public boolean changeUserDetailsThree(String[] data, String[] column) {

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try{
            Query query = session.createNativeQuery(
                    "UPDATE user SET " + column[0] + " = ?, " + column[1] + " = ?, " + column[2] + " = ? WHERE userName = ?"
            );
            query.setParameter(1, data[0]);
            query.setParameter(2, data[1]);
            query.setParameter(3, data[2]);
            query.setParameter(4, LogInData.getUserName());

            int updatedRows = query.executeUpdate();

            if(updatedRows>0){
                return true;
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            session.close();
        }
    }


//    @Override
//    public String changeUserDetailsFour(String[] data, String[] column) {
//
//        String sql = "UPDATE user SET "+column[0] +" = ?, "+ column[1] +" = ?, "+ column[2]+" = ?, "+column[3]+" =? WHERE userName = ?";
//        boolean result = SQLUtil.execute(sql,data[0],data[1],data[2],data[3],userName);
//
//        if(result){
//            return "Successfully updated your profile";
//        }
//        else{
//            return "something went wrong, failed to update your profile, try again later";
//        }
//    }
}





