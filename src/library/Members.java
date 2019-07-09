package library;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.*;


@SuppressWarnings("serial")
public class Members extends JFrame{

	JFrame parent;
	Image image = new ImageIcon(this.getClass().getResource("/resources/icon.png")).getImage();
	JScrollPane test;

	JLabel title;
	JButton btn, add_mem, trig_update;
	Statement stmt;
	SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");

	Members(JFrame parent, Statement stmt){
		super("Library-Management System");
		setSize(1000, 500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		setIconImage(image);

		this.parent = parent;
		this.stmt = stmt;

		title = new JLabel("Members:");
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

		add_mem = new JButton("Add a Member");
		add_mem.setAlignmentX(Component.CENTER_ALIGNMENT);
		add_mem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AddMem(add_mem, stmt, trig_update);
				add_mem.setEnabled(false);
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

		Dimension d = add_mem.getMaximumSize();
		btn.setMaximumSize(new Dimension(d));

		test = new JScrollPane(inst_table());
		test.setMaximumSize(new Dimension(800, 400));

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

	public JTable inst_table() {
		ResultSet rs;
		String[] cols = {"Lib-ID", "Name", "Type"};
		String Query = "with temp(lib_id, name) as ((select lib_id, name from student) union (select lib_id, name from teacher)) select lib_id, name from temp";
		DefaultTableModel model = new DefaultTableModel(0, cols.length);
		model.setColumnIdentifiers(cols);
		try {
			int i = 0;
			rs = stmt.executeQuery(Query);
			while(rs.next()) {
				Vector<Object> vec = new Vector<>();
				vec.add(rs.getObject(1));
				vec.add(rs.getObject(2));
				if(rs.getObject(1).toString().startsWith("19"))
					vec.add("Student");
				else
					vec.add("Teacher");
				model.insertRow(i, vec);
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JTable tab = new JTable(model);
		tab.setDefaultEditor(Object.class, null);
		tab.setAutoCreateRowSorter(true);
		tab.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = tab.rowAtPoint(e.getPoint());
				String Lib_id = tab.getValueAt(row, 0).toString();
				new MemberDetails(Lib_id, stmt);
			}
		});
		return tab;
	}
}
