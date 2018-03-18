package Connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

public class DBconnection {

    public static void main(String[] args) throws SQLException, FileNotFoundException {
        Connection conn = null;
        connect DBconfig = new connect();
        conn = DBconfig.connect();

        Statement stmt = null;
        ResultSet rs = null;
        
        File in = new File("output.txt");
        Scanner pen = new Scanner(in);
        while(true){
            while (pen.hasNext()) {
               String x = pen.nextLine();
               stmt = conn.createStatement();
               String insert = "INSERT INTO items values (" + "'" + x + "'" + ", 'apple')";
               stmt.execute(insert);
            }
        }
        /*
        try {
            
            stmt = conn.createStatement();
            String sss = "INSERT INTO items values (123456, 'apple')";
            stmt.execute(sss);
            
    // Now do something with the ResultSet ....
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
    // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore

                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore

                stmt = null;
            }
        }*/
    }

}
