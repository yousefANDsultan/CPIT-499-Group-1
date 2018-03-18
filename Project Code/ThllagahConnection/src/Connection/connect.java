
package Connection;

import java.sql.*;


public class connect {
    private final String userName = "root";
    private final String password = "";
    private final String conn_string = "jdbc:mysql://localhost:3306/fridge";
    
    public Connection connect (){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(conn_string, userName, password);
            System.out.println("connected");
        }catch(SQLException e){
            System.err.println(e);
        }
        return conn;
    }
}
