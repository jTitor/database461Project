package database461Project;

import java.io.File;
import java.util.Scanner;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Driver {
	private static Database database;
	private static boolean inCommandLoop = false;

	static File DB_LOCATION = new File(System.getProperty("user.dir"), "UMBC.db");

	private static boolean initialize() {
		/* Temporarily, I choose to use sqlite3. Later, we can change it. */
		database = new Database("sqlite", DB_LOCATION);
		return database.Connect();
	}

	private static void doCommand(String command) {
		String lowerCommand = command.toLowerCase();
		if (lowerCommand.equals(CommandStrings.LoadCSV)) {
			// Load a CSV.
			throw new NotImplementedException();
		} else if (lowerCommand.equals(CommandStrings.ManualQuery)) {
			// Execute a manual query.
			throw new NotImplementedException();
		} else if (lowerCommand.equals(CommandStrings.Quit)) {
			// Exit the command loop.
			inCommandLoop = false;
		} else {
			// Invalid command; do nothing.
			throw new NotImplementedException();
		}
	}

	private static void commandLoop() {
		inCommandLoop = true;
		while (inCommandLoop) {
			// Ask for a command.
			Scanner s = new Scanner(System.in);
			String command = s.next();
			// Try to execute that command.
			doCommand(command);
		}
		throw new NotImplementedException();
	}

	public static void main(String[] args) {
		// Try to connect to the database.
		// If that failed, exit.
		if (!initialize()) {
			return;
		}
		// Otherwise, enter command loop.
		commandLoop();
	}
}
