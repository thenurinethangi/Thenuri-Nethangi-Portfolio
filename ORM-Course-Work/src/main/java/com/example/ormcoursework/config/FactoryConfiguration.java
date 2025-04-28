package com.example.ormcoursework.config;

import com.example.ormcoursework.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class FactoryConfiguration {

    private static FactoryConfiguration factoryConfiguration;
    private final SessionFactory sessionFactory;

    private FactoryConfiguration(){

        Configuration configuration = new Configuration();
        configuration.configure();
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(TherapyProgram.class);
        configuration.addAnnotatedClass(Therapist.class);
        configuration.addAnnotatedClass(Schedule.class);
        configuration.addAnnotatedClass(Patient.class);
        configuration.addAnnotatedClass(Register.class);
        configuration.addAnnotatedClass(Payment.class);
        configuration.addAnnotatedClass(TherapySession.class);
        configuration.addAnnotatedClass(TherapistProgramAssignment.class);
        configuration.addAnnotatedClass(MedicalHistory.class);

        sessionFactory = configuration.buildSessionFactory();

    }

    public static FactoryConfiguration getInstance(){

        return factoryConfiguration==null ? factoryConfiguration = new FactoryConfiguration() : factoryConfiguration;
    }

    public Session getSession(){

        return sessionFactory.openSession();
    }
}




