import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class LogIn extends JFrame {

	private JPanel contentPane;
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
					LogIn frame = new LogIn();
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
	public LogIn() 
	{

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel logInLbl = new JLabel("Log In");
		logInLbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
		logInLbl.setHorizontalAlignment(SwingConstants.CENTER);
		logInLbl.setBounds(80, 26, 298, 55);
		contentPane.add(logInLbl);

		//Displays the correct errors when appropriate
		JLabel errorLbl = new JLabel("Password is incorrect.");
		errorLbl.setHorizontalAlignment(SwingConstants.CENTER);
		errorLbl.setForeground(Color.RED);
		errorLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		errorLbl.setBounds(64, 76, 343, 40);
		contentPane.add(errorLbl);
		errorLbl.setVisible(false);

		//Where username is entered
		JLabel usernameLbl = new JLabel("Username: ");
		usernameLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		usernameLbl.setBounds(64, 113, 72, 31);
		contentPane.add(usernameLbl);

		usernameField = new JTextField();
		usernameField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		usernameField.setBounds(64, 154, 343, 35);
		contentPane.add(usernameField);
		usernameField.setColumns(10);

		//Where password is entered
		JLabel passwordLbl = new JLabel("Password:");
		passwordLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		passwordLbl.setBounds(64, 221, 69, 31);
		contentPane.add(passwordLbl);

		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		passwordField.setBounds(64, 262, 343, 35);
		contentPane.add(passwordField);

		//Closes Log In window
		//Takes user back to the Start Menu
		JButton cancelBttn = new JButton("Cancel");
		cancelBttn.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				StartMenu startMenu = new StartMenu();
				startMenu.setVisible(true);
				setVisible(false);
			}
		});
		cancelBttn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cancelBttn.setBounds(262, 327, 145, 55);
		contentPane.add(cancelBttn);

		//If the entered username and password are correct 
		//Logs user in, closes Log In window, opens View Window
		//Otherwise displays appropriate error
		JButton LogInBtn = new JButton("Log In");
		LogInBtn.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				//Error for when the entered username or password is null
				if (usernameField.getText().contentEquals("") || passwordField.getText().contentEquals("")) 
				{
					errorLbl.setVisible(true);
					errorLbl.setText("Please fill in all fields.");
				}
				//Checks user's log in credentials
				else 
				{
					Users cur = new Users(0, null, null, usernameField.getText(), passwordField.getText());
					Users checkUser = cur.getUser(usernameField.getText(), passwordField.getText());
					if (checkUser == null) 
					{
						errorLbl.setVisible(true);
						errorLbl.setText("Username or password is incorrect.");
					} 
					//Logs user in, closes Log In window, opens View Window
					//Otherwise displays appropriate error
					else 
					{
						ViewWindow view = new ViewWindow();
						setVisible(false);
						view.setVisible(true);
						view.run(checkUser.getUserId(), checkUser.getFirstName(), checkUser.getLastName(),
								checkUser.getUsername(), checkUser.getPassword());
					}
				}
			}
		});
		LogInBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		LogInBtn.setBounds(64, 327, 145, 55);
		contentPane.add(LogInBtn);

	}
}
