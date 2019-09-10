/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

module HomeBanking {
    requires javax.activation;
    requires java.mail;
    requires mysql.connector.java;
    requires javafx.swt;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.web;
    requires java.sql;
    opens controller to javafx.fxml;
    opens model.entities to javafx.base;
    
    exports homebanking;
}
