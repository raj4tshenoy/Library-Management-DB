package library;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class Issue extends JFrame{
	JFrame parent;
	String LIBID;
	Image image = new ImageIcon(this.getClass().getResource("/resources/icon.png")).getImage();
	JScrollPane jp2;
	Statement stmt;
	JTable tab;
///////////////////////////////////////////////
	static JLabel LibID, lbook, ldoi, lissuedby;
	static JTextField tf, book, doi, issuedby;
	static JButton btn1, btn2, btn4, addf;
	int filtr=0;
	String slibid = "", sbookname = "", sdoi = "", sissuedby = ""; //get text from filter storage strings
	
///////////////////////////////////////////////
	
	JPanel jp1;
	
	Issue(JFrame parent, int f, Statement stmt, String LIBID){
		super("Library-Management System");
		setSize(1000, 500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		setIconImage(image);
		this.parent = parent;
		this.stmt = stmt;
		this.LIBID = LIBID;
		LibID = new JLabel("Library ID:    ");
		
		///////////////////////////////////////////////
		lbook = new JLabel("Book Name: ");
		ldoi = new JLabel("DOI: ");
		lissuedby = new JLabel("Issued By: ");
		tf = new JTextField(50);
		
		////////////////////////////////////////////////
		book = new JTextField(50);
		doi = new JTextField(10);
		doi.setToolTipText("Date Format: DD-MM-YYYY");
		issuedby = new JTextField(5);
		///////////////////////////////////////////////
		
		btn1 = new JButton("Search");
		btn2 = new JButton("Back");
		btn4 = new JButton("Add Filter"); ///////////////////////////////////////////////
		btn4.setMaximumSize(new Dimension(btn4.getMaximumSize()));
		
		lbook.setVisible(false);
		ldoi.setVisible(false);
		lissuedby.setVisible(false);
		book.setVisible(false);
		doi.setVisible(false);
		issuedby.setVisible(false);
		
		jp1 = getPanel1(f);		
		jp2 = getPanel2(slibid,sbookname,sdoi,sissuedby);
		
		jp1.setMaximumSize(new Dimension(this.getSize().width, 250));
		
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(jp1);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(jp2);
		
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				parent.setVisible(true);
			}
		});
///////////////////////////////////////////////
		btn4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(filtr==0) {
					lbook.setVisible(true);
					ldoi.setVisible(true);
					lissuedby.setVisible(true);
					book.setVisible(true);
					doi.setVisible(true);
					issuedby.setVisible(true);
					filtr = 1;
					btn4.setText("Cancel");
				}
				else {
					lbook.setVisible(false);
					ldoi.setVisible(false);
					lissuedby.setVisible(false);
					book.setVisible(false);
					doi.setVisible(false);
					issuedby.setVisible(false);
					btn4.setText("Add Filter");
					filtr=0;
					tf.setText("");
					book.setText("");
					doi.setText("");
					issuedby.setText("");
				}
			}
		}); 
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lbook.setVisible(false);
				ldoi.setVisible(false);
				lissuedby.setVisible(false);
				book.setVisible(false);
				doi.setVisible(false);
				issuedby.setVisible(false);
				btn4.setText("Add Filter");
				filtr=0;
				

				//Check Filter conditions if any:
				try {
					slibid = tf.getText();
				}
				catch (Exception eee) {
					slibid = "";
				}
				try {
					sbookname = book.getText();
				}
				catch (Exception eee) {
					sbookname = "";
				}
				try {
					sdoi = doi.getText();
				}
				catch (Exception eee) {
					sdoi = "";
				}
				try {
					sissuedby = issuedby.getText();
				}
				catch (Exception eee) {
					sissuedby = "";
				}
				
				//Refresh Table
				remove(jp2);
				jp2 = getPanel2(slibid,sbookname,sdoi,sissuedby);
				add(jp2);
				validate();
				repaint();
				
				//Reset text fields after search
				tf.setText("");
				book.setText("");
				doi.setText("");
				issuedby.setText("");
			}
		});///////////////////////////////////////////////
		
		
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

	private JScrollPane getPanel2(String slibid, String sbookname, String sdoi, String sissuedby) {
		// TODO Auto-generated method stub	
		ResultSet rs = null;
		String Query = " select issue_no as ISSUE, lib_id as Library_ID, ISBN, title as Title, date_of_issue as DOI, date_of_return as DOR, issued_by as Librarian from book natural join issue where lib_id like '%"+ slibid +"%' and title like '%"+ sbookname +"%' and to_char(date_of_issue,'DD-MM-YYYY') like '%"+sdoi+"%' and issued_by like '%"+sissuedby+"%'" + " order by issue_no";
		
		if(!LIBID.isEmpty())
			Query = "select issue_no as ISSUE, lib_id as LIBRARY_ID, ISBN, title as Title, date_of_issue as DOI, date_of_return as DOR, issued_by as Librarian from book natural join issue where lib_id = " + LIBID + " order by issue_no";
		try {
			rs = stmt.executeQuery(Query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tab = new JTable(buildTableModel(rs));
		tab.setDefaultEditor(Object.class, null);
		tab.setAutoCreateRowSorter(true);
		tab.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = tab.rowAtPoint(e.getPoint());
				String issue_no = tab.getValueAt(row, 0).toString();
				
				if(LIBID.isEmpty())
					new ReturnBook(issue_no, stmt, btn1, true);
				else
					new ReturnBook(issue_no, stmt, btn1, false);
			}
		});
		JScrollPane p = new JScrollPane(tab);
		return p;
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

	private JPanel getPanel1(int f) {
		// TODO Auto-generated method stub
		
		JPanel p = new JPanel(new GridBagLayout());	

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0,5,5,5);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		if(f==0) {
		p.add(LibID, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		
		p.add(tf, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 0;
		
		p.add(btn1, gbc);
		
		gbc.gridx = 3;
		gbc.gridy = 0;
		
		p.add(btn4, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		
		p.add(lbook, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		
		p.add(book, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		
		p.add(ldoi, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		
		p.add(doi, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		
		p.add(lissuedby, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		
		p.add(issuedby, gbc);
								//////////////end Change
		
		p.setBorder(new TitledBorder("Search"));
		}
		gbc.gridx = 4;
		gbc.gridy = 0;
		
		p.add(btn2, gbc);
		
		
		
		//p.setBorder(new TitledBorder(""));
		p.setSize(1000, 250);
		
		return p;
	}
	
}
