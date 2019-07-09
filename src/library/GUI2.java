package library;

import java.awt.*;
import java.sql.*;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.event.*;

@SuppressWarnings("serial")
class GUI2 extends JFrame implements ActionListener{
	/**
	 * This is the first Page that shows after a successful login by a teacher or student. The User can then click on the appropriate buttons to get to the required functionalities.
	 * 
	 */
	JButton btn1, btn2, btn3, btn4, btn5, btn6;
	JLabel lb_welcome, lb1, cred_lbl, o,n;
	JPasswordField od,nw;
	Image image = new ImageIcon(this.getClass().getResource("/resources/icon.png")).getImage();
	Component temp;
	Statement stmt;
	Connection conn;
	static int i = 0, clkd = 0;
	ResultSet rs;
	String tmp;
	String login_id;
	
	GUI2(Connection conn, String tx){
		super("Library-Management System");
		setSize(1000, 500);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		setIconImage(image);
		
		this.conn = conn;
		this.tmp = tx;
		login_id=tx;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		 	od = new JPasswordField(32);
	        od.setAlignmentX(Component.CENTER_ALIGNMENT);
	        od.setMaximumSize(new Dimension(200, 20));
	        
	        nw = new JPasswordField(32);
	        nw.setAlignmentX(Component.CENTER_ALIGNMENT);
	        nw.setMaximumSize(new Dimension(200, 20));
		
		cred_lbl = new JLabel("Developed by: Shashank Shirol and Rajat Shenoy");
		cred_lbl.setFont(new Font("Sans", Font.PLAIN, 15));
		cred_lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		
		
		//FETCHING NAME FOR GREETING
		try {
				rs = stmt.executeQuery("with temp_tab(lib_id, name) as ((select lib_id, name from student) union (select lib_id, name from teacher)) select name from temp_tab where lib_id = " + tmp);
				rs.next();
				tmp = rs.getObject(1).toString();
        	}
        	catch(SQLException ee) {
        		tmp = "User";
        	}
		
		
		
		
		
		
		lb1 = new JLabel("Welcome, "+tmp);
		lb1.setFont(new Font("Monospaced", Font.BOLD, 26));
		lb1.setAlignmentX(Component.CENTER_ALIGNMENT);
		lb1.setBorder(new EmptyBorder(30, 0, 40, 0));
		
		o = new JLabel("Current Password: ");
		o.setFont(new Font("Sans", Font.BOLD, 9));
		o.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		n = new JLabel("New Password: ");
		n.setFont(new Font("Sans", Font.BOLD, 9));
		n.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		btn2 = new JButton("Update Password");
		btn2.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		
		btn1 = new JButton("Browse Books");
		btn1.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		btn3 = new JButton("Change Password");
		btn3.setAlignmentX(Component.CENTER_ALIGNMENT);
		Dimension d = btn3.getMaximumSize();
		btn1.setMaximumSize(new Dimension(d));
		btn2.setMaximumSize(new Dimension(d));
		
		btn4 = new JButton("Logout");
		btn4.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn4.setMaximumSize(new Dimension(d));
		
		btn5 = new JButton("Exit");
		btn5.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn5.setMaximumSize(new Dimension(d));
		
		btn6 = new JButton("Books Issued");
		btn6.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn6.setMaximumSize(new Dimension(d));
		
		btn1.setActionCommand("btn1");
		btn2.setActionCommand("btn6"); //changes done here
		btn3.setActionCommand("btn3");
		btn4.setActionCommand("btn4");
		btn5.setActionCommand("btn5");
		btn6.setActionCommand("btn2");
		
		btn1.addActionListener(this);
		btn2.addActionListener(this);
		btn3.addActionListener(this);
		btn4.addActionListener(this);
		btn5.addActionListener(this);
		btn6.addActionListener(this);
		
		temp = Box.createRigidArea(new Dimension(0, 10));
		
		add(lb1);
		add(btn1);
		add(Box.createRigidArea(new Dimension(0,10)));
		add(btn6);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(btn3);
		add(o);
		add(od);
		add(n);
		add(nw);
		add(temp);
		add(btn2);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(btn4);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(btn5);
		add(Box.createVerticalGlue());
		add(cred_lbl);
		
		getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.darkGray));
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		o.setVisible(false); n.setVisible(false); od.setVisible(false); nw.setVisible(false);
		btn2.setVisible(false);
		temp.setVisible(false);
		setVisible(true);
	}
	
	
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("btn1")){
			new Book(this, 2, stmt, login_id).setVisible(true);
			setVisible(false);
		}
		else if (e.getActionCommand().contentEquals("btn2")){
			new Issue(this,1, stmt, login_id).setVisible(true);
			setVisible(false);
		}
		else if (e.getActionCommand().equals("btn3")){
			if(clkd==0) {
			o.setVisible(true); n.setVisible(true); od.setVisible(true); nw.setVisible(true);
			btn2.setVisible(true); btn3.setText("Cancel");
			temp.setVisible(true);
			clkd=1;
			
			
			
			}
			else {
				o.setVisible(false); n.setVisible(false); od.setVisible(false); nw.setVisible(false);
				temp.setVisible(false);
				btn2.setVisible(false); clkd=0; btn3.setText("Change Password");
			}
			
		}
		else if (e.getActionCommand().equals("btn6")){
			o.setVisible(false); n.setVisible(false); od.setVisible(false); nw.setVisible(false);
			btn2.setVisible(false); clkd=0; btn3.setText("Change Password");
			temp.setVisible(false);
			
			//Checking for correct password
			try {
				rs = stmt.executeQuery("select password from login where lib_id="+login_id);
				if(!rs.next()) JOptionPane.showMessageDialog(null,"Invalid Password", "Alert!", JOptionPane.INFORMATION_MESSAGE);
				else {
					String chk = rs.getString(1);
					if(Arrays.equals(chk.toCharArray(), od.getPassword())){
						System.out.println("Update pass");
						String np = String.copyValueOf(nw.getPassword());
						stmt.executeUpdate("UPDATE LOGIN SET PASSWORD='"+np+"' where lib_id="+login_id);
						JOptionPane.showMessageDialog(null,"Password Updated Successfully!", "Alert!", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
			catch(SQLException eee) {
				JOptionPane.showMessageDialog(null,"Fatal Error Encountered.", "Alert!", JOptionPane.INFORMATION_MESSAGE);
			}
			
		}
		else if (e.getActionCommand().equals("btn4")){
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			new Login();
			dispose();
		}
		else {
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.exit(0);
		}
	}
}
