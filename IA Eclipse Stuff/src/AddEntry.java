import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Properties;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.sqlite.SQLiteDataSource;

import javax.swing.SwingConstants;
import javax.swing.SpringLayout;

public class AddEntry extends JFrame 
{

	private JPanel contentPane;
	private JTextField filterField;
	private JTextField purPrice;
	private JTextField notesField;
	public String fName;
	public String lastName;
	public int userId;
	public String username;
	public String password;
	public int quantity;
	public String setNum;
	public String date;
	public String price;
	public String notes;
	private int row;
	private String selectedSort;
	private JTextField qtyField;
	private Database catalogue;
	private SpringLayout springLayout; //uh

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
					AddEntry frame = new AddEntry();
					frame.setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	// Main method where everything is run; Otherwise certain variables would be
	// null
	// causing the program to crash
	public void run(int id, String first, String last, String use, String pas) 
	{
		userId = id;
		fName = first;
		lastName = last;
		username = use;
		password = pas;
		catalogue = new Database();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 750);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Directions for adding entries
		// Changes depending what part of process user is on
		JLabel AddEntryDir = new JLabel("Select set to add it.");
		AddEntryDir.setHorizontalAlignment(SwingConstants.CENTER);
		AddEntryDir.setFont(new Font("Tahoma", Font.PLAIN, 16));
		AddEntryDir.setBounds(179, 22, 336, 22);
		contentPane.add(AddEntryDir);

		//Displays the correct errors when appropriate
		JLabel errorLbl = new JLabel("");
		errorLbl.setFont(new Font("Tahoma", Font.BOLD, 10));
		errorLbl.setHorizontalAlignment(SwingConstants.CENTER);
		errorLbl.setForeground(Color.RED);
		errorLbl.setBounds(57, 48, 615, 22);
		contentPane.add(errorLbl);
		errorLbl.setVisible(false);

		//Text field for entering filter words
		filterField = new JTextField();
		filterField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		filterField.setBounds(98, 80, 215, 31);
		contentPane.add(filterField);
		filterField.setColumns(10);

		// Table where catalogue of all LEGO Sets ever produced
		// is displayed. Table initialized based on the LEGO_Database table in the
		// LEGO_Collection table
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Set #");
		model.addColumn("Name");
		model.addColumn("Year Released");
		model.addColumn("# of Parts");
		
