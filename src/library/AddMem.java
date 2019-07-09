package library;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
//import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import java.sql.*;
//import javax.swing.border.TitledBorder;
import java.util.ArrayList;

import layout.SpringUtilities;

@SuppressWarnings("serial")
public class AddMem extends JFrame{
	//DECLARATION
	JButton btn, re_enable_this, submit;
	JPanel p1, p2;
	JTabbedPane tp;
    boolean	ins_flag = true;
    String sx;
    Image image = new ImageIcon(this.getClass().getResource("/resources/icon.png")).getImage();
	static ArrayList<JTextField> t_list = new ArrayList<JTextField>();
	static ArrayList<JTextField> s_list = new ArrayList<JTextField>();
	
	//CONSTRUCTOR
	AddMem(JButton re_enable_this, Statement stmt, JButton trig_update){
		super("Library-Management System");
		setSize(900, 500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		setIconImage(image);
		this.re_enable_this = re_enable_this;
		
		btn = new JButton("Back");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				re_enable_this.setEnabled(true);
			}
		});		
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		
		
		p1 = ins_std1();
		p2 = ins_tch1();
		
		JTabbedPane tp = new JTabbedPane();
		tp.setTabPlacement(JTabbedPane.TOP);
		tp.setBorder(new EmptyBorder(0, 10, 10, 10));
		tp.add("Add Student", p1);
		tp.add("Add Teacher", p2);
		
		
		submit = new JButton("Submit");
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ins_flag = true;
				if(tp.getSelectedIndex()==0) {
					
					String base = "INSERT INTO STUDENT VALUES(";
					String append = "";
					try {
						for (int i = 0; i<s_list.size(); i++) {
							if(s_list.get(i).getText().trim().equalsIgnoreCase("")) {
								ins_flag = false;
								break;
							}
							else if(i==0) {
								sx = s_list.get(i).getText().trim();
								append = append + sx + ",";
							}
							else if(i==2 || i==4){
								append = append + s_list.get(i).getText().trim() + ",";
							}
							else if(i==6) {
								append = append + s_list.get(i).getText().trim();
							}
							else {
								append = append + "'" + s_list.get(i).getText().trim() + "'" + ",";
							}
						}
						append = append + ")";
						try {
							stmt.executeUpdate("INSERT INTO LOGIN VALUES(" + sx + ",'0000')");
						}catch(SQLException e3) {
							JOptionPane.showMessageDialog(getRootPane(), "LIB_ID should be UNIQUE", "Alert", JOptionPane.INFORMATION_MESSAGE);
							ins_flag=false;
							stmt.executeUpdate("DELETE from LOGIN where lib_id = "+sx);
						}
						if(ins_flag) {
							stmt.executeUpdate(base + append);
							JOptionPane.showMessageDialog(getRootPane(), "Record Inserted Successfully!!!", "Alert", JOptionPane.INFORMATION_MESSAGE);
							trig_update.doClick();
						}
						else {
							JOptionPane.showMessageDialog(getRootPane(), "Record cannot be inserted due to a constraint violation.", "Alert", JOptionPane.INFORMATION_MESSAGE);
							try {
								stmt.executeUpdate("DELETE FROM LOGIN WHERE LIB_ID = " + sx);
							}
							catch(SQLException eee) {
								JOptionPane.showMessageDialog(getRootPane(), "Record not inserted.", "Alert", JOptionPane.INFORMATION_MESSAGE);
							}
						}
					}
					catch(SQLException ee) {
						JOptionPane.showMessageDialog(getRootPane(), "Invalid entry.", "Alert", JOptionPane.INFORMATION_MESSAGE);
					}
					
				}
				else {
					
					String base = "INSERT INTO TEACHER VALUES(";
					String append = "";
					try {
						for (int i = 0; i<t_list.size(); i++) {
							if(t_list.get(i).getText().trim().equalsIgnoreCase("")) {
								ins_flag = false;
								break;
							}
							else if(i==0) {
								sx = t_list.get(i).getText().trim();
								append = append + sx + ",";
							}
							else if(i==2){
								append = append + t_list.get(i).getText().trim() + ",";
							}
							else if(i==5) {
								append = append + "'"+t_list.get(i).getText().trim()+"'";
							}
							else {
								append = append + "'" + t_list.get(i).getText().trim() + "'" + ",";
							}
						}
						append = append + ")";
						try {
							stmt.executeUpdate("INSERT INTO LOGIN VALUES(" + sx + ",'0000')");
						}catch(SQLException e3) {
							JOptionPane.showMessageDialog(getRootPane(), "LIB_ID should be UNIQUE", "Alert", JOptionPane.INFORMATION_MESSAGE);
							ins_flag = false;
						}
						if(ins_flag) {
							stmt.executeUpdate(base + append);
							JOptionPane.showMessageDialog(getRootPane(), "Record Inserted Successfully!!!", "Alert", JOptionPane.INFORMATION_MESSAGE);
							trig_update.doClick();
						}
						else {
							JOptionPane.showMessageDialog(getRootPane(), "Record cannot be inserted due to a constraint violation.", "Alert", JOptionPane.INFORMATION_MESSAGE);
							try {
								stmt.executeUpdate("DELETE FROM LOGIN WHERE LIB_ID = " + sx);
							}
							catch(SQLException eee) {
								JOptionPane.showMessageDialog(getRootPane(), "Record not inserted.", "Alert", JOptionPane.INFORMATION_MESSAGE);
							}
						}
					}
					catch(SQLException ee) {
						JOptionPane.showMessageDialog(getRootPane(), "Invalid entry.", "Alert", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				
			}
		});		
		submit.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		add(Box.createRigidArea(new Dimension(0,10)));
		add(btn);
		//add(Box.createRigidArea(new Dimension(0, 30)));
		add(tp);
		//add(Box.createRigidArea(new Dimension(0, 30)));
		add(submit);
		add(Box.createRigidArea(new Dimension(0,10)));
		getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.darkGray));
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/3, dim.height/2-this.getSize().height/3);
		
		
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	re_enable_this.setEnabled(true);
            }
        });
		
		
		setVisible(true);
		
		
	}
	
	private JPanel ins_std1() {
		// TODO Auto-generated method stub
		int i;
		String[] labels = {"Library ID :", "Name :", "Reg No. :", "Department :", "Year :", "Block :", "Issue Limit :"};
		JPanel p = new JPanel(new SpringLayout());
		p.setBorder(new EmptyBorder(30, 200, 10, 200));
		
		for(i = 0; i<labels.length; i++) {
			JLabel l = new JLabel(labels[i], JLabel.TRAILING);
			p.add(l);
			JTextField tf = new JTextField();
			tf.setMaximumSize(new Dimension(350, 20));
			l.setLabelFor(tf);
			p.add(tf);
			s_list.add(tf);
		}
		SpringUtilities.makeCompactGrid(p, labels.length, 2,
										10,10,
										10,10);
		//p.setBorder(new TitledBorder("Add a member"));
		
		return p;
	}
	
	private JPanel ins_tch1() {
		// TODO Auto-generated method stub
		int i;
		String[] labels = {"Library ID :", "Name :", "Emp No. :", "Department :", "Chamber :", "Building :"};
		JPanel p = new JPanel(new SpringLayout());
		p.setBorder(new EmptyBorder(30, 200, 10, 200));
		
		for(i = 0; i<labels.length; i++) {
			JLabel l = new JLabel(labels[i], JLabel.TRAILING);
			p.add(l);
			JTextField tf = new JTextField();
			tf.setMaximumSize(new Dimension(350, 20));
			l.setLabelFor(tf);
			p.add(tf);
			t_list.add(tf);
		}
		SpringUtilities.makeCompactGrid(p, labels.length, 2,
										10,10,
										10,10);
		//p.setBorder(new TitledBorder("Add a member"));
		
		return p;
	}
}
