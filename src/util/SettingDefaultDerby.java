/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author torsello
 */
public class SettingDefaultDerby {

    public static void main(String[] args) {
        String setProperty = "CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(";
        String getProperty = "VALUES SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY(";
        String requireAuth = "'derby.connection.requireAuthentication'";

        String sqlAuthorization = "'derby.database.sqlAuthorization'";
        String defaultConnMode = "'derby.database.defaultConnectionMode'";
        String fullAccessUsers = "'derby.database.fullAccessUsers'";

        String readOnlyAccessUsers = "'derby.database.readOnlyAccessUsers'";
        String provider = "'derby.authentication.provider'";
        String propertiesOnly = "'derby.database.propertiesOnly'";

        System.out.println("Turning on authentication and SQL authorization.");
        Statement s = DbConnection.getInstance().createStatement();

                try {
            // Set requireAuthentication
            s.executeUpdate(setProperty + requireAuth + ", 'true')");
            //CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.connection.requireAuthentication','true'
            // Set sqlAuthorization
            s.executeUpdate(setProperty + sqlAuthorization + ", 'true')");

            //CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.database.sqlAuthentication','true'
            // Retrieve and display property values
            ResultSet rs = s.executeQuery(getProperty + requireAuth + ")");
            rs.next();
            System.out.println("Value of requireAuthentication is " + rs.getString(1));

            rs = s.executeQuery(getProperty + sqlAuthorization + ")");
            rs.next();
            System.out.println("Value of sqlAuthorization is " + rs.getString(1));

            // Set authentication scheme to Derby builtin
            s.executeUpdate(setProperty + provider + ", 'BUILTIN')");

            // Create some sample users
            //s.executeUpdate(setProperty + "'derby.user.homebanking', 'homebanking')");

            // Define noAccess as default connection mode
            //s.executeUpdate(setProperty + defaultConnMode + ", 'noAccess')");

            // Confirm default connection mode
            rs = s.executeQuery(getProperty + defaultConnMode + ")");
            rs.next();
            System.out.println("Value of defaultConnectionMode is " + rs.getString(1));

            // Define read-write users
            s.executeUpdate(setProperty + fullAccessUsers + ", 'homebanking')");

            // Define read-only user
            // s.executeUpdate(setProperty + readOnlyAccessUsers + ", 'guest')");
        } catch (SQLException ex) {
            Logger.getLogger(SettingDefaultDerby.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
