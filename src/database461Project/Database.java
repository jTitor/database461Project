package database461Project;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Database {

	private Connection connection;
	private String dbURL = null;

	public Database() {
		connection = null;
	}

	/**
	 * Constructor useful for using a local database file
	 * @see Database#setDbUrl(String, File)
	 * @param dbms	a type of database ("sqlite", "mysql" or "oracle")
	 * @param localDB a location of the local database file
	 */
	public Database(String dbms, File localDB) {
		this();
		setDbUrl (dbms, localDB);
	}

	/**
	 * Constructor useful for using a remote database
	 * @see Database#setDbUrl(String, String, int)
	 * @param dbms a type of database ("sqlite", "mysql" or "oracle")
	 * @param address a host address of the database
	 * @param port a port number of the database
	 */
	public Database(String dbms, String address, int port) {
		this();
		setDbUrl (dbms, address, port);
	}

	/**
	 * Sets a JDBC URL to connect a local database file
	 * @param dbms dbms a type of database ("sqlite", "mysql" or "oracle")
	 * @param localDB localDB a location of the local database file
	 */
	public void setDbUrl (String dbms, File localDB) {
		dbURL = "jdbc:" + dbms + ":" + localDB.getPath();
	}

	/**
	 * Sets a JDBC URL to connect a remote database
	 * @param dbms dbms a type of database ("sqlite", "mysql" or "oracle")
	 * @param address address a host address of the database
	 * @param port port a port number of the database
	 */
	public void setDbUrl (String dbms, String address, int port) {
		dbURL = "jdbc:" + dbms + ":" + "//" + address + ":" + port + "/";
	}

	/**
	 * Attempts to execute the requested query.
	 *
	 * @param sqlQuery
	 *            The query to execute.
	 * @return True if the query was successfully submitted, false otherwise.
	 *         The query did not necessarily successfully update the database.
	 */
	private boolean doUpdate(String sqlQuery) {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sqlQuery);
		} catch (SQLException sqlE) {
			return false;
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException sqlE) {
				return false;
			}
		}
		return true;
	}

	private ResultSet doQuery(String sqlQuery) {
		Statement statement = null;
		ResultSet result = null;
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(sqlQuery);
		} catch (SQLException sqlE) {
			return null;
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException sqlE) {
				return null;
			}
		}
		return result;
	}

	/**
	 * Specifies if this database is currently connected.
	 *
	 * @return True if the database is currently connected, false otherwise.
	 */
	public boolean IsConnected() {
		return connection != null;
	}

	/**
	 * Connects to the requested local database. Assuming no userID and
	 * password.
	 * @see Database#Connect(String, String)
	 * @see Database#Connect(String, int, String, String)
	 * @return True if connection was successful, false otherwise.
	 */
	public boolean Connect() {
		if (dbURL == null) {
			System.out.println("Please set the location of the database first.");
			return false;
		}

		// Disconnect if we're already connected.
		if (IsConnected()) {
			if (!Disconnect()) {
				return false;
			}
		}

		try {
			connection = DriverManager.getConnection(dbURL);

			/*
			 *  This is for only sqlite3
			 * "Foreign key constraints are disabled by default" in sqlite3
			 */
			Statement s = connection.createStatement();
			s.execute("PRAGMA foreign_keys = ON;");
			s.close();
		} catch (SQLException sqlE) {
			connection = null;
			return false;
		}
		return true;
	}

	/**
	 * Connects to the requested database with user ID and password
	 * @see Database#Connect()
	 * @see Database#Connect(String, int, String, String)
	 * @param user user ID
	 * @param password password
	 * @return True if connection was successful, false otherwise.
	 */
	public boolean Connect(String user, String password) {
		if (dbURL == null) {
			System.out.println("Please set the location of the database first.");
			return false;
		}
		// Disconnect if we're already connected.
		if (IsConnected()) {
			if (!Disconnect()) {
				return false;
			}
		}

		// We're assuming the DB is some kind of SQL database.
		Properties connectionProperties = new Properties();
		connectionProperties.put("user", user);
		connectionProperties.put("password", password);

		try {
			connection = DriverManager.getConnection(dbURL, connectionProperties);

			/*
			 *  This is for only sqlite3
			 * "Foreign key constraints are disabled by default" in sqlite3
			 */
			Statement s = connection.createStatement();
			s.execute("PRAGMA foreign_keys = ON;");
			s.close();
		} catch (SQLException sqlE) {
			connection = null;
			return false;
		}
		return true;
	}

	/**
	 * Connects to the requested database.
	 * @see Database#Connect()
	 * @see Database#Connect(String, String)
	 * @return True if connection was successful, false otherwise.
	 */
	public boolean Connect(String address, int port, String user, String password) {
		// Disconnect if we're already connected.
		if (IsConnected()) {
			if (!Disconnect()) {
				return false;
			}
		}

		// We're assuming the DB is some kind of SQL database.
		Properties connectionProperties = new Properties();
		connectionProperties.put("user", user);
		connectionProperties.put("password", password);

		try {
			connection = DriverManager.getConnection("jdbc:mysql://" + address + ":" + port + "/",
					connectionProperties);
		} catch (SQLException sqlE) {
			connection = null;
			return false;
		}
		return true;
	}

	/**
	 * Disconnects from the currently connected database.
	 *
	 * @return True if disconnection was successful.
	 */
	public boolean Disconnect() {
		if (connection == null) {
			return true;
		}

		// Make sure we don't have an active transaction!
		try {
			connection.close();
		} catch (SQLException sqlE) {
			return false;
		}
		return true;
	}

	/**
	 * Selects a series of records.
	 */
	// public ? Select(String query)

	/**
	 * Inserts a record.
	 */
	// public boolean Insert(String tableName, String[] attributes)

	/**
	 * Inserts multiple records.
	 */
	// public boolean InsertMultiple(String tableName)

	/**
	 * Deletes a record.
	 */
	// public boolean Delete(String tableName)

	/**
	 * Creates a table.
	 *
	 * @return True if the table was connected, false otherwise.
	 */
	public boolean CreateTable(String tableName, String[] attributes) {
		String attributesStr = "";
		for (int i = 0; i < attributes.length; ++i) {
			attributesStr += attributes[i];
			if (i != attributes.length - 1) {
				attributesStr += ",\n";
			}
		}
		String query = "create table " + tableName + "(" + attributesStr + ")";
		return doUpdate(query);
	}

	// "id INTEGER PRIMARY KEY, "
	// + "name TEXT NOT NULL);"

	/**
	 * Erases all tables.
	 *
	 * @return True if all tables were successfully dropped, false otherwise.
	 */
	// public boolean DropAllTables()
}
