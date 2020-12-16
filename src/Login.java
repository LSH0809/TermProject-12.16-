import java.awt.Button;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;




public class Login implements Runnable {

	public static final int DEFAULT = 0; //기본 화면
	public static final int LOGINCHECK = 1; 
	public static final int CREATEACCOUNT = 2;
	public static final int SAMECHECK = 3;
	public static final int EDIT = 4;
	public static final int DELETE = 5;
	public static final int CHAT = 7; //챗 하는 방 
	public static final int AFTERLOGIN = 8;//로그인하면 이 부분이 나옴
	public static final int CHATMODE = 9;
		
	public static String ID;
	public static String temp_ID;
	public static String flag;
	public String username;
	String name;
	String passwd;
	String email;
	String birthdate;
	String namecheck;
	int mode = DEFAULT;
	int check = 0;
	int checkboxnum;

	//add list 부분
	List memberList = new List();
	

	static 	String url = "jdbc:mysql://localhost/chat?&allowPublicKeyRetrieval=true&useSSL=false&&serverTimezone=UTC&&useSSL=false&user=root&password=Skyey98081!";
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	ResultSet tempOn = null;
	ResultSet tempOff = null;
	Scanner sc = new Scanner(System.in);

	JFrame frame = new JFrame("Login Program");
	JPanel mainPanel = new JPanel();
	JLabel mainUserLabel = new JLabel("ID");
	JTextField mainUserField = new JTextField("");
	JLabel pwLabel = new JLabel("Password");
	JPasswordField pwField = new JPasswordField();
	JButton loginBtn = new JButton("Log in");
	JButton createBtn =	new JButton("Create Account");
	
	//여기 내가 작성하는 부분
	JFrame stateFrame = new JFrame("State Program");
	JPanel statePanel = new JPanel();
	//JButton stateChatBtn = new JButton("Chat");
	//JButton stateEditBtn = new JButton("Edit");
	JLabel afterModeLabel = new JLabel("Choose");
	JButton [] stateBtn = new JButton[2];
	
	//여기도 내가 작성
	JFrame chatFrame = new JFrame("Chat Program");
	JPanel chatPanel = new JPanel();
	

	JFrame warnFrame = new JFrame();
	JLabel warnLabel = new JLabel();
	JButton warnBtn = new JButton("Got it");

	JFrame askFrame = new JFrame();
	JLabel askLabel = new JLabel();
	JButton askBtn1 = new JButton("Yes");
	JButton askBtn2 = new JButton("No");

	JFrame createFrame = new JFrame("Create Account");
	JPanel createPanel = new JPanel();

	JLabel [] newLabel = new JLabel[6];
	JTextField [] newField = new JTextField[6];

	JLabel [] userLabel = new JLabel[6];
	JTextField [] userField = new JTextField[6];
	JButton [] userBtn = new JButton[3];
	JLabel userModeLabel = new JLabel("UserMode");
	JLabel changeInfoLabel = new JLabel("Change Info");

	JButton [] newBtn = new JButton[3];


	String checkboxlist[] = new String[100];

	//여기 추가한 부분
	

	JPanel userPanel = new JPanel();
	JLabel changeLabel = new JLabel("Change your Info");

	JButton logoutbtn = new JButton("Log out");
	JButton deletebtn = new JButton("Delete(checked)");
	
	JFrame editFrame = new JFrame("Change data");
	JLabel editLabel = new JLabel("Change to what?");
	JTextField editText = new JTextField();
	JButton editBtn = new JButton("Okay");
	
	//exit button
	JButton exitBtn = new JButton("Exit");
	JButton chatRoomButton = new JButton("Chat Room"); 
	
	
	JScrollPane jScrollPane;
	JTable table;
	JCheckBox checkbox[];

	Dimension dim_Frame = new Dimension(400,600);
	Dimension dim_Label = new Dimension(300,30);
	Dimension dim_Field = new Dimension(300,40);
	Dimension dim_Button = new Dimension(300,70);
	Dimension dim_SButton = new Dimension(150,70);



