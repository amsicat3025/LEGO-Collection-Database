
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.table.DefaultTableModel;

import org.sqlite.SQLiteDataSource;

//Class for writing to the Collection table in the LEGO_Collection Database
//Utilizes SQLite

public class Collection 
{
	private int myUserId;
	private int myQuantity;
	private String mySetNum;
	private String myName;
	private int myYear;
	private String myPurDate;
	private String myPrice;
	private String myNotes;
	private Database myCollection;

	public Collection(int id, int qty, String num, String setName, int release, String date, String purPrice,
			String note) 
	{
		myUserId = id;
		myQuantity = qty;
		mySetNum = num;
		myName = setName;
		myYear = release;
		myPurDate = date;
		myPrice = purPrice;
		myNotes = note;
		myCollection = new Database();
	}

	//Adds an entry into the Collection table by writing to the LEGO_Collection.db file 
	//The user_id denotes which user's set that is
	public void addEntry() 
	{
		String query = "INSERT INTO Collection(user_id, quantity, set_num, name, year, pur_date, price, notes) VALUES "
				+ "(" + myUserId + ", " + myQuantity + ", '" + mySetNum + "', '" + myName + "', " + myYear + ", '"
				+ myPurDate + "', '" + myPrice + "', '" + myNotes + "')";

		SQLiteDataSource source = myCollection.getDs();

		try (Connection conn = source.getConnection(); Statement stmt = conn.createStatement();) {
			stmt.execute(query);
			System.out.println(query);

		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			System.exit(0);
		}
	}

	//Adds edited information to a particular row in the Collection table
	//By writing to the LEGO_Collection.db file where specified
	//Specific row denoted by the userId and the set number given
	public void editEntry() 
	{
		String query = "UPDATE Collection SET quantity = " + myQuantity + ", " + "pur_date = '" + myPurDate + "', "
				+ "price = '" + myPrice + "', " + "notes = '" + myNotes + "' " + "WHERE user_id = " + myUserId
				+ " AND set_num = '" + mySetNum + "'"; 
		
		//Establishes database connection 
		SQLiteDataSource source = myCollection.getDs();

		try (Connection conn = source.getConnection(); Statement stmt = conn.createStatement();) 
		{
			stmt.execute(query);

		} 
		catch (SQLException p) 
		{
			p.printStackTrace();
			System.exit(0);
		}
	}

	//Deletes information to a particular row in the Collection table
	//By deleting from the LEGO_Collection.db file where specified
	//Specific row denoted by the userId and the set number given
	public void deleteEntry() 
	{
		String query = "DELETE FROM Collection WHERE user_id = " + myUserId + " AND set_num = '" + mySetNum + "'";

		//Establishes database connection
		SQLiteDataSource source = myCollection.getDs();

		try (Connection conn = source.getConnection(); Statement stmt = conn.createStatement();) 
		{
			stmt.execute(query);
		} 
		catch (SQLException r) 
		{
			r.printStackTrace();
			System.exit(0);
		}
	}
}
