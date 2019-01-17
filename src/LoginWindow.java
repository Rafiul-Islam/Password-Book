import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.JButton;

public class LoginWindow {

	private JFrame frame;

	Connection connection;
	private JTextField userNameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private Font font;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWindow window = new LoginWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public LoginWindow() {
		initialize();
		connection = DatabaseConnectorClass.dbConnection();
	}

	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(0, 128, 128));
		frame.setBounds(100, 100, 800, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		font = new Font("Segoe UI", Font.PLAIN, 17);

		JLabel lblLogin = new JLabel("SIGN IN");
		lblLogin.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		lblLogin.setForeground(Color.WHITE);
		lblLogin.setBounds(354, 66, 101, 47);
		frame.getContentPane().add(lblLogin);

		JLabel lblUsername = new JLabel("USER NAME");
		lblUsername.setForeground(Color.WHITE);
		lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		lblUsername.setBounds(130, 143, 128, 45);
		frame.getContentPane().add(lblUsername);

		userNameField = new JTextField();
		userNameField.setBounds(269, 143, 286, 45);
		userNameField.setFont(font);
		frame.getContentPane().add(userNameField);
		userNameField.setColumns(10);

		JLabel lblPassword = new JLabel("PASSWORD");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		lblPassword.setBounds(130, 212, 128, 45);
		frame.getContentPane().add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setColumns(10);
		passwordField.setBounds(269, 212, 286, 45);
		passwordField.setFont(font);
		frame.getContentPane().add(passwordField);

		loginButton = new JButton("LOGIN");
		loginButton.setBounds(342, 286, 117, 47);
		frame.getContentPane().add(loginButton);

		loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String username = userNameField.getText().toString();
				String password = passwordField.getText().toString();

				try {

					String query = "SELECT * FROM `user_data` WHERE User_Name = ? and Password = ?";

					PreparedStatement preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, username);
					preparedStatement.setString(2, password);

					ResultSet resultSet = preparedStatement.executeQuery();

					int count = 0;

					while (resultSet.next()) {
						count++;
					}

					if (count == 1) {

						JOptionPane.showMessageDialog(null, "Login Successful !");
						frame.dispose();
						
						ShowDataInTable dataInTable = new ShowDataInTable();
						dataInTable.setVisible(true);
						dataInTable.setBounds(100, 100, 800, 600);
						dataInTable.setLocationRelativeTo(null);
						dataInTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

					} else if (count > 1) {

						JOptionPane.showMessageDialog(null, "Duplicate UserName and Password !");

					} else {
						JOptionPane.showMessageDialog(null, "Invalid UserName or Password !");
					}

				} catch (Exception event) {

					JOptionPane.showMessageDialog(null, "ERROR !");

				}

			}

		});

	}

}
