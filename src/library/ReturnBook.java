package library;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.sql.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class ReturnBook extends JFrame{
	JButton back, returned, click_before_return;
	JPanel pan;
	ResultSet rs;
	Image image = new ImageIcon(this.getClass().getResource("/resources/icon.png")).getImage();
	String issue_id;
	double finepayable;
	Statement stmt;
	
	ReturnBook(String issue_id, Statement stmt, JButton click_this, Boolean disp_button){
		super("Library-Management System");
		setSize(900, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		setIconImage(image);
		this.issue_id = issue_id;
		this.stmt = stmt;
		this.click_before_return = click_this;
		
		back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();				
			}
		});		
		back.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		try {
			rs = stmt.executeQuery("select issue_no, lib_id, isbn, title, date_of_issue, date_of_return, issued_by from book natural join issue where issue_no = " + issue_id);
			pan = dispgui(rs);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		finepayable = computeFine();
		
		returned = new JButton("Return this Book");
		returned.setEnabled(disp_button);
		returned.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					stmt.executeUpdate("update book set no_of_copies = no_of_copies + 1 where isbn = (select isbn from issue where issue_no = " + issue_id + ")");
					stmt.executeUpdate("delete from issue where issue_no = " + issue_id);
					JOptionPane.showMessageDialog(null,"Successfully Returned!", "Return", JOptionPane.INFORMATION_MESSAGE);
					click_before_return.doClick();
					dispose();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});		
		returned.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		add(Box.createRigidArea(new Dimension(0,10)));
		add(back);
		add(Box.createRigidArea(new Dimension(0, 30)));
		add(pan);
		add(Box.createRigidArea(new Dimension(0, 30)));
		add(returned);
		add(Box.createRigidArea(new Dimension(0, 30)));
		
		getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.darkGray));
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/3, dim.height/2-this.getSize().height/3);
		
		setVisible(true);
	}

	private JPanel dispgui(ResultSet rs) throws SQLException{
		// TODO Auto-generated method stub
		
		JPanel jp = new JPanel();
		BoxLayout bl = new BoxLayout(jp, BoxLayout.PAGE_AXIS);
		jp.setLayout(bl);
		jp.setMaximumSize(new Dimension(700, 400));
		jp.add(Box.createRigidArea(new Dimension(0, 20)));
		jp.setBorder(new TitledBorder("Return a Book"));
		
		String labels[] = {"Issue No: ", "Lib ID: ", "ISBN: ", "Title: ", "DOI: ", "DOR: ", "Librarian: "};
		while(rs.next()) {
			for(int i = 0; i < labels.length; i++) {
				JLabel l = new JLabel(labels[i] + rs.getObject(i + 1).toString());
				l.setAlignmentX(Component.CENTER_ALIGNMENT);
				l.setFont(new Font("Monospace", Font.BOLD, 15));
				jp.add(l);
				jp.add(Box.createRigidArea(new Dimension(0,20)));
			}
		}
		JLabel l1 = new JLabel("Fine Payable: " + String.valueOf(finepayable));
		l1.setAlignmentX(Component.CENTER_ALIGNMENT);
		l1.setFont(new Font("Monospace", Font.BOLD, 15));
		jp.add(l1);
		return jp;
	}
	
	private double computeFine() {
		double fine;
		Integer days = 0;
		//String day = "";
		//Fetch day difference
		try {
			rs = stmt.executeQuery("select sysdate - date_of_return from issue where issue_no ="+issue_id);
			rs.next();
			days = rs.getInt(1);
		}
		catch(SQLException ex) {
			JOptionPane.showMessageDialog(null,"Unexpected Error occured. in computing fine", "Alert", JOptionPane.INFORMATION_MESSAGE);
		}
		
		if(days < 0) {
			fine = 0;
		}
		else if(days < 7) {
			fine = 10;
		}
		else if(days < 14) {
			fine = 20;
		}
		else {
			fine = days*5;
		}
		System.out.println(fine);
		return fine;
	}
	
}
