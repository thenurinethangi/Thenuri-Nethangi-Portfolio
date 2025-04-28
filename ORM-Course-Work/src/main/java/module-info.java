module com.example.ormcoursework {
    requires javafx.fxml;
    requires static lombok;
    requires java.management;
    requires jbcrypt;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;
    requires org.controlsfx.controls;
    requires java.desktop;
    requires jasperreports;
//    requires org.controlsfx.controls;

    opens com.example.ormcoursework to javafx.fxml;
    opens com.example.ormcoursework.controller to javafx.fxml;
    opens com.example.ormcoursework.entity to org.hibernate.orm.core;
    opens com.example.ormcoursework.view.tm to javafx.base;
    exports com.example.ormcoursework;
}