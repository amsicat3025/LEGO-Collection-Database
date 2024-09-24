import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.sqlite.SQLiteDataSource;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; //? 
import java.text.NumberFormat;
import java.util.Properties;

import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import java.awt.event.MouseAdapter;
import javax.swing.JButton;
import java.awt.Color;

public class EditEntry extends JFrame 
{

	private JPanel contentPane;
	private JTextField qtyField;
	private JTextField purchaseDate;
	private JTextField purPrice;
	private JTextField notesField;
	public String fName;
	public String lastName;
	public int userId;
	public String username;
	public String password;
	public int quantity;
	public String setNum;
	public String name;
	public int year;
	public String date;
	public String price;
	public String notes;

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
					EditEntry frame = new EditEntry();
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
	public void run(int id, String first, String last, String use, String pas, int qty, String set, String setName,
			int release, String purDate, String purchase, String descr) 
	{
		userId = id;
		fName = first;
		lastName = last;
		username = use;
		password = pas;
		quantity = qty;
		setNum = set;
		name = setName;
		year = release;
		date = purDate;
		price = purchase;
		notes = descr;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Instruction label for editing entries
		JLabel lblEditInformationBelow = new JLabel("Edit information below.");
		lblEditInformationBelow.setHorizontalAlignment(SwingConstants.CENTER);
		lblEditInformationBelow.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblEditInformationBelow.setBounds(81, 22, 336, 22);
		contentPane.add(lblEditInformationBelow);

		//Displays the correct errors when appropriate
		JLabel errorLbl = new JLabel("");
		errorLbl.setForeground(Color.RED);
		errorLbl.setHorizontalAlignment(SwingConstants.CENTER);
		errorLbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		errorLbl.setBounds(57, 54, 391, 22);
		contentPane.add(errorLbl);
		errorLbl.setVisible(false);

		//Displays the set information being edited
		JLabel editingLbl = new JLabel("Editing: " + name); // Okay idk how I'm doing this...
		editingLbl.setHorizontalAlignment(SwingConstants.LEFT);
		editingLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		editingLbl.setBounds(57, 75, 357, 34);
		contentPane.add(editingLbl);

		//Displays the quantity of a set being edited
		JLabel qtyLbl = new JLabel("Quantity:");
		qtyLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		qtyLbl.setBounds(47, 168, 65, 21);
		contentPane.add(qtyLbl);

		qtyField = new JTextField();
		qtyField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		qtyField.setColumns(10);
		qtyField.setBounds(47, 195, 65, 39);
		contentPane.add(qtyField);
		qtyField.setText("" + quantity + "");

		//Initializes a datePicker
		//Which lets the user choose the date they purchased a set from an
		//Interactive calendar GUI
		//Wait shouldn't I fix this so it's initialized to whatever it was initially?
		//Eh it should be fine... 
		//Ask Daddy just in case
		JLabel dateLabel = new JLabel("Date Purchased:");
		dateLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		dateLabel.setBounds(45, 119, 114, 39);
		contentPane.add(dateLabel);

		UtilDateModel dModel = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(dModel, p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter()); 
		datePicker.getJFormattedTextField().setFont(new Font("Tahoma", Font.PLAIN, 14));
		datePicker.setBounds(152, 129, 170, 40);
		contentPane.add(datePicker);

		// The purchase price of a set
		// Displays the purchase price
		JLabel priceLbl = new JLabel("Purchase Price:");
		priceLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		priceLbl.setBounds(152, 167, 95, 22);
		contentPane.add(priceLbl);
		
		purPrice = new JTextField();
		purPrice.setFont(new Font("Tahoma", Font.PLAIN, 14));
		purPrice.setColumns(10);
		purPrice.setBounds(152, 195, 170, 39);
		contentPane.add(purPrice);
		purPrice.setText(price.substring((price.indexOf("$") + 1), price.length()));

		//User can edit their notes about the set they purchased here if they wish
		JLabel notesFieldLabel = new JLabel("Notes:");
		notesFieldLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		notesFieldLabel.setBounds(47, 244, 49, 31);
		contentPane.add(notesFieldLabel);

		notesField = new JTextField();
		notesField.setHorizontalAlignment(SwingConstants.LEFT);
		notesField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		notesField.setColumns(10);
		notesField.setBounds(47, 285, 391, 86);
		contentPane.add(notesField);
		notesField.setText(notes);

		//Closes Edit Entry Window
		//Opens View Window 
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cancelButton.setBounds(298, 381, 140, 40);
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
		contentPane.add(cancelButton);

		// Saves the edits to the Collection table in LEGO_Databse
		JButton saveButton = new JButton("Save");
		saveButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		saveButton.setBounds(148, 381, 140, 40);
		saveButton.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				int qTest = 0;
				double pTest;
				quantity = 0;
				price = null;
				if (datePicker.getJFormattedTextField().getText().contentEquals("")) 
				{
					errorLbl.setVisible(true);
					errorLbl.setText("Please fill out all fields.");
				} 
				else 
				{
					try // Makes sure that quantity and price are appropriate values (int and double)
					{
						qTest = Integer.parseInt(qtyField.getText());
						quantity = qTest;

						pTest = Double.parseDouble(purPrice.getText());
						NumberFormat formatter = NumberFormat.getCurrencyInstance(); // Makes it so that the entered
																						// String is a monetary value
						price = "" + formatter.format(pTest) + "";
						date = datePicker.getJFormattedTextField().getText();
						notes = notesField.getText();

						Collection cur = new Collection(userId, quantity, setNum, name, year, date, price, notes);
						cur.editEntry(); // Updates the information by writing to the LEGO_Collection.db file
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
		contentPane.add(saveButton);
	}

	/*
	 * /** Create the frame.
	 */
	// This class is empty because otherwise necessary variables (userId, fName,
	// lastName, etc. would not be carried over,
	// causing the program to crash)
	public EditEntry() 
	{
		
	}
}
