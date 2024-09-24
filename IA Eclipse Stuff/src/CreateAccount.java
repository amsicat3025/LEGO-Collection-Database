import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class CreateAccount extends JFrame 
{

	private JPanel contentPane;
	private JTextField fNameField;
	private JTextField lastNameField;
	private JTextField usernameField;
	private JPasswordField passwordField;

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
					CreateAccount frame = new CreateAccount();
					frame.setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CreateAccount() 
	{

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 620, 750);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel acctCreationLabel = new JLabel("Account Creation");
		acctCreationLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		acctCreationLabel.setHorizontalAlignment(SwingConstants.CENTER);
		acctCreationLabel.setBounds(144, 49, 293, 41);
		contentPane.add(acctCreationLabel);

		//Displays the correct errors when appropriate
		JLabel errorLbl = new JLabel("");
		errorLbl.setForeground(Color.RED);
		errorLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		errorLbl.setBounds(65, 95, 444, 41);
		contentPane.add(errorLbl);
		errorLbl.setVisible(false);

		//Where the user enters their username
		JLabel fNameLabel = new JLabel("First Name:");
		fNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		fNameLabel.setBounds(65, 156, 72, 31);
		contentPane.add(fNameLabel);
		
		fNameField = new JTextField();
		fNameField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		fNameField.setColumns(10);
		fNameField.setBounds(65, 197, 444, 35);
		contentPane.add(fNameField);

		//Where the user enters their last name
		lastNameField = new JTextField();
		lastNameField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lastNameField.setColumns(10);
		lastNameField.setBounds(65, 301, 444, 35);
		contentPane.add(lastNameField);

		JLabel lastNameLabel = new JLabel("Last Name:");
		lastNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lastNameLabel.setBounds(65, 260, 72, 31);
		contentPane.add(lastNameLabel);

		//Where the user enters their desired username
		//The username cannot be a duplicate of a pre-existing username
		JLabel usernameLbl = new JLabel("Username: ");
		usernameLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		usernameLbl.setBounds(65, 364, 72, 31);
		contentPane.add(usernameLbl);

		usernameField = new JTextField();
		usernameField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		usernameField.setColumns(10);
		usernameField.setBounds(65, 405, 444, 35);
		contentPane.add(usernameField);

		//Where the user enters their desired password
		JLabel lblEnterAPassword = new JLabel("Enter a password that is at least 8 characters.");
		lblEnterAPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEnterAPassword.setBounds(65, 464, 288, 31);
		contentPane.add(lblEnterAPassword);

		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		passwordField.setColumns(10);
		passwordField.setBounds(65, 505, 444, 35);
		contentPane.add(passwordField);

		// Cancels account creation process
		//Takes the user back to the StartMenu
		//Closes account creation window
		JButton cancelBttn = new JButton("Cancel");
		cancelBttn.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				StartMenu start = new StartMenu();
				usernameField.setText("");
				passwordField.setText("");
				start.setVisible(true);
				setVisible(false);
			}
		});
		cancelBttn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cancelBttn.setBounds(309, 583, 200, 55);
		contentPane.add(cancelBttn);

		// If conditions are fulfilled (i.e. username has not been used before in users
		// database, user's inputted password is >= 8 characters)
		// Creates new account for user, writing to the database for Users
		// And takes user to the View Window
		JButton newAcctScreen = new JButton("Create Account");
		newAcctScreen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (fNameField.getText().equals("") || lastNameField.getText().equals("")
						|| usernameField.getText().equals("") || passwordField.getText().equals("")) 
					System.out.println("ERROR: Please fill in all fields.");
				else 
				{
					//Error for when the password the user inputs is less than 8 characters
					if (passwordField.getText().length() < 8) 
					{
						errorLbl.setVisible(true);
						errorLbl.setText("Password is shorter than 8 characters. Please enter a longer password.");
					} 
					else 
					{
						ViewWindow view = new ViewWindow();
						Users cur = new Users(0, null, null, usernameField.getText(), passwordField.getText());
						//Error for if the user's inputted username is already taken
						if (cur.isUsernameTaken(usernameField.getText())) 
						{
							errorLbl.setVisible(true);
							errorLbl.setText("Username is already taken. Please a different new username.");
						} 
						//Creates account for the new user using the user's input, adds it to the database
						//And takes the user to the View Window
						//Closes the ACcount Creation window
						else 
						{
							Users newUser = new Users(0, fNameField.getText(), lastNameField.getText(),
									usernameField.getText(), passwordField.getText());
							newUser.addUser();
							newUser = newUser.getUser(usernameField.getText(), passwordField.getText());
							setVisible(false);
							view.setVisible(true);
							view.run(newUser.getUserId(), newUser.getFirstName(), newUser.getLastName(),
									newUser.getUsername(), newUser.getPassword());
						}
					}

				}
			}
		});
		newAcctScreen.setFont(new Font("Tahoma", Font.PLAIN, 14));
		newAcctScreen.setBounds(65, 583, 192, 55);
		contentPane.add(newAcctScreen);
	}
}
