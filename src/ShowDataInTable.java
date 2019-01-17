
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import net.proteanit.sql.DbUtils;

import java.awt.Color;

import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.DefaultComboBoxModel;

public class ShowDataInTable extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JButton dataFatchButon;
	Connection connection;
	java.sql.Statement statement;
	Font font;
	private JTextField userNameField;
	private JPasswordField passwordField;
	private JComboBox comboBox, searchCatagoryBox;
	private JTextField searchField;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ShowDataInTable frame = new ShowDataInTable();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void refreshTable() {

		try {

			String query = "SELECT * FROM `user_data`";

			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			table.setModel(DbUtils.resultSetToTableModel(resultSet));

		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, e);

		}

		userNameField.setText("");
		passwordField.setText("");

	}

	public void fillComboBox() {

		try {

			String query = "SELECT * FROM `user_data`";

			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				comboBox.addItem(resultSet.getString("User_Name"));

			}

		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, e);

		}

	}

	public void setComboboxEmpty() {

		comboBox.removeAllItems();

	}

	public ShowDataInTable() {

		connection = DatabaseConnectorClass.dbConnection();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setForeground(Color.WHITE);
		contentPane.setBackground(new Color(0, 128, 128));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		font = new Font("Segoe UI", Font.PLAIN, 17);

		comboBox = new JComboBox();
		comboBox.setBounds(213, 342, 176, 36);
		contentPane.add(comboBox);
		comboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {

					String query = "SELECT * FROM user_data WHERE User_Name = ?";

					PreparedStatement preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, (String) comboBox.getSelectedItem());

					ResultSet resultSet = preparedStatement.executeQuery();

					while (resultSet.next()) {

						userNameField.setText(resultSet.getString("User_Name"));
						passwordField.setText(resultSet.getString("Password"));
					}

				} catch (Exception e) {

					JOptionPane.showMessageDialog(null, e);

				}

			}
		});

		dataFatchButon = new JButton("USER DATA");
		dataFatchButon.setForeground(Color.BLACK);
		dataFatchButon.setFont(font);
		dataFatchButon.setBounds(32, 11, 143, 45);
		contentPane.add(dataFatchButon);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(32, 77, 720, 243);
		contentPane.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 17));
		table.setBackground(Color.WHITE);
		table.setRowHeight(23);
		table.setSelectionBackground(Color.ORANGE);

		JButton refrashButton = new JButton("CLEAR");
		refrashButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				DefaultTableModel dtm = (DefaultTableModel) table.getModel();
				dtm.setRowCount(0);

			}
		});
		refrashButton.setForeground(Color.BLACK);
		refrashButton.setFont(new Font("Segoe UI", Font.PLAIN, 17));
		refrashButton.setBounds(191, 11, 99, 45);
		contentPane.add(refrashButton);

		JLabel lblUserName = new JLabel("User Name");
		lblUserName.setForeground(Color.WHITE);
		lblUserName.setFont(new Font("Segoe UI", Font.PLAIN, 19));
		lblUserName.setBounds(32, 401, 99, 50);
		contentPane.add(lblUserName);

		userNameField = new JTextField();
		userNameField.setBounds(142, 401, 341, 50);
		userNameField.setFont(font);
		contentPane.add(userNameField);
		userNameField.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 19));
		lblPassword.setBounds(32, 484, 99, 50);
		contentPane.add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setColumns(10);
		passwordField.setFont(font);
		passwordField.setBounds(142, 484, 341, 54);
		contentPane.add(passwordField);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {

				int selectrow = table.getSelectedRow();

				String userName = table.getValueAt(selectrow, 0).toString();
				String passwor = table.getValueAt(selectrow, 1).toString();

				userNameField.setText(userName);
				passwordField.setText(passwor);

			}
		});

		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {

					if (userNameField.getText().isEmpty() || passwordField.getText().isEmpty()) {

						JOptionPane.showMessageDialog(null, "UserName or Password Field is Empty");

					} else {
						PreparedStatement statement = connection
								.prepareStatement("INSERT INTO user_data VALUES (?, ?)");
						statement.setString(1, userNameField.getText().toString());
						statement.setString(2, passwordField.getText().toString());

						statement.executeUpdate();

						JOptionPane.showMessageDialog(null, "Insered !");
					}

				} catch (Exception e) {

					JOptionPane.showMessageDialog(null, e);

				}
				refreshTable();
				setComboboxEmpty();
				fillComboBox();
			}
		});
		saveButton.setBounds(531, 416, 89, 45);
		contentPane.add(saveButton);

		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (userNameField.getText().isEmpty() && passwordField.getText().isEmpty()) {

					JOptionPane.showMessageDialog(null, "Already Reseted !");

				} else {
					userNameField.setText("");
					passwordField.setText("");
				}
			}
		});
		resetButton.setBounds(648, 416, 89, 45);
		contentPane.add(resetButton);

		JButton updateButton = new JButton("Update");
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				int selectrow = table.getSelectedRow();

				if (selectrow >= 0) {

					String string = (String) table.getValueAt(selectrow, 0);

					try {

						String queryUpdate = "update user_data set User_Name = '" + userNameField.getText().toString()
								+ "', Password = '" + passwordField.getText().toString() + "' where User_Name = '"
								+ string + "'";

						statement = connection.createStatement();
						statement.executeLargeUpdate(queryUpdate);

						JOptionPane.showMessageDialog(null, "Successfully Updated !");

					} catch (Exception e) {

						JOptionPane.showMessageDialog(null, "Update Fail !");
					}

				} else {
					JOptionPane.showMessageDialog(null, "You Didn't Select Any Row");
				}
				refreshTable();
				setComboboxEmpty();
				fillComboBox();
			}
		});
		updateButton.setBounds(531, 483, 89, 45);
		contentPane.add(updateButton);

		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				int selectrow = table.getSelectedRow();

				if (selectrow >= 0) {

					String string = (String) table.getValueAt(selectrow, 0);

					String deleteQuery = "DELETE FROM `user_data` WHERE User_Name = '" + string + "'";

					try {

						statement = connection.createStatement();
						statement.executeLargeUpdate(deleteQuery);

					} catch (SQLException e) {

						JOptionPane.showMessageDialog(null, "Data Not Deleted !");
						e.printStackTrace();

					}

				} else {
					JOptionPane.showMessageDialog(null, "You Didn't Select Any Row");
				}
				refreshTable();
				setComboboxEmpty();
				fillComboBox();
			}
		});
		deleteButton.setBounds(648, 483, 89, 45);
		contentPane.add(deleteButton);

		JButton logoutButton = new JButton("LOGOUT");
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				dispose();

				LoginWindow loginWindow = new LoginWindow();
				loginWindow.main(null);

			}
		});
		logoutButton.setBounds(663, 11, 89, 45);
		contentPane.add(logoutButton);
		
		searchCatagoryBox = new JComboBox();
		searchCatagoryBox.setModel(new DefaultComboBoxModel(new String[] {"User_Name", "Password"}));
		searchCatagoryBox.setFont(new Font("Segoe UI", Font.PLAIN, 17));
		searchCatagoryBox.setBounds(323, 11, 126, 45);
		contentPane.add(searchCatagoryBox);

		searchField = new JTextField();
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {

				try {
					
					String getSelectionCatagory = searchCatagoryBox.getSelectedItem().toString();
					
					String query = "SELECT * FROM user_data WHERE " + getSelectionCatagory + " = ?";

					PreparedStatement preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, searchField.getText());

					ResultSet resultSet = preparedStatement.executeQuery();

					table.setModel(DbUtils.resultSetToTableModel(resultSet));

				} catch (Exception e) {

					JOptionPane.showMessageDialog(null, e);

				}

			}
		});
		searchField.setFont(new Font("Segoe UI", Font.PLAIN, 17));
		searchField.setBounds(471, 11, 169, 45);
		contentPane.add(searchField);
		searchField.setColumns(10);

		dataFatchButon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {

					String query = "SELECT * FROM `user_data`";

					statement = connection.createStatement();
					ResultSet resultSet = statement.executeQuery(query);

					table.setModel(DbUtils.resultSetToTableModel(resultSet));

				} catch (Exception e) {

					JOptionPane.showMessageDialog(null, e);

				}

			}
		});

		fillComboBox();
	}
}
