package com.mayab.quality.app;
import java.sql.*;


import com.mayab.quality.loginunittest.dao.UserMysqlDAO;
import com.mayab.quality.loginunittest.model.User;

import com.mayab.quality.loginunittest.dao.UserMysqlDAO;

public class App {
	
	
	 class JDBCTest{
		  public static void main(String[] args) {
			  /*    Connection con = null;
		         try{
		             Class.forName("com.mysql.cj.jdbc.Driver");
		             String dbURL = "jdbc:mysql://localhost:3307//calidad";
		             System.out.println("jdbcurl=" + dbURL);
		             String strUserID = "system";
		             String strPassword = "Oracle123";
		             con=DriverManager.getConnection(dbURL,strUserID,strPassword);
		             System.out.println("Connected to the database.");
		             Statement stmt=con.createStatement();
		             System.out.println("Executing query");
		             ResultSet rs=stmt.executeQuery("SELECT 1 FROM DUAL");
		             while(rs.next())
		                 System.out.println(rs.getInt("1"));
		             con.close();
		         }catch(Exception e){ System.out.println(e);}
		         finally {
		             con.close();
		         }    */
			  
			   // Erstelle ein UserMysqlDAO-Objekt und speichere einen neuen Benutzer
		        UserMysqlDAO dao = new UserMysqlDAO();
		        dao.save(new User("name", "email@email.com", "123456", false)); // 'false' für 'isLogged'
			  
		  }
		     
		    }
}
