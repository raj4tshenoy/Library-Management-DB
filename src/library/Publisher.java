package library;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

@SuppressWarnings("serial")
public class Publisher extends JFrame {
JFrame parent;
	
	JScrollPane test;
	
	JLabel title;
	JButton btn, add_mem, trig_update;
	Statement stmt;
	Image image = new ImageIcon(this.getClass().getResource("/resources/icon.png")).getImage();
	SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
	
	Publisher(JFrame parent, Statement stmt){
		super("Library-Management System");
		setSize(1000, 500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		setIconImage(image);
		
		this.stmt = stmt;
		this.parent = parent;
		
		title = new JLabel("Publishers:");
		title.setFont(new Font("Monospaced", Font.BOLD, 20));
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		title.setBorder(new EmptyBorder(10, 0, 10, 0));
		
		btn = new JButton("Back");
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);		
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				parent.setVisible(true);
			}
		});
		
		trig_update = new JButton("Update");
		trig_update.setAlignmentX(Component.CENTER_ALIGNMENT);		
		trig_update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove(test);
				test = new JScrollPane(inst_table());
				test.setMaximumSize(new Dimension(800, 400));
				add(test);
				validate();
				repaint();
			}
		});
		
		add_mem = new JButton("Add a Publisher");
		add_mem.setAlignmentX(Component.CENTER_ALIGNMENT);
		add_mem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AddPub(add_mem, stmt, trig_update);
				add_mem.setEnabled(false);
			}
		});
		
		Dimension d = add_mem.getMaximumSize();
		btn.setMaximumSize(new Dimension(d));
		
		test = new JScrollPane(inst_table());
		test.setMaximumSize(new Dimension(800, 400));
		//test.setBorder(new EmptyBorder(0, 50, 20, 50));
		
		add(Box.createRigidArea(new Dimension(0,10)));
		add(btn);
		add(Box.createRigidArea(new Dimension(0, 5)));
		add(add_mem);
		add(title);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(test);
		
		getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.darkGray));
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	parent.setVisible(true);
            }
        });
		
		setVisible(true);		
	}
	
	public JTable inst_table(){
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("select * from Publisher");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(getRootPane(), "Invalid entry.", "Alert", JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
		}
		JTable tab = new JTable(buildTableModel(rs));
		tab.setEnabled(false);
		tab.setAutoCreateRowSorter(true);
		return tab;
	}

	private static DefaultTableModel buildTableModel(ResultSet rs){
		// TODO Auto-generated method stub
		ResultSetMetaData metaData;
		try {
			metaData = rs.getMetaData();
			Vector<String> columnNames = new Vector<String>();
		    int columnCount = metaData.getColumnCount();
		    for (int column = 1; column <= columnCount; column++) {
		        columnNames.add(metaData.getColumnName(column));
		    }

		    // data of the table
		    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		    while (rs.next()) {
		        Vector<Object> vector = new Vector<Object>();
		        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
		            vector.add(rs.getObject(columnIndex));
		        }
		        data.add(vector);
		    }

		    return new DefaultTableModel(data, columnNames);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
