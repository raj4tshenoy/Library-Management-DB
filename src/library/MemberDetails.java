package library;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import java.sql.*;

@SuppressWarnings("serial")
public class MemberDetails extends JFrame{
	JButton back;
	JPanel pan;
	ResultSet rs;
	Image image = new ImageIcon(this.getClass().getResource("/resources/icon.png")).getImage();
	String lib_id;
	
	MemberDetails(String lib_id, Statement stmt){
		super("Library-Management System");
		setSize(900, 500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		setIconImage(image);
		this.lib_id = lib_id;
		
		back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();				
			}
		});		
		back.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		try {
			if(lib_id.startsWith("19"))
				rs = stmt.executeQuery("select * from student where lib_id = " + lib_id);
			else
				rs = stmt.executeQuery("select * from teacher where lib_id = " + lib_id);
			
			pan = dispgui(rs);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		add(Box.createRigidArea(new Dimension(0,10)));
		add(back);
		add(Box.createRigidArea(new Dimension(0, 30)));
		add(pan);
		add(Box.createRigidArea(new Dimension(0, 30)));
		getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.darkGray));
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/3, dim.height/2-this.getSize().height/3);
		
		setVisible(true);
	}

	private JPanel dispgui(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		JPanel jp = new JPanel();
		BoxLayout bl = new BoxLayout(jp, BoxLayout.PAGE_AXIS);
		jp.setLayout(bl);
		jp.setMaximumSize(new Dimension(700, 300));
		jp.add(Box.createRigidArea(new Dimension(0, 20)));
		jp.setBorder(new TitledBorder("Member Details"));
		if(lib_id.startsWith("19")){
			jp.setMaximumSize(new Dimension(700, 350));
			String labels[] = {"LIB_ID: ", "Name: ", "REG. No.: ", "Branch: ", "Year: ", "Block: ", "Issue Limit: "};
			while(rs.next()) {
				for(int i = 0; i < labels.length; i++) {
					JLabel l = new JLabel(labels[i] + rs.getObject(i + 1).toString());
					l.setAlignmentX(Component.CENTER_ALIGNMENT);
					l.setFont(new Font("Monospace", Font.BOLD, 15));
					jp.add(l);
					jp.add(Box.createRigidArea(new Dimension(0,20)));
				}
			}
		}
		else {
			String labels[] = {"LIB_ID: ", "Name: ", "Emp. No.: ", "Dept. Name: ", "Chamber: ", "Building: "};
			while(rs.next()) {
				for(int i = 0; i < labels.length; i++) {
					JLabel l = new JLabel(labels[i] + rs.getObject(i + 1).toString());
					l.setAlignmentX(Component.CENTER_ALIGNMENT);
					l.setFont(new Font("Monospace", Font.BOLD, 15));
					jp.add(l);
					jp.add(Box.createRigidArea(new Dimension(0,20)));
				}
			}
		}
		return jp;
	}
	
	

}
