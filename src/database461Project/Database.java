package database461Project;
//What is our JDBC package?
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.sql.Connection;

public class Database
{
	private Connection connection;
	
	/**
	 * Attempts to execute the requested query.
	 * @param sqlQuery The query to execute.
	 * @return True if the query was successfully submitted, false otherwise.
	 * The query did not necessarily successfully update the database.
	 */
	private boolean doUpdate(String sqlQuery)
	{
		Statement statement = null;
		try
		{
			statement = connection.createStatement();
			statement.executeUpdate(sqlQuery);
		}
		catch(SQLException sqlE)
		{
			return false;
		}
		finally
		{
			try
			{
				if(statement != null)
				{
					statement.close();
				}
			}
			catch(SQLException sqlE)
			{
				return false;
			}
		}
		return true;
	}
	
	private ResultSet doQuery(String sqlQuery)
	{
		Statement statement = null;
		ResultSet result = null;
		try
		{
			statement = connection.createStatement();
			result = statement.executeQuery(sqlQuery);
		}
		catch(SQLException sqlE)
		{
			return null;
		}
		finally
		{
			try
			{
				if(statement != null)
				{
					statement.close();
				}
			}
			catch(SQLException sqlE)
			{
				return null;
			}
		}
		return result;
	}
	
	public Database()
	{
		connection = null;
	}
	
	/**
	 * Specifies if this database is currently connected.
	 * @return True if the database is currently connected,
	 * false otherwise.
	 */
	public boolean IsConnected()
	{
		return connection != null;
	}
	
	/**
	 * Connects to the requested database.
	 * @return True if connection was successful,
	 *  false otherwise.
	 */
	public boolean Connect(String address, int port, String user, String password)
	{
		//Disconnect if we're already connected.
		if(IsConnected())
		{
			if(!Disconnect())
			{
				return false;
			}
		}
		
		//We're assuming the DB is some kind of SQL database.
		Properties connectionProperties = new Properties();
		connectionProperties.put("user", user);
		connectionProperties.put("password", password);
		
		try
		{
			connection = DriverManager.getConnection("jdbc:mysql://" + address + ":" + port + "/", connectionProperties);
		}
		catch (SQLException sqlE)
		{
			connection = null;
			return false;
		}
		return true;
	}
	
	/**
	 * Disconnects from the currently connected database.
	 * @return True if disconnection was successful.
	 */
	public boolean Disconnect()
	{
		if(connection == null)
		{
			return true;
		}
		
		//Make sure we don't have an active transaction!
		try
		{
			connection.close();
		}
		catch(SQLException sqlE)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Selects a series of records.
	 */
	//public ? Select(String query)
	
	/**
	 * Inserts a record.
	 */
	//public boolean Insert(String tableName, String[] attributes)
	
	/**
	 * Inserts multiple records.
	 */
	//public boolean InsertMultiple(String tableName)
	
	/**
	 * Deletes a record.
	 */
	//public boolean Delete(String tableName)
	
	/**
	 * Creates a table.
	 * @return True if the table was connected, false otherwise.
	 */
	public boolean CreateTable(String tableName, String[] attributes)
	{
		String attributesStr = "";
		for(int i = 0; i < attributes.length; ++i)
		{
			attributesStr += attributes[i];
			if(i != attributes.length - 1)
			{
				attributesStr += ",\n";
			}
		}
		String query = "create table " + tableName + "(" + attributesStr + ")";
		return doUpdate(query);
	}
	
	/**
	 * Erases all tables.
	 * @return True if all tables were successfully dropped, false otherwise.
	 */
	//public boolean DropAllTables()
}
