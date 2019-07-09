package library;

import java.sql.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;

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
import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;

import layout.SpringUtilities;

@SuppressWarnings("serial")
public class AddPub extends JFrame{
	
	//DECLARATION
	JButton btn, re_enable_this, submit;
	JPanel pan;
	Image image = new ImageIcon(this.getClass().getResource("/resources/icon.png")).getImage();
	static ArrayList<JTextField> ins_list = new ArrayList<JTextField>();
	boolean ins_flag = true;
	
	//CONSTRUCTOR
	AddPub(JButton re_enable_this, Statement stmt, JButton trig_update){
		super("Library-Management System");
		setSize(900, 500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		setIconImage(image);
		this.re_enable_this = re_enable_this; //to enable the button on the previous screen.
		
		//BACK BUTTON
		btn = new JButton("Back");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				re_enable_this.setEnabled(true);
			}
		});		
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		
		//SUBMIT BUTTON
		submit = new JButton("Submit");
		submit.addActionListener(new ActionListener() { //Submit Button
			public void actionPerformed(ActionEvent e) {
				ins_flag = true;
				String base = "INSERT INTO PUBLISHER VALUES(";
				String append = "";
				try {
					for (int i = 0; i<ins_list.size(); i++) {
						if(ins_list.get(i).getText().trim().equalsIgnoreCase("")) {
							ins_flag = false;
							break;
						}
						else if(i==0){
							append = append + ins_list.get(i).getText().trim() + ",";
						}
						else if(i==6) {
							append = append + ins_list.get(i).getText().trim();
						}
						else {
							append = append + "'" + ins_list.get(i).getText().trim() + "'" + ",";
						}
					}
					append = append + ")";
					
					//Processing the string:
					if(ins_flag) {
						stmt.executeUpdate(base + append);
						JOptionPane.showMessageDialog(getRootPane(), "Record Inserted Successfully!!!", "Alert", JOptionPane.INFORMATION_MESSAGE);
						trig_update.doClick();
					}
					//if(!rs.next()) JOptionPane.showMessageDialog(getRootPane(), "Record cannot be inserted. Please mention null in the empty fields if any.", "Alert", JOptionPane.INFORMATION_MESSAGE);
					else JOptionPane.showMessageDialog(getRootPane(), "Record cannot be inserted. Fields cannot be empty.", "Alert", JOptionPane.INFORMATION_MESSAGE);
				}
				catch(SQLException ee) {
					JOptionPane.showMessageDialog(getRootPane(), "An unexpected error occured while execution. Please contact the developer.", "Alert", JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
		});		
		submit.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		pan = ins_gui();
		
		add(Box.createRigidArea(new Dimension(0,10)));
		add(btn);
		add(Box.createRigidArea(new Dimension(0, 30)));
		add(pan);
		add(Box.createRigidArea(new Dimension(0, 30)));
		add(submit);
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
	
	private JPanel ins_gui() {
		// TODO Auto-generated method stub
		int i;
		String[] labels = {"Publisher ID :", "Name :", "Street :", "Building :", "City: ", "Email: ", "Phone :"};
		JPanel p = new JPanel(new SpringLayout());
		//p.setBorder(new EmptyBorder(30, 300, 10, 300));
		
		for(i = 0; i<labels.length; i++) {
			JLabel l = new JLabel(labels[i], JLabel.TRAILING);
			p.add(l);
			JTextField tf = new JTextField();
			tf.setMaximumSize(new Dimension(200, 20));
			l.setLabelFor(tf);
			p.add(tf);
			ins_list.add(tf);
		}
		SpringUtilities.makeCompactGrid(p, labels.length, 2,
										10,10,
										10,10);
		p.setBorder(new TitledBorder("Add a Publisher"));
		
		return p;
	}
}
