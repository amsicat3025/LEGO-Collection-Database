
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.sqlite.SQLiteDataSource;

//Class for writing to the Users table in the LEGO_Collection Database
//Utilizes SQLite
public class Users 
{
	private int myUserId;
	private String myFName;
	private String myLName;
	private String myUsername;
	private String myPassword;
	private Database myUsers;

	public Users(int userId, String fName, String lastName, String user, String pas) 
	{
		myUserId = userId;
		myFName = fName;
		myLName = lastName;
		myUsername = user;
		myPassword = pas;
		myUsers = new Database();
	}

	//Returns a Users object if it exists 
	//Accomplishes this by reading from the LEGO_Collection.db file
	public Users getUser(String username, String password) 
	{
		String query = "SELECT user_id, first_name, last_name, username, password FROM Users WHERE username = '"
				+ myUsername + "' AND password = '" + myPassword + "'";
		
		//Establishes database connection 
		SQLiteDataSource source = myUsers.getDs();
		Users usr = null;
		try (Connection conn = source.getConnection(); Statement stmt = conn.createStatement();) 
		{
			ResultSet rs = stmt.executeQuery(query);
			//if rs.isClosed(), it means that the requested information from the LEGO_Collection.db file does not exist
			if (rs.isClosed() == false) 
			{
				usr = new Users(rs.getInt("user_id"), rs.getString("first_name"), rs.getString("last_name"),
						rs.getString("username"), rs.getString("password"));
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			System.exit(0);
		}
		return usr;
	}

	//Checks if the entered username is taken when creating a new account
	//By reading from the LEGO_Collection.db file 
	public boolean isUsernameTaken(String username) 
	{
		String query = "SELECT user_id FROM Users WHERE username = '" + myUsername + "'";
		
		//Establishes database connection 
		SQLiteDataSource source = myUsers.getDs();
		try (Connection conn = source.getConnection(); Statement stmt = conn.createStatement();) 
		{
			ResultSet rs = stmt.executeQuery(query);
			//if rs.isClosed(), it means that the requested information from the LEGO_Collection.db file does not exist
			if (rs.isClosed() == false) 
			{
				return true;
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			System.exit(0);
		}
		return false;
	}

	//Creates a new user and adds it to the database
	//By writing to the LEGO_Collection.db file 
	public void addUser() 
	{
		String query1 = "INSERT INTO Users(username, password, first_name, last_name) VALUES ('" + myUsername + "', '"
				+ myPassword + "', '" + myFName + "', '" + myLName + "')";
		SQLiteDataSource source = myUsers.getDs();

		try (Connection conn = source.getConnection(); Statement stmt = conn.createStatement();) 
		{
			stmt.execute(query1); // inserts new user into Users table; unique user_id generated when this occurs,
									// which is why myUserId is not used here
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			System.exit(0);
		}
	}

	//Returns the user's userId
	public int getUserId() 
	{
		return myUserId;
	}

	// Returns user's first name
	public String getFirstName() 
	{
		return myFName;
	}

	// Returns user's last name
	public String getLastName() 
	{
		return myLName;
	}

	// Returns username;
	public String getUsername() 
	{
		return myUsername;
	}

	// Returns password;
	public String getPassword() 
	{
		return myPassword;
	}

}
