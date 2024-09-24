import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StartMenu extends JFrame
{

	private JPanel contentPane;

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
					StartMenu frame = new StartMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public StartMenu() 
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel startLabel = new JLabel("LEGO Collection Database");
		startLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		startLabel.setHorizontalAlignment(SwingConstants.CENTER);
		startLabel.setBounds(54, 34, 365, 50);
		contentPane.add(startLabel);
		
		//Takes user to Log In window, closes current window
		JButton logInBttn = new JButton("Log In");
		logInBttn.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				LogIn log = new LogIn(); 
				setVisible(false);
				log.setVisible(true); 
			}
		});
		logInBttn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		logInBttn.setBounds(122, 166, 217, 50);
		contentPane.add(logInBttn);
		
		//Takes user to Create Account Window
		JButton createAccountBttn = new JButton("Create Account");
		createAccountBttn.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				CreateAccount create = new CreateAccount(); 
				setVisible(false);
				create.setVisible(true);
			}
		});
		createAccountBttn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		createAccountBttn.setBounds(122, 259, 217, 50);
		contentPane.add(createAccountBttn);
	}
}