	public Login() {
		
		frame.setVisible(true);
		frame.setSize(dim_Frame);
		frame.setLocation(0, 0);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainPanel.setLayout(null);
		mainUserLabel.setLocation(50, 10);
		mainUserLabel.setSize(dim_Label);
		mainUserField.setLocation(50, 40);
		mainUserField.setSize(dim_Field);
		pwLabel.setSize(dim_Label);
		pwLabel.setLocation(50, 110);
		pwField.setSize(dim_Field);
		pwField.setLocation(50, 140);
		loginBtn.setSize(dim_Button);
		loginBtn.setLocation(50, 210);
		createBtn.setSize(dim_Button);
		createBtn.setLocation(50, 280);
		exitBtn.setSize(400,30);
		exitBtn.setLocation(0,530);
		
		statePanel.setLayout(null);

		loginBtn.addActionListener(new ButtonAction());
		createBtn.addActionListener(new ButtonAction());
		exitBtn.addActionListener(new ButtonAction());
		chatRoomButton.addActionListener(new ButtonAction());
		
		mainPanel.setFont(new Font("Arial", Font.BOLD, 20));
		statePanel.setFont(new Font("Arial",Font.BOLD,20));

		frame.add(mainPanel);//초기상태인것 같아
		
		
		mainPanel.add(mainUserLabel);
		mainPanel.add(mainUserField);
		mainPanel.add(pwLabel);
		mainPanel.add(pwField);
		mainPanel.add(loginBtn);
		mainPanel.add(createBtn);
		mainPanel.add(exitBtn);
		
		//statePanel.add(stateChatBtn);
		
		newLabel[0] = new JLabel("ID");
		newField[0] = new JTextField();
		newLabel[1] = new JLabel("Password(should be longer than 7)");
		newField[1] = new JTextField();
		newLabel[2] = new JLabel("Rewrite Password");
		newField[2] = new JTextField();
		newLabel[3] = new JLabel("Name");
		newField[3] = new JTextField();
		newLabel[4] = new JLabel("Birth Date");
		newField[4] = new JTextField();
		newLabel[5] = new JLabel("E-mail");
		newField[5] = new JTextField();
		newBtn[0] = new JButton("Save");
		newBtn[1] = new JButton("Cancel");
		newBtn[2] = new JButton("중복확인");

		userLabel[0] = new JLabel("Current Password");
		userField[0] = new JTextField();
		userLabel[1] = new JLabel("New Password");
		userField[1] = new JTextField();
		userLabel[2] = new JLabel("Re-Password");
		userField[2] = new JTextField();
		userLabel[3] = new JLabel("Name");
		userField[3] = new JTextField();
		userLabel[4] = new JLabel("Birth Date");
		userField[4] = new JTextField();
		userLabel[5] = new JLabel("E-mail");
		userField[5] = new JTextField();
		userBtn[0] = new JButton("Edit");
		userBtn[1] = new JButton("Cancel");
		userBtn[2] = new JButton("Delete Account");
		
		//이 부분 내가 작성중
		stateBtn[0] = new JButton("Edit Profile");
		stateBtn[1] = new JButton("Chat");

		warnFrame.setSize(400, 200);
		warnFrame.setLocation(0, 0);
		warnFrame.setLayout(null);
		warnFrame.setResizable(false);
		warnFrame.setVisible(false);
		
		warnLabel.setLocation(10, 10);
		warnLabel.setSize(380, 100);
		warnLabel.setFont(new Font("Arial", Font.PLAIN, 15));

		warnBtn.setSize(100, 50);
		warnBtn.setLocation(150, 100);
		warnBtn.addActionListener(new ButtonAction());
		
		
		warnFrame.add(warnLabel);
		warnFrame.add(warnBtn);

		askFrame.setSize(400, 300);
		askFrame.setLocation(600, 500);
		askFrame.setResizable(false);
		askFrame.setVisible(false);
		askFrame.setLayout(null);
		askFrame.add(askLabel);
		askFrame.add(askBtn1);
		askFrame.add(askBtn2);

		
		editFrame.setSize(200, 300);
		editFrame.setLocation(200, 500);
		editFrame.setResizable(false);
		editFrame.setVisible(false);
		editFrame.setLayout(null);
		
		
		editLabel.setSize(150, 50);
		editLabel.setLocation(25, 0);
		
		editText.setSize(150, 50);
		editText.setLocation(25, 50);
		
		editBtn.setSize(100, 50);
		editBtn.setLocation(50, 100);
		editBtn.addActionListener(new ButtonAction());
		
		editFrame.add(editLabel);
		editFrame.add(editText);
		editFrame.add(editBtn);
		
		
		askLabel.setText("Is it okay to delete?");
		askLabel.setLocation(50,10);
		askLabel.setSize(300, 50);
		askBtn1.addActionListener(new ButtonAction());
		askBtn2.addActionListener(new ButtonAction());
		askBtn1.setLocation(50, 60);
		askBtn1.setSize(dim_SButton);
		askBtn2.setLocation(200, 60);
		askBtn2.setSize(dim_SButton);

	}

