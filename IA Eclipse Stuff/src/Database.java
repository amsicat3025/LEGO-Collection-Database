
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.sqlite.SQLiteDataSource;

//Class handles connecting to the Database and initializing it 
public class Database 
{
	private SQLiteDataSource ds;

	//Initializes database by specifying the URL of the file
	public Database() 
	{
		try 
		{
			ds = new SQLiteDataSource();
			ds.setUrl("jdbc:sqlite:LEGO_Collection.db");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			System.exit(0);
		}
	}

	//Returns ds
	public SQLiteDataSource getDs() 
	{
		return ds;
	}
}