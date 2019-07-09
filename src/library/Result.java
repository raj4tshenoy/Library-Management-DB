package library;

import javax.swing.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;

@SuppressWarnings("serial")
public class Result extends JFrame{
	JButton btn, re_enable_this;
	JScrollPane pan;
	String librarian;
	ArrayList<JTextField> params = new ArrayList<JTextField>();
	Image image = new ImageIcon(this.getClass().getResource("/resources/icon.png")).getImage();
	Statement stmt;
	JTable tab;
	int stud_flag;
	
	Result(ArrayList<JTextField> params, JButton re_enable_this, String stitle, String sauthor, String sisbn, String spublisher, String sgenre, Statement stmt, int n, String librarian){
		super("Library-Management System");
		setSize(900, 500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		setIconImage(image);
		
		this.stmt = stmt;
		this.params = params;
		this.librarian = librarian;
		this.re_enable_this = re_enable_this;
		stud_flag = n;
		
		btn = new JButton("Back");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				re_enable_this.setEnabled(true);
			}
		});		
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		pan = Result_table(stitle,sauthor,sisbn,spublisher,sgenre);
		
		add(Box.createRigidArea(new Dimension(0,10)));
		add(btn);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(pan);
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

	private JScrollPane Result_table(String stitle, String sauthor, String sisbn, String spublisher, String sgenre) {
		ResultSet rs = null;
		String Query = "select isbn, title, author, no_of_copies, name as publisher_name, edition, genre, aisle from book natural join publisher where title like '%"+stitle+"%' and author like '%"+sauthor+"%' and isbn like '%"+sisbn+"%' and name like '%"+spublisher+"%' and genre like '%"+sgenre+"%'";
		try {
			rs = stmt.executeQuery(Query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tab = new JTable(buildTableModel(rs));
		tab.setAutoCreateRowSorter(true);
		tab.setDefaultEditor(Object.class, null);
		
		if (stud_flag == 1) {
			tab.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					int row = tab.rowAtPoint(e.getPoint());
					String isbn = tab.getValueAt(row, 0).toString();
					new IssueBook(isbn, stmt, librarian);
				}
			});
		}
		
		
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


	
}