	class ButtonAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JButton myButton = (JButton)e.getSource();
			String temp = myButton.getText();

			warnFrame.setVisible(false);


			if(temp.equals("Log in")) {
				username = mainUserField.getText();
				passwd = new String(pwField.getPassword()); 
				mainUserField.setText("");
				pwField.setText("");

				mode = AFTERLOGIN;
				System.out.println("Login 정보 " + username + " " + passwd);
				try {
					command();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else if(temp.contentEquals("Exit")) {
				flag ="off";
				String Update = "update user set access = '" + flag + "' where username = '"+ ID+"' ; ";
				try {
					stmt.executeUpdate(Update);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.exit(0);
			}
			else if(temp.equals("Edit Profile")) {
				username = mainUserField.getText();
				passwd = new String(pwField.getPassword()); 
				mainUserField.setText("");
				pwField.setText("");
				mode = EDIT;
				try {
					command();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else if(temp.equals("Chat")) {
				mode = CHATMODE;
				try {
					command();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else if(temp.equals("Create Account")) {
				mode = CREATEACCOUNT;
				createPanel.setLayout(null);
				frame.remove(mainPanel);
				frame.add(createPanel);
				frame.revalidate();
				frame.repaint();

				int x = 50, y = 10;

				for(int i =  0; i< newLabel.length ; i++) {
					newLabel[i].setLocation(x, y);
					newLabel[i].setSize(dim_Label);
					createPanel.add(newLabel[i]);
					y += 30;
					newField[i].setLocation(x, y);
					newField[i].setSize(dim_Field);
					createPanel.add(newField[i]);
					y += 40;
					if(i==0) y += 50; 

				}

				for(int i = 0; i < newBtn.length -1 ; i++) {
					newBtn[i].setLocation(x, y);
					newBtn[i].setSize(dim_SButton);
					newBtn[i].addActionListener(this);
					createPanel.add(newBtn[i]);
					x += 150;
				}
				newBtn[2].setLocation(230, 80);
				newBtn[2].setSize(120, 50);
				newBtn[2].addActionListener(this);
				createPanel.add(newBtn[2]);

			}

			else if(temp.equals("Save")) {				
				int admission = 1;
				String warnMessage = "";

				if(check == 0) {
					warnMessage = "Check your username for redunduncy.";
					admission = 0;
				}
				else if(!newField[1].getText().equals(newField[2].getText()) ) {
					warnMessage = "Rewrite Password.";
					admission = 0;
				}
				else if(newField[1].getText().length() < 8) {
					warnMessage = "password too short.";
					admission = 0;

				}

				else {
					for(int i = 0; i < newField.length; i++) {
						if(newField[i].getText().length()==0) {
							warnMessage = warnMessage + " " + newField[i].getText(); 
							admission = 0;

						}
					}
					if(admission == 0) warnMessage = "All data should be filled.";
				}
				if(admission == 0) {
					warnLabel.setText(warnMessage);
					warnFrame.setVisible(true);
				}
				else {
					System.out.println("new account added");
					username = newField[0].getText();
					passwd = newField[1].getText();
					name = newField[3].getText();
					birthdate = newField[4].getText();
					email = newField[5].getText();

					for(int i = 0; i < newField.length; i++) {
						newField[i].setText("");
					}
					try {
						command();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					warnLabel.setText("Account created! Login with your Account!");
					warnFrame.setVisible(true);
					frame.remove(createPanel);
					frame.add(mainPanel);
					frame.revalidate();
					frame.repaint();
					mode = DEFAULT;
					check = 0;
				}
			}

			else if(temp.equals("Cancel")) {
				frame.remove(createPanel);
				frame.remove(userPanel);
				frame.add(mainPanel);
				frame.revalidate();
				frame.repaint();
				for(int i = 0; i < newField.length; i++) {
					newField[i].setText("");
				}
				for(int i = 0; i < userField.length; i++) {
					userField[i].setText("");
				}
				flag ="off";
				String Update = "update user set access = '" + flag + "' where username = '"+ ID+"' ; ";
				try {
					stmt.executeUpdate(Update);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mode = DEFAULT;
			}
			else if(temp.equals("중복확인")) {
				
				mode = SAMECHECK;
				namecheck = newField[0].getText();
				try {
					command();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(check == 0) {
					warnLabel.setText("Username exists! Try different username!");
					warnFrame.setVisible(true);
				}
				else {
					warnLabel.setText("Good! You can use this username!");
					warnFrame.setVisible(true);
				}
				mode = CREATEACCOUNT;
			}

			else if(temp.equals("Edit")) {
				int admission = 1;
				String warnMessage = "";

				if(!userField[0].getText().equals(passwd)) {
					warnMessage = "Current Password is wrong.";
					admission = 0;
				}
				else if(!userField[1].getText().equals(userField[2].getText()) ) {
					warnMessage = "Correctly rewrite Password.";
					admission = 0;
				}
				else if(userField[2].getText().length() < 8) {
					warnMessage = "password too short.";
					admission = 0;

				}

				else {
					for(int i = 1; i < userField.length; i++) {
						if(userField[i].getText().length()==0) {
							warnMessage = warnMessage + " " + userField[i].getText(); 
							admission = 0;

						}
					}
					if(admission == 0) warnMessage = warnMessage + "All should be filled.";
				}

				if(admission == 0) {
					warnLabel.setText(warnMessage);
					warnFrame.setVisible(true);
				}
				else {
					System.out.println("new account added");
					passwd = userField[1].getText();
					name = userField[3].getText();
					birthdate = userField[4].getText();
					email = userField[5].getText();
					
					mode = EDIT;
					try {
						command();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					warnLabel.setText("Account edited! Login with your Account!");
					warnFrame.setVisible(true);
					for(int i = 0; i < userField.length; i++) {
						userField[i].setText("");
					}
					frame.remove(createPanel);
					frame.remove(userPanel);
					frame.add(mainPanel);
					frame.revalidate();
					frame.repaint();
					mode = DEFAULT;
					check = 0;
				}
			}
			else if(temp.equals("Delete Account")){
				mode = DELETE;
				askFrame.setVisible(true);
				

			}
			else if(temp.equals("Yes")) {
				
				askFrame.setVisible(false);
				frame.remove(userPanel);
				frame.remove(createPanel);
				frame.add(mainPanel);
				frame.revalidate();
				frame.repaint();

				try {
					command();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else if(temp.equals("No")) {
				askFrame.setVisible(false);
			}
			
			else if(temp.equals("Delete(checked)")){
		
				askFrame.setVisible(true);
				
			}
			else if(temp.equals("Log out")) {
			
				frame.setVisible(true);
				frame.remove(mainPanel);
				frame.remove(createPanel);
				frame.remove(userPanel);
				frame.add(mainPanel);
				
				frame.revalidate();
				frame.repaint();
			}
			else if(temp.equals("Okay")){
				editFrame.setVisible(false);
				ID = editText.getText();
				editText.setText("");
		
				try {
					command();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else if(temp.equals("Got it")){
				warnFrame.setVisible(false);
			}
			else if(temp.equals("Chat Room")) {
				mode = CHAT;
				chat();
			}
		}
	}

	public void command()throws Exception {
	
		try {
				
			Class.forName("com.mysql.cj.jdbc.Driver"); // JDBC 드라이버 로드
			System.out.println("드라이버 연결 성공!" + mode);

			conn = DriverManager.getConnection(url);
			System.out.println("데이터베이스 연결 성공!");
			
			//여기 User class 관리하는 부분
			stmt = conn.createStatement();


			String useXproject = "use chat";
			stmt.executeUpdate(useXproject);
			
			ResultSet list = null;
			if(mode == LOGINCHECK) {
								
				temp_ID = "username"; ID = username;
				list = stmt.executeQuery("select username from user;");
				while(list.next()) {
					String member = list.getNString(1);
					memberList.add(member);
				}
				usermode();
			}
			
			
			
			else if(mode == AFTERLOGIN) {
				temp_ID = "username";
				ID = username;
				flag = "on";
				//username으로 검색하기
				String search = "select * from user where " + temp_ID + " like '" + ID +"';";
				//username으로 받은 데이터가 rs에 저장되고 그 내용을 확인!
				rs = stmt.executeQuery(search);
				
				
				if(rs.next()) {
					if(passwd.equals(rs.getString("passwd"))){
						System.out.println("login succeeded");
						String Update = "update user set access = '" + flag + "' where username = '"+ ID+"' ; ";
						stmt.executeUpdate(Update);
						afterLoginMode();
					}
					else {
						System.out.println("login failed");
						warnFrame.setVisible(true);
						warnLabel.setText("Password is wrong.");
					}
				}
				else {
					System.out.println("login failed");
					warnFrame.setVisible(true);
					warnLabel.setText("ID does not exist.");
				}
				
			}
			else if(mode ==CHATMODE) {
				chatMode();
			}

			else if(mode == CREATEACCOUNT) {
				
				String adduser = "insert into user(username, passwd, name, birthdate, email) "
						+ "values('"+ username + "', '" + passwd + "', '"
						+ name + "', '" + birthdate + "', '" + email + "');";
				
				System.out.println(adduser);
				stmt.executeUpdate(adduser);
			}
			else if(mode == SAMECHECK) {
				
				
				String search = "select * from user where username like '" + namecheck +"';";
				rs = stmt.executeQuery(search);
				if(rs.next()) {
					System.out.println(rs.getString("username"));
					check = 0;
				}
				else {
					System.out.println("check changed");
					check = 1;
				}
			}
			else if(mode == EDIT) {
				String Update = "update user set passwd = '" + passwd 
						+ "' where username = '"+ username+"' ; ";
				stmt.executeUpdate(Update);
				System.out.println(Update);

				Update = "update user set name = '" + name + "' where username = '"+ username+"' ; ";
				stmt.executeUpdate(Update);
				System.out.println(Update);

				Update = "update user set email = '" + email + "' where username = '"+ username+"' ; ";
				stmt.executeUpdate(Update);
				System.out.println(Update);

				Update = "update user set birthdate = '" + birthdate + "' where username = '"
						+ username +"' ; ";
				stmt.executeUpdate(Update);
				System.out.println(Update);
				usermode();
			}
			else if(mode == DELETE) {
				String Delete = "delete from user where username = '" + ID + "';";
				System.out.println(Delete);
				stmt.executeUpdate(Delete);
			}
			

		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch(SQLException e) {
			e.printStackTrace(); 
		}
	}

	public void usermode() {
		frame.remove(mainPanel);
		frame.remove(createPanel);
		frame.remove(statePanel);
		frame.remove(chatPanel);
		frame.add(userPanel);
		frame.revalidate();
		frame.repaint();

		int x = 50, y = 10;


		userModeLabel.setLocation(x, y);
		userModeLabel.setSize(dim_Label);
		userPanel.add(userModeLabel);
		y += 30;
		changeInfoLabel.setSize(dim_Label);
		changeInfoLabel.setLocation(x, y);
		userPanel.add(changeInfoLabel);
		y += 30;

		for(int i =  0; i< userLabel.length ; i++) {
			userLabel[i].setLocation(x, y);
			userLabel[i].setSize(dim_Label);
			userPanel.add(userLabel[i]);
			y += 30;
			userField[i].setLocation(x, y);
			userField[i].setSize(dim_Field);
			userPanel.add(userField[i]);
			y += 40;


		}

		for(int i = 0; i < userBtn.length-1 ; i++) {
			userBtn[i].setLocation(x, y);
			userBtn[i].setSize(150,30);
			userBtn[i].addActionListener(new ButtonAction());
			userPanel.add(userBtn[i]);
			x += 150;
		}
		userBtn[2].setLocation(200, 10);
		userBtn[2].setSize(dim_SButton);
		userBtn[2].addActionListener(new ButtonAction());
		userPanel.add(userBtn[2]);
		
	
	}
	
	public void afterLoginMode() {
		frame.remove(mainPanel);
		frame.remove(createPanel);
		frame.remove(userPanel);
		frame.remove(chatPanel);
		frame.add(statePanel);
		frame.revalidate();
		frame.repaint();

		int x = 50, y = 10;
		afterModeLabel.setLocation(x, y);
		afterModeLabel.setSize(dim_Label);
		statePanel.add(afterModeLabel);
		y += 30;
		
		stateBtn[0].setSize(dim_Button);;
		stateBtn[0].setLocation(50,210);
		stateBtn[1].setSize(dim_Button);
		stateBtn[1].setLocation(50,350);
		stateBtn[0].addActionListener(new ButtonAction());
		stateBtn[1].addActionListener(new ButtonAction());
		statePanel.add(stateBtn[0]);
		statePanel.add(stateBtn[1]);
		statePanel.add(exitBtn);
		//0이 edit profile 이고 1이 chat 버튼 
	}
	
	public void chatMode() {
		frame.remove(mainPanel);
		frame.remove(createPanel);
		frame.remove(userPanel);
		frame.remove(statePanel);
		frame.add(chatPanel);
		frame.revalidate();
		frame.repaint();
		
		//여기 복붙
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 400, 600);
		
		List memberList = new List();
		
		
		try {
			Connection con = null;
			java.sql.Statement st = null;
			ResultSet result = null;
       
			con = DriverManager.getConnection("jdbc:mysql://localhost/chat?&allowPublicKeyRetrieval=true&useSSL=false&&serverTimezone=UTC&&useSSL=false&user=root&password=Skyey98081!");
			st = con.createStatement();
			st.execute("use chat;");
			result = st.executeQuery("select username from user where access= 'on';");
			while (result.next()){
			    String str = result.getNString(1);
			    memberList.add(str); // 리스트에 데이터를 추가한다.
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		   // 결과를 하나씩 출력한다.
		
		  chatPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		  chatPanel.setLayout(null);
		  
		  JPanel panel = new JPanel();
		  panel.add(memberList);
		  
		  
		  panel.setBounds(0, 0, 400, 550); 
		  chatPanel.add(panel); 
		  panel.setLayout(null);
		  
		  JTextField textField = new JTextField(); textField.setBounds(20, 50, 74, 21);
		  textField.setEditable(false);
		  textField.setFont(new Font("굴림", Font.BOLD, 12)); textField.setText("채팅목록");
		  panel.add(textField); textField.setColumns(10);
		  
		  
		  Frame listFrame = new Frame(); listFrame.setLayout(null);
		  listFrame.setSize(300,300); memberList.setBounds(20,80,350,400);
		  memberList.setFont(new Font("굴림",Font.BOLD,15));
		  

		  
		  JButton btnNewButton = new JButton("Chat"); btnNewButton.setBounds(22, 505,
		  97, 23); panel.add(btnNewButton);
		  
		  chatRoomButton.setBounds(138, 505, 97, 23);
		  panel.add(chatRoomButton);
		  
		  panel.add(exitBtn);
		 
		 
	}
	
	public void chat() {
		Socket socket = null;            //Server와 통신하기 위한 Socket
        DataInputStream in = null;        //Server로부터 데이터를 읽어들이기 위한 입력스트림
        BufferedReader in2 = null;        //키보드로부터 읽어들이기 위한 입력스트림

        DataOutputStream out = null;   
        String str2 = "";
        
        try {
        	
            socket = new Socket("localhost",9999);    //서버로 접속
            
            in = new DataInputStream(socket.getInputStream());            
            in2 = new BufferedReader(new InputStreamReader(System.in)); 
            out = new DataOutputStream(socket.getOutputStream());        

            //채팅에 사용 할 닉네임을 입력받음
            System.out.print("닉네임을 입력해주세요 : ");
            String data = in2.readLine();            
            
            //서버로 닉네임을 전송
            out.writeUTF(data);               
            //사용자가 채팅 내용을 입력 및 서버로 전송하기 위한 쓰레드 생성 및 시작
            Thread th = new Thread(new Send(out));
            th.start();
        }catch(IOException e) {}
        try {
            //클라이언트의 메인 쓰레드는 서버로부터 데이터 읽어들이는 것만 반복.
            while(true)
            {
                str2 = in.readUTF();        
                System.out.println(str2);
            }
        }catch(IOException e) {}
        try {
        	out.writeUTF(str2);
     
        }catch(Exception e) {}
    }
	
	
	
	  public void run() {//implements Login class in the client.java 
		  try{
	  
	  }catch(Exception e) {}
		  
		 
	  }
	 
	
}