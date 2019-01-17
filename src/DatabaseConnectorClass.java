import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JOptionPane;

public class DatabaseConnectorClass {
	
	public static Connection dbConnection() {
		
		String url = "jdbc:mysql://localhost/user_info";
		Connection connection;
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			connection = DriverManager.getConnection(url, "root", "");
			
			//System.out.println("Connection Successful !");
			//JOptionPane.showMessageDialog(null, "Connection Successful !");
			
			return connection;
			
		} catch (Exception e) {
			
			JOptionPane.showMessageDialog(null, e);
			return null;
		}
		
	}

}