		//Prevents catalogue table's contents from being edited
		JTable table = new JTable(model) 
		{
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) 
			{
				return false;
			};
		};
		table.getColumnModel().getColumn(0).setWidth(10);
		table.getColumnModel().getColumn(1).setWidth(20);
		table.getColumnModel().getColumn(2).setWidth(5);
		table.getColumnModel().getColumn(3).setWidth(10);

		//Where table containing a catalogue of all existing sets is initialized
		//Reads from the LEGO_Collection.db file
		String query = "SELECT set_num, name, year, theme_id, num_parts FROM LEGO_Database";

		// Establishes connection to the database
		SQLiteDataSource source = catalogue.getDs();

		try (Connection conn = source.getConnection(); Statement stmt = conn.createStatement();) {
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("Getting user collection " + userId);
			while (rs.next()) 
			{
				model.insertRow(model.getRowCount(), new String[] { rs.getString("set_num"), rs.getString("name"),
						rs.getString("year"), rs.getString("theme_id"), rs.getString("num_parts") });
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			System.exit(0);
		}
		table.setRowSelectionAllowed(true);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(98, 129, 532, 230);
		contentPane.add(scrollPane);

		//Lets user search for specific sets using the contents of the filterField
		//To add to the query String
		JButton searchBttn = new JButton("Search");
		searchBttn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		searchBttn.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				String text = filterField.getText();
				String query;
				if (!filterField.getText().contentEquals("")) 
				{
					query = "SELECT set_num, name, year, theme_id, num_parts FROM LEGO_Database WHERE name LIKE '%"
							+ text + "%'";
				} 
				else 
				{
					query = "SELECT set_num, name, year, theme_id, num_parts FROM LEGO_Database";
				}

				SQLiteDataSource source = catalogue.getDs();

				try (Connection conn = source.getConnection(); Statement stmt = conn.createStatement();) 
				{
					ResultSet rs = stmt.executeQuery(query);
					DefaultTableModel mod = (DefaultTableModel) table.getModel();
					mod.setRowCount(0);
					while (rs.next()) 
					{
						model.insertRow(model.getRowCount(),
								new String[] { rs.getString("set_num"), rs.getString("name"), rs.getString("year"),
										rs.getString("theme_id"), rs.getString("num_parts") });
					}
				} 
				catch (SQLException j) 
				{
					j.printStackTrace();
					System.exit(0);
				}
			}
		});
		searchBttn.setBounds(333, 80, 84, 31);
		contentPane.add(searchBttn);

		//Where the user enters the quantity of a particular set they have in their collection
		JLabel qtyLbl = new JLabel("Quantity:");
		qtyLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		qtyLbl.setBounds(98, 414, 65, 21);
		contentPane.add(qtyLbl);

		qtyField = new JTextField();
		qtyField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		qtyField.setColumns(10);
		qtyField.setBounds(98, 445, 65, 39);
		contentPane.add(qtyField);
		
		//Where user inputs the purchase date of the set they obtained
		JLabel dateLabel = new JLabel("Date Purchased:");
		dateLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		dateLabel.setBounds(98, 382, 114, 22);
		contentPane.add(dateLabel);

		//Initializes a datePicker
		//Which lets the user choose the date they purchased a set from an
		//Interactive calendar GUI
		UtilDateModel dModel = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(dModel, p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter()); 
		datePicker.getJFormattedTextField().setFont(new Font("Tahoma", Font.PLAIN, 14));
		datePicker.setBounds(214, 376, 170, 40);
		contentPane.add(datePicker);

		//Where the user inputs the purchase price of the set they obtained
		JLabel priceLabel = new JLabel("Purchase Price (numbers only):");
		priceLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		priceLabel.setBounds(214, 414, 246, 22);
		contentPane.add(priceLabel);

		purPrice = new JTextField();
		purPrice.setFont(new Font("Tahoma", Font.PLAIN, 14));
		purPrice.setColumns(10);
		purPrice.setBounds(214, 445, 170, 39);
		contentPane.add(purPrice);

		//User can enter notes about the set they purchased here if they wish
		JLabel notesFieldLabel = new JLabel("Notes:");
		notesFieldLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		notesFieldLabel.setBounds(98, 494, 49, 31);
		contentPane.add(notesFieldLabel);

		notesField = new JTextField();
		notesField.setHorizontalAlignment(SwingConstants.LEFT);
		notesField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		notesField.setColumns(10);
		notesField.setBounds(98, 524, 532, 86);
		contentPane.add(notesField);

		// Adds information user inputted, as well as the selected set info
		// To the Collection table under the user's userId
		JButton addButton = new JButton("Add");
		addButton.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				setNum = table.getValueAt(row, 0).toString();
				//Displays error if any of the fields are null
				if (datePicker.getJFormattedTextField().getText().contentEquals("") || qtyField.getText().contentEquals("") || purPrice.getText().contentEquals("")) 
				{
					errorLbl.setVisible(true);
					errorLbl.setText("Please fill out all fields.");
				} 
				else 
				{
					int qTest = 0;
					double pTest;
					// Makes sure quantity and price are appropriate values (int and double)
					try 
					{
						qTest = Integer.parseInt(qtyField.getText());
						quantity = qTest;

						pTest = Double.parseDouble(purPrice.getText());
						NumberFormat formatter = NumberFormat.getCurrencyInstance(); // Casts the price to a monetary
																						// value
						price = "" + formatter.format(pTest) + "";
						date = datePicker.getJFormattedTextField().getText();
						notes = notesField.getText();

						Collection cur = new Collection(userId, quantity, setNum, table.getValueAt(row, 1).toString(),
								Integer.parseInt(table.getValueAt(row, 2).toString()), date, price, notes);
						cur.addEntry(); // Adds the information by writing to the LEGO_Collection.db file
						ViewWindow view = new ViewWindow(); // Takes user back to ViewWindow to see the update to their
															// collection
						setVisible(false);
						view.setVisible(true);
						view.run(userId, fName, lastName, username, password);
					} 
					catch (Exception p) 
					{
						errorLbl.setVisible(true);
						errorLbl.setText("ERROR: Incorrect value type for quantity and/or price.");
					}
				}
			}
		});
		addButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		addButton.setBounds(264, 629, 170, 55);
		contentPane.add(addButton);

		// Allows user to sort entries based on column headers
		// Entries sorted in ascending order
		JLabel sortInstructions = new JLabel("Sort by: ");
		sortInstructions.setBounds(427, 78, 84, 34);
		sortInstructions.setFont(new Font("Tahoma", Font.PLAIN, 14));
		sortInstructions.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(sortInstructions);

		JComboBox<String> sortOptions = new JComboBox<String>();
		sortOptions.setBounds(505, 81, 114, 29);
		sortOptions.setFont(new Font("Tahoma", Font.PLAIN, 14));
		sortOptions.setEnabled(true);
		sortOptions.addItem("Set #");
		sortOptions.addItem("Name");
		sortOptions.addItem("Year Released");
		sortOptions.addItem("Theme Id");
		sortOptions.addItem("# of Parts");
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
				case "Set #":
					selectedSort = "set_num";
					break;
				case "Name":
					selectedSort = "name";
					break;
				case "Year":
					selectedSort = "year";
					break;
				case "Theme Id":
					selectedSort = "theme_id";
					break;
				case "# of Parts":
					selectedSort = "num_parts";
					break;
				}

				// Deletes table's contents and reinitializes them using the given specifications in
				// the query String
				String query = "SELECT set_num, name, year, theme_id, num_parts FROM LEGO_Database ORDER BY "
						+ selectedSort + " ASC";
				SQLiteDataSource source = catalogue.getDs();

				try (Connection conn = source.getConnection(); Statement stmt = conn.createStatement();) 
				{

					ResultSet rs = stmt.executeQuery(query);

					DefaultTableModel mod = (DefaultTableModel) table.getModel();
					mod.setRowCount(0); 
					while (rs.next()) 
					{
						model.insertRow(model.getRowCount(),
								new String[] { rs.getString("set_num"), rs.getString("name"), rs.getString("year"),
										rs.getString("theme_id"), rs.getString("num_parts") });
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

		// Cancels the user's adding process, 
		//Closes the Add Entry Window
		//Displays the View Window
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				ViewWindow view = new ViewWindow();
				setVisible(false);
				view.setVisible(true);
				view.run(userId, fName, lastName, username, password);
			}
		});
		cancelButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		cancelButton.setBounds(460, 629, 170, 54);
		contentPane.add(cancelButton);
	}

	/**
	 * Create the frame.
	 */
	public AddEntry() 
	{
	

	}

}


