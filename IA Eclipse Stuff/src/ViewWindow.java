import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.sqlite.SQLiteDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import java.awt.Component;
import java.awt.Panel;
import javax.swing.JTextField;
import java.awt.Color;

public class ViewWindow extends JFrame 
{

	private JPanel contentPane;
	public int userId;
	public String fName;
	public String lastName;
	public String username;
	public String password;
	public int quantity;
	public String date;
	public String price;
	public String notes;
	public String setNum;
	public int row;
	public String selectedSort;
	private JTextField filterField;
	private Database collection;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run() 
			{
				try 
				{
					ViewWindow frame = new ViewWindow();
					frame.setVisible(true);
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	// Main method where everything is run; Otherwise certain variables would be
	// null,
	// causing the program to crash
	public void run(int id, String first, String last, String use, String pas) 
	{
		userId = id;
		fName = first;
		lastName = last;
		username = use;
		password = pas;
		selectedSort = "Quantity";
		collection = new Database();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 750);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Takes user back to the starting window
		JButton logOutBttn = new JButton("Log Out");
		logOutBttn.setBounds(508, 12, 122, 41);
		logOutBttn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				StartMenu lego = new StartMenu();
				lego.setVisible(true);
				setVisible(false);
			}
		});

		//Shows who's collection is being displayed
		JLabel yrCollectionLbl = new JLabel(fName + "'s Collection");
		yrCollectionLbl.setBounds(142, 10, 374, 41);
		yrCollectionLbl.setHorizontalAlignment(SwingConstants.CENTER);
		yrCollectionLbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
		contentPane.add(yrCollectionLbl);
		
		//Instructs user to select a set in the table to edit or delete
		JLabel dirLabel = new JLabel("Select set to edit or delete it.");
		dirLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dirLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		dirLabel.setBounds(190, 38, 307, 29);
		contentPane.add(dirLabel);

		// Error label
		// Shows error when a row is not selected to edit or delete information
		JLabel errorLbl = new JLabel("Row not selected. Please select a row.");
		errorLbl.setForeground(Color.RED);
		errorLbl.setBounds(135, 61, 398, 29);
		errorLbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		errorLbl.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(errorLbl);
		errorLbl.setVisible(false);

		//Text field for entering filter words
		filterField = new JTextField();
		filterField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		filterField.setBounds(98, 94, 215, 31);
		contentPane.add(filterField);
		filterField.setColumns(10);

		//Where table containing all of the user's set entries is initialized
		//Reads from the LEGO_Collection.db file
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Qty"); 
		model.addColumn("Set #"); 
		model.addColumn("Name"); 
		model.addColumn("Release"); 
		model.addColumn("Purchase Date"); 
		model.addColumn("Price"); 
		model.addColumn("Notes"); 
		
		//Prevents the collection table's contents from being edited outside of the Edit Entry window 
		JTable table = new JTable(model) 
		{
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) 
			{
				return false;
			};
		};
		table.getColumnModel().getColumn(0).setWidth(5);
		table.getColumnModel().getColumn(1).setWidth(10);
		table.getColumnModel().getColumn(2).setWidth(30);
		table.getColumnModel().getColumn(3).setWidth(10);
		table.getColumnModel().getColumn(4).setWidth(10);
		table.getColumnModel().getColumn(5).setWidth(5);
		table.getColumnModel().getColumn(6).setWidth(30);

		// Connects to the LEGO_Collection database
		// So that the user's collection information can be gathered
		// Selects all rows that have a user_id equal to the current user's userId
		String query = "SELECT quantity, set_num, name, year, pur_date, price, notes FROM Collection WHERE user_id = "
				+ userId + " ORDER BY " + selectedSort + " ASC";

		SQLiteDataSource source = collection.getDs();

		try (Connection conn = source.getConnection(); Statement stmt = conn.createStatement();) 
		{

			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) 
			{
				model.insertRow(model.getRowCount(),
						new String[] { rs.getString("quantity"), rs.getString("set_num"), rs.getString("name"),
								rs.getString("year"), rs.getString("pur_date"), rs.getString("price"),
								rs.getString("notes") });
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			System.exit(0);
		}
		table.setRowSelectionAllowed(true);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(98, 133, 532, 402);
		contentPane.add(scrollPane);

		//Takes in user's input from filterField
		//And uses it to filter entries in the user's collection based on the set name
		//Reinitializes the table so that it has the filtered entries
		JButton searchBttn = new JButton("Search");
		searchBttn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		searchBttn.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				String text = filterField.getText();
				String query;
				if (!text.contentEquals("")) 
				{
					query = "SELECT quantity, set_num, name, year, pur_date, price, notes FROM Collection WHERE name LIKE '%"
							+ text + "%' AND" + " user_id = " + userId;
				}

				else
				{
					query = "SELECT quantity, set_num, name, year, pur_date, price, notes FROM Collection WHERE user_id = "
							+ userId;
				}

				SQLiteDataSource source = collection.getDs();

				try (Connection conn = source.getConnection(); Statement stmt = conn.createStatement();) 
				{
					ResultSet rs = stmt.executeQuery(query);
					DefaultTableModel mod = (DefaultTableModel) table.getModel();
					mod.setRowCount(0);
					while (rs.next()) 
					{
						model.insertRow(model.getRowCount(),
								new String[] { rs.getString("quantity"), rs.getString("set_num"), rs.getString("name"),
										rs.getString("year"), rs.getString("pur_date"), rs.getString("price"),
										rs.getString("notes") });
					}
				} 
				catch (SQLException j) 
				{
					j.printStackTrace();
					System.exit(0);
				}
			}
		});
		searchBttn.setBounds(315, 94, 84, 31);
		contentPane.add(searchBttn);

		// Allows user to sort entries based on column headers
		// Entries sorted in ascending order
		JLabel sortInstructions = new JLabel("Sort by: ");
		sortInstructions.setBounds(444, 89, 62, 34);
		sortInstructions.setFont(new Font("Tahoma", Font.PLAIN, 14));
		sortInstructions.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(sortInstructions);

		JComboBox<String> sortOptions = new JComboBox<String>();
		sortOptions.setBounds(516, 92, 114, 29);
		sortOptions.setFont(new Font("Tahoma", Font.PLAIN, 14));
		sortOptions.setEnabled(true);
		//Adds list of options
		sortOptions.addItem("Quantity");
		sortOptions.addItem("Set #");
		sortOptions.addItem("Name");
		sortOptions.addItem("Release");
		sortOptions.addItem("Date Purchased");
		sortOptions.addItem("Purchase Price");
		sortOptions.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				//Gets the String selected from the sortOptions box
				String sort = sortOptions.getSelectedItem().toString();
				
				//Based on the contents of sort
				//Changes selectedSort to the appropriate column header to use in the query String
				switch (sort) 
				{
				case "Quantity":
					selectedSort = "quantity";
					break;
				case "Set #":
					selectedSort = "set_num";
					break;
				case "Name":
					selectedSort = "name";
					break;
				case "Release":
					selectedSort = "year";
					break;
				case "Date Purchased":
					selectedSort = "pur_date";
					break;
				case "Purchase Price":
					selectedSort = "price";
					break;
				}

				// Deletes table's contents and reinitializes them using the given specifications in
				// the query String
				String query = "SELECT quantity, set_num, name, year, pur_date, price, notes FROM Collection WHERE user_id = "
						+ userId + " ORDER BY " + selectedSort + " ASC";

				SQLiteDataSource source = collection.getDs();

				try (Connection conn = source.getConnection(); Statement stmt = conn.createStatement();) {

					ResultSet rs = stmt.executeQuery(query);
					System.out.println("Getting user collection " + userId);

					DefaultTableModel mod = (DefaultTableModel) table.getModel();
					mod.setRowCount(0);
					while (rs.next())
					{
						model.insertRow(model.getRowCount(),
								new String[] { rs.getString("quantity"), rs.getString("set_num"), rs.getString("name"),
										rs.getString("year"), rs.getString("pur_date"), rs.getString("price"),
										rs.getString("notes") });
					}
				} 
				catch (SQLException r) 
				{
					r.printStackTrace();
					System.exit(0);
				}
			}
		});
		contentPane.add(sortOptions);

		// Takes user to Add Entry window
		JButton addEntry = new JButton("Add Entry");
		addEntry.setBounds(98, 566, 170, 55);
		contentPane.add(addEntry);
		addEntry.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				AddEntry add = new AddEntry();
				setVisible(false);
				add.setVisible(true);
				add.run(userId, fName, lastName, username, password);
			}
		});
		addEntry.setFont(new Font("Tahoma", Font.PLAIN, 14));

		// Takes user to Edit Entry window if a set is selected
		// Otherwise causes an error to pop up
		JButton editEntry = new JButton("Edit Entry");
		editEntry.setBounds(278, 566, 170, 55);
		contentPane.add(editEntry);
		editEntry.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				int row = table.getSelectedRow();
				if (row >= 0) 
				{
					EditEntry editor = new EditEntry();
					editor.setVisible(true);
					setVisible(false);
					System.out.println(table.getValueAt(row, 0));
					quantity = Integer.parseInt((String) table.getValueAt(row, 0));
					date = table.getValueAt(row, 4).toString();
					price = table.getValueAt(row, 5).toString();
					notes = table.getValueAt(row, 6).toString();
					editor.run(userId, fName, lastName, username, password,
							Integer.parseInt(table.getValueAt(row, 0).toString()), table.getValueAt(row, 1).toString(),
							table.getValueAt(row, 2).toString(), Integer.parseInt(table.getValueAt(row, 3).toString()),
							table.getValueAt(row, 4).toString(), table.getValueAt(row, 5).toString(),
							table.getValueAt(row, 6).toString());
				} 
				else 
				{
					errorLbl.setVisible(true);
				}
			}
		});
		editEntry.setFont(new Font("Tahoma", Font.PLAIN, 14));
		

		//Pop up for deleting entries
		Panel deletePop = new Panel();
		deletePop.setBounds(157, 541, 418, 120);
		contentPane.add(deletePop);
		deletePop.setLayout(null);
		deletePop.setVisible(false);
		
		JLabel youSureLbl = new JLabel("Are you sure you want to delete this set?");
		youSureLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		youSureLbl.setHorizontalAlignment(SwingConstants.CENTER);
		youSureLbl.setBounds(76, 27, 261, 17);
		deletePop.add(youSureLbl);
		youSureLbl.setVisible(false);
		
		//Makes the pop-up window for deleting entries appear
		JButton deleteEntry = new JButton("Delete Entry");
		deleteEntry.setBounds(460, 566, 170, 55);
		deleteEntry.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				int row = table.getSelectedRow();

				if (row < 0) 
				{
					errorLbl.setVisible(true);
				} else 
				{
					deletePop.setVisible(true);
					youSureLbl.setVisible(true);
					addEntry.setVisible(false);
					editEntry.setVisible(false);
					deleteEntry.setVisible(false);
				}

			}
		});

		if (!deletePop.isVisible()) 
		{
			deleteEntry.setVisible(true);
		}

		deleteEntry.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(deleteEntry);

		// If selected, closes pop up window
		JButton noBttn = new JButton("No");
		noBttn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		noBttn.setBounds(111, 64, 60, 35);
		noBttn.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				deletePop.setVisible(false);
				row = -1;
			}
		});
		deletePop.add(noBttn);

		// If selected, deletes set from the Collection table in LEGO_Collection
		// database
		// And re-initializes table appropriately
		JButton yesBttn = new JButton("Yes");
		yesBttn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		yesBttn.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{

				Collection cur = new Collection(userId, 0, table.getValueAt(row, 1).toString(), null, 0, null, null,
						null);
				cur.deleteEntry();

				DefaultTableModel mod = (DefaultTableModel) table.getModel();
				mod.setRowCount(0);
				String query = "SELECT quantity, set_num, name, year, pur_date, price, notes FROM Collection WHERE user_id = "
						+ userId + "";
				SQLiteDataSource source = collection.getDs();
				try (Connection conn = source.getConnection(); Statement stmt = conn.createStatement();) 
				{
					ResultSet rs = stmt.executeQuery(query);
					while (rs.next()) 
					{
						model.insertRow(model.getRowCount(),
								new String[] { rs.getString("quantity"), rs.getString("set_num"), rs.getString("name"),
										rs.getString("year"), rs.getString("pur_date"), rs.getString("price"),
										rs.getString("notes") });
					}
				} 
				catch (SQLException r) 
				{
					r.printStackTrace();
					System.exit(0);
				}

				youSureLbl.setVisible(false);
				deletePop.setVisible(false);
				addEntry.setVisible(true);
				editEntry.setVisible(true);
				deleteEntry.setVisible(true);
			}
		});
		yesBttn.setBounds(222, 64, 60, 35);
		deletePop.add(yesBttn);
		logOutBttn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(logOutBttn);
	}

	/**
	 * Create the frame.
	 */
	// Empty because otherwise certain variables would be null
	// Causing the program to crash
	public ViewWindow() {

	}
}
