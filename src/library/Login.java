package library;

import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



@SuppressWarnings("serial")
class Login extends JFrame implements ActionListener {
    JTextField t1;
    JPasswordField pass;
    JLabel l1,l2;
    JButton go,b2;
    Image image = new ImageIcon(this.getClass().getResource("/resources/icon.png")).getImage();
    String User, Pass;
    Connection conn;
    
    Login(){
        super("Library-Management System - LOGIN");
        setSize(500, 270);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        setIconImage(image);
        /*
         *  Setting up the connection to the database
         *  Enter USER and PASS in their respective strings before running the code.
         *  
         *  create a new table called login and values added are 1. 1234 - qwerty
         *  													 2. 1678 - poiuzt
         */
        
        User = "system";
        Pass = "11215145";
        
        try {
        	Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", User, Pass);
			conn.setAutoCommit(true);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch(ClassNotFoundException ex) {
			
			ex.printStackTrace();
		}
        
        
        
        
        t1 = new JTextField(20);
        t1.setAlignmentX(Component.CENTER_ALIGNMENT);
        t1.setMaximumSize(new Dimension(200, 20));
        
        pass = new JPasswordField(32);
        pass.setAlignmentX(Component.CENTER_ALIGNMENT);
        pass.setMaximumSize(new Dimension(200, 20));
        
        go = new JButton("Go");
        go.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        l1 = new JLabel("Library ID: ");
        l1.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        l2 = new JLabel("Password: ");
        l2.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        b2 = new JButton("Exit");
        b2.setAlignmentX(Component.CENTER_ALIGNMENT);
        Dimension d = b2.getMaximumSize();
        go.setMaximumSize(d);
        
        add(Box.createRigidArea(new Dimension(0,30)));
        add(l1);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(t1);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(l2);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(pass);
        add(Box.createRigidArea(new Dimension(0,20)));
        add(go);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(b2);
        
        go.setActionCommand("go");
        b2.setActionCommand("b2");
        
        go.addActionListener(this);
        b2.addActionListener(this);
        
        
        setVisible(true);
        setResizable(false);
        
        getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.darkGray));
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        
        
    }
    
    public void actionPerformed(ActionEvent ae) {
        if(ae.getActionCommand().equals("go")) {
            String temp;
            try {
            	temp = t1.getText();
            	temp = temp.trim();
            	Statement stmt = conn.createStatement();
            	CallableStatement cstmt = conn.prepareCall("{call logDetails(?)}");
            	cstmt.setString(1, temp);
            	ResultSet rs = stmt.executeQuery("select password from login where lib_id='" + temp+"'");
            	if(!rs.next()) JOptionPane.showMessageDialog(null,"Invalid ID/Pass", "Alert!", JOptionPane.INFORMATION_MESSAGE);
            	else{
            		String pass_ret = rs.getString(1);
            		if(String.valueOf(pass.getPassword()).equals(pass_ret)) {
            			if(temp.startsWith("19")||temp.startsWith("20")) {
            				cstmt.execute();
            				new GUI2(conn,temp);
            			}
                        else {
                        	cstmt.execute();
                        	new GUI1(conn,temp);
                        }
                        dispose();
            		}
            		else {
            			JOptionPane.showMessageDialog(null,"Invalid ID/Pass", "Alert!", JOptionPane.INFORMATION_MESSAGE);
            		}
            	}
            
            }
            catch(Exception e) {
            	JOptionPane.showMessageDialog(null,"Invalid ID/Pass", "Alert!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else if(ae.getActionCommand().equals("b2")) {
            try{conn.close();} 
            catch(SQLException e){System.out.println("Connection Fault: Not Closing");}
        	System.exit(0);
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Login();
            }
        });
    }

}