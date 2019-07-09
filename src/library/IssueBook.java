package library;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class IssueBook extends JFrame{
	JButton back, issue;
	JPanel pan;
	JLabel lbl;
	JTextField tf;
	ResultSet rs;
	String librarian;
	Image image = new ImageIcon(this.getClass().getResource("/resources/icon.png")).getImage();
	String isbn;
	double finepayable;
	Statement stmt;
	
	IssueBook(String isbn, Statement stmt, String librarian){
		super("Library-Management System");
		setSize(900, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		setIconImage(image);
		
		this.librarian = librarian;
		this.isbn = isbn;
		this.stmt = stmt;
		
		back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();				
			}
		});		
		back.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		try {
			rs = stmt.executeQuery("select * from book where isbn = " + isbn);
			pan = dispgui(rs);
			pan.setAlignmentX(Component.CENTER_ALIGNMENT);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		issue = new JButton("Issue Book");
		issue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(tf.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null,"Insert Library ID", "Issue", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						rs = stmt.executeQuery("select count(issue_no) from issue where lib_id = "+tf.getText());
						rs.next();
						if(Integer.parseInt(rs.getObject(1).toString()) == 7 && tf.getText().startsWith("19")) {
							JOptionPane.showMessageDialog(null,"Issue Limit Reached", "Issue", JOptionPane.INFORMATION_MESSAGE);
							dispose();
						}
						stmt.executeUpdate("update book set no_of_copies = no_of_copies - 1 where isbn = " + isbn);
						stmt.executeUpdate("insert into issue values((select max(issue_no) from issue) + 1,"+isbn+",(select sysdate from dual), (select sysdate from dual) + 14,"+librarian+","+tf.getText()+")");
						JOptionPane.showMessageDialog(null,"Successfully Issued!", "Issue", JOptionPane.INFORMATION_MESSAGE);
						dispose();
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,"Invalid Lib ID", "Issue", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});		
		issue.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		add(Box.createRigidArea(new Dimension(0,10)));
		add(back);
		add(Box.createRigidArea(new Dimension(0, 30)));
		add(pan);
		add(Box.createRigidArea(new Dimension(0, 30)));
		add(issue);
		add(Box.createRigidArea(new Dimension(0, 30)));
		
		getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.darkGray));
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/4, dim.height/2-this.getSize().height/4);
		
		setVisible(true);
	}

	private JPanel dispgui(ResultSet rs) throws SQLException{
		// TODO Auto-generated method stub
		
		JPanel jp = new JPanel();
		BoxLayout bl = new BoxLayout(jp, BoxLayout.PAGE_AXIS);
		jp.setLayout(bl);
		jp.setMaximumSize(new Dimension(700, 400));
		jp.add(Box.createRigidArea(new Dimension(0, 20)));
		jp.setBorder(new TitledBorder("Issue a Book"));
		
		lbl = new JLabel("Enter Library_ID");
		lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		tf = new JTextField();
		tf.setMaximumSize(new Dimension(200, 20));
		JLabel dash = new JLabel("------------------------------------------------------");
		dash.setAlignmentX(Component.CENTER_ALIGNMENT);
		jp.add(lbl);
		jp.add(tf);
		jp.add(dash);
		jp.add(Box.createRigidArea(new Dimension(0, 5)));
		String labels[] = {"ISBN: ", "Title: ", "Author: ", "No. of Copies: ", "Publisher No.: ", "Edition: ", "Genre: ", "Aisle: "};
		while(rs.next()) {
			for(int i = 0; i < labels.length; i++) {
				JLabel l = new JLabel(labels[i] + rs.getObject(i + 1).toString());
				l.setAlignmentX(Component.CENTER_ALIGNMENT);
				l.setFont(new Font("Monospace", Font.BOLD, 15));
				jp.add(l);
				jp.add(Box.createRigidArea(new Dimension(0,20)));
			}
		}
		return jp;
	}
}
