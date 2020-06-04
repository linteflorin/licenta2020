package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import webcrawler.WebCrawler;

public class DatabaseManager {
	
	private static DatabaseManager single_instance = null;
	
	private DatabaseManager() {
		
	}
	
	public static DatabaseManager getInstance() {
		if(single_instance == null) {
			single_instance = new DatabaseManager();
		}
		return single_instance;
	}
	
	public  void checkForConnection() {
		 try {
		        Class.forName("org.postgresql.Driver");
		    } catch (ClassNotFoundException e1) {
		        e1.printStackTrace();
		    }

		    //String url = "jdbc:postgresql://webclone.dcae.pub.ro/phppgadmin:5432/";
//		    String url = "jdbc:postgresql://localhost:5432/dcae";
//		    String user = "postgres";
//		    String pass = "user123;";
		    
		 String url = "jdbc:postgresql://webclone.dcae.pub.ro:5432/dcae";
		    Properties props = new Properties();
		    props.setProperty("user","postgres");
		    props.setProperty("password","user123;");
		    
		    try {
				Connection conn = DriverManager.getConnection(url, props);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		    try {
//		        db = DriverManager.getConnection(url,user,pass);
//		    } catch (SQLException e1) {
//		        e1.printStackTrace();
//		    }
		
	}
}
