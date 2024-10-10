package com.example.Job.MicroService.job;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Statement;

import org.hibernate.dialect.OracleTypes;
import org.hibernate.type.SqlTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JdbcJAva {
	
	
	public static Logger logger  = LoggerFactory.getLogger(JdbcJAva.class);
	String url = "jdbc:mysql://localhost:3306/demo";
	String user = "root";
	String password = "****";

	public Connection getConnection() throws SQLException {
		Connection connection = DriverManager.getConnection(url,user,password);
		return connection;
	}

	public int getStudentDetails(int marks) {
		CallableStatement statement = null;
		ResultSet rs = null;
		int number = 0;

		try {
			System.out.println("inside getStudentDetails with marks as "+marks);
			Connection connection = getConnection();
			String query = "CALL STUDENTS_WITH_INPUT_AND__OUTPUT_PARAM(?,?)";
			statement = connection.prepareCall(query);
			statement.setInt(1, marks);
			statement.registerOutParameter(2,SqlTypes.INTEGER);
			statement.execute();
			number  = statement.getInt(2);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return number;

	}
	
	public Job getJobDetailsByJobId(long jobId)
	{
		
		logger.info("JdBcJava::getJobDetailsByJobId");
		PreparedStatement statement = null;
		Job job = null;
		ResultSet rs = null;
		try
		{
			Connection connection = getConnection();
			String query = "Select description, location,max_salary,min_salary,title from job where id=?";
			statement = connection.prepareStatement(query);
			statement.setLong(1, jobId);
			 rs =  statement.executeQuery();
			  String title;
				 String description = null;
				 String maxSalary = null;
				 String minSalary = null;
				 String location= null;
			 while(rs.next())
			 {
				 description = rs.getString("description");
				 location = rs.getString("location");
				 minSalary = rs.getString("min_salary");
				 maxSalary = rs.getString("max_salary");
				 title = rs.getString("title");
				 
			 }
			  job = new Job();
			 job.setId(jobId);
			 job.setDescription(description);
			 job.setLocation(location);
			 job.setMaxSalary(maxSalary);
			 job.setMinSalary(minSalary);
			 
			 
			
		}
		
		catch(SQLException sq)
		{
			sq.printStackTrace();
		}
		return job;
	}

}
