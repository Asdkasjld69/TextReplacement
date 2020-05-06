package ok;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import demo.Demo;

public class Layout_Text extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String[] headers = { "Before", "After" };
	private static String[] headers_t = { "Tag", "Type", "Constraint", "Value" };
	private static String[] lheaders = { "Message", "Time" };
	private String regu = "";
	private String path = "path";
	private File config_path = new File("config");
	private File config_s = new File(config_path.getPath() + "/StringToReplace.txt");
	private File config_t = new File(config_path.getPath() + "/TagToReplace.txt");
	private DefaultTableModel DTM = new DefaultTableModel(null, headers);
	private DefaultTableModel DTM_T = new DefaultTableModel(null, headers_t);
	private DefaultTableModel LDTM = new DefaultTableModel(null, lheaders) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void setValueAt(Object aValue, int row, int column) {
			// TODO Auto-generated method stub
			return;
		}

	};
	private ArrayList<String> srcs = new ArrayList<String>();
	private ArrayList<String> dests = new ArrayList<String>();
	private HashMap<String, ArrayList<ArrayList<String>>> tagtc = new HashMap<String, ArrayList<ArrayList<String>>>();
	private HashMap<String, String> tagv = new HashMap<String, String>();
	private JTextField input_regu = new JTextField(32);
	private JTextField input_path = new JTextField();
	private JSpinner input_depth = new JSpinner(new SpinnerNumberModel(0,0,100,1));
	private JTable body = new JTable(DTM);
	private JTable log = new JTable(LDTM) {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
			// TODO Auto-generated method stub
			Component comp = super.prepareRenderer(renderer, row, column);
			comp.setBackground(new Color(255, 255, 255));
			comp.setForeground(new Color(0, 0, 0));
			String[] mess = LDTM.getValueAt(row, 0).toString().split("#");
			if (mess.length > 0) {
				if (mess[mess.length - 1].equals("COMMIT")) {
					comp.setBackground(new Color(255, 96, 255));
				}
				if (mess[mess.length - 1].equals("STARTED")) {
					comp.setBackground(new Color(128, 196, 255));
				}
				if (mess[mess.length - 1].contains("FINISHED")) {
					comp.setBackground(new Color(128, 255, 128));
				}
				if (mess[mess.length - 1].contains("FAILED")) {
					comp.setBackground(new Color(255, 96, 96));
					comp.setForeground(new Color(255, 255, 255));
				}
			}
			return comp;
		}

	};
	JScrollPane panel_body = new JScrollPane(body);
	JScrollPane panel_log = new JScrollPane(log);
	JPanel panel_top = new JPanel();
	JPanel panel_main = new JPanel();
	JPanel panel_left = new JPanel();
	private Map<String, JButton> buttons = new HashMap<String, JButton>();
	private Dimension size = new Dimension(716, 400);
	private double ratio = 0.4;
	private JDialog dialog_about = new JDialog();
	private JDialog error = new JDialog();
	JLabel intro = new JLabel(
			"<html>A <font color='#66ccff'>Tool</font> for <font color='red'><b>MASS</b></font> <u>text</u> <i>replacing</i>!</html>");
	private JCheckBox check_safe = new JCheckBox("Safe");
	int height_top;
	private boolean stopflag = false;
	private int state = 0;
	private boolean mode = true;
	public static long LAUNCH_TIME = System.currentTimeMillis();

	public Layout_Text() {
		super();
		JMenuBar mbar = new JMenuBar();
		JButton about = new JButton("About");
		JButton switchmode = new JButton("TEXT");
		switchmode.setPreferredSize(switchmode.getMinimumSize());
		switchmode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				mode = !mode;
				if (!mode) {
					switchmode.setText("TAG");
					loadTopPanel("tag");
					body.setModel(DTM_T);
					setTitle("Replace Tag");
				} else {
					switchmode.setText("TEXT");
					loadTopPanel("text");
					body.setModel(DTM);
					setTitle("Replace Text");
				}
				
			}

		});
		about.setBackground(new Color(238, 238, 238));
		switchmode.setBackground(new Color(112, 204, 255));
		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dialog_about.setVisible(true);
				dialog_about.setLocation(getX(), getY());
			}

		});
		dialog_about.setTitle("About me");
		dialog_about.setSize(224, 132);
		dialog_about.setResizable(false);
		FlowLayout flow_about = new FlowLayout();
		flow_about.setAlignment(FlowLayout.LEFT);
		dialog_about.setLayout(flow_about);
		dialog_about.add(intro);
		dialog_about.add(new JLabel("hotmail:"));
		dialog_about.add(new JLabel("cnmbxjj@hotmail.com"));
		dialog_about.add(new JLabel("discord:"));
		dialog_about.add(new JLabel("5sfPA3m"));
		dialog_about.add(new JLabel("<html><b>*Word&Excel not supported (yet)*</b></html>"));
		mbar.add(switchmode);
		mbar.add(about);
		mbar.add(new JLabel("PATH:"));
		mbar.add(input_path);
		this.setJMenuBar(mbar);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setTitle(Demo.title);
		Dimension minsize = new Dimension(600, 400);
		this.setMinimumSize(minsize);
		this.setSize(size);
		this.setLocation((dim.width - this.getWidth()) / 2, (dim.height - this.getHeight()) / 2);
		check_safe.setSelected(true);
		
		JPanel panel_bottom = new JPanel();
		loadTopPanel("text");
		panel_main.setLayout(new BorderLayout());
		panel_bottom.setLayout(new FlowLayout());
		panel_left.setLayout(new BorderLayout());

		JLabel label_regu = new JLabel("Regu:");
		JLabel drag = new JLabel(" ");
		
		Cursor cu = new Cursor(Cursor.E_RESIZE_CURSOR);
		drag.setCursor(cu);
		panel_bottom.add(label_regu);
		panel_bottom.add(input_regu);
		panel_bottom.add(new JLabel("depth:"));
		panel_bottom.add(input_depth);
		panel_bottom.add(check_safe);
		panel_left.add(panel_log);
		panel_left.add(drag, BorderLayout.EAST);
		panel_main.add(panel_top, BorderLayout.NORTH);
		panel_main.add(panel_body, BorderLayout.CENTER);
		panel_main.add(panel_bottom, BorderLayout.SOUTH);
		panel_main.add(panel_left, BorderLayout.WEST);
		this.add(panel_main);
		panel_main.setVisible(true);
		panel_body.setVisible(true);
		panel_bottom.setVisible(true);
		panel_log.setVisible(true);
		panel_log.setPreferredSize(new Dimension((int) (this.getWidth() * ratio), panel_log.getHeight()));
		this.setVisible(true);
		panel_top.setVisible(true);
		height_top = panel_top.getHeight();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		drag.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				int mousex = e.getXOnScreen();
				panel_left
						.setPreferredSize(
								new Dimension(
										mousex - panel_left.getLocationOnScreen().x < 100 ? 100
												: getWidth() - (mousex - panel_left.getLocationOnScreen().x) < 100
														? getWidth() - 100
														: mousex - panel_left.getLocationOnScreen().x,
										panel_left.getHeight()));
				panel_left.revalidate();
				ratio = (double) panel_left.getWidth() / panel_main.getWidth();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});

		addWindowStateListener(new WindowStateListener() {

			@Override
			public void windowStateChanged(WindowEvent e) {
				// TODO Auto-generated method stub
				adaptSize();

			}

		});
		panel_main.addComponentListener(new ComponentListener() {

			@Override
			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub
				adaptSize();
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

		});
		// this.setResizable(false);
	}

	public void loadConfig() {
		if (!config_s.exists()) {
			try {
				config_s.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!config_t.exists()) {
			try {
				config_t.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String[] conf = TextDataReader.read(config_s);
		String[] conf_t = TextDataReader.read(config_t);
		boolean reguflag = true;
		boolean pathflag = true;
		if (conf[0].length() > 0) {
			String[] confs = conf[0].split("\n");
			System.out.println("======================================");
			for (String cfs : confs) {
				if (reguflag) {
					reguflag = false;
					regu = cfs;
					input_regu.setText(cfs);
					continue;
				}
				if (pathflag) {
					pathflag = false;
					path = cfs;
					input_path.setText(cfs);
					continue;
				}
				String[] cs = cfs.split("\t");
				srcs.add(cs[0]);
				dests.add(cs[1]);
				addChange(cs[0], cs[1]);
				System.out.println("[" + cs[0] + " -> " + cs[1] + "]");
			}
			System.out.println("======================================");
		}
		System.out.println(conf_t[0]);
		if (conf_t[0].length() > 0) {
			String[] confs_t = conf_t[0].split("\n");
			System.out.println("======================================");
			for (String cfs : confs_t) {
				String[] cs = cfs.split("\t");
				ArrayList<ArrayList<String>> ttc = new ArrayList<ArrayList<String>>();
				ArrayList<String> tt = new ArrayList<String>();
				ArrayList<String> tc = new ArrayList<String>();
				ttc.add(tt);
				ttc.add(tc);
				tagtc.put(cs[0], ttc);
				tagv.put(cs[0], cs.length<4?"":cs[3]);
			}
			for (String cfs : confs_t) {
				String[] cs = cfs.split("\t");
				ArrayList<String> tt = tagtc.get(cs[0]).get(0);
				ArrayList<String> tc = tagtc.get(cs[0]).get(1);
				tt.add(cs[1]);
				tc.add(cs[2]);
				System.out.println(tagtc);
				System.out.println(tagv);
				addChange_t(cs[0], cs[1], cs[2], cs.length<4?"":cs[3]);
			}
		}
		String[] logs = conf[1].concat(conf_t[1]).split("\n");
		for (String l : logs) {
			String[] log = l.split("\t");
			addLog(l.replace(log[log.length-1], ""), log[log.length-1]);
		}
	}

	public Map<String, JButton> getActions() {
		return buttons;
	}

	public void commitChanges(ArrayList<File> files) {
		String log = "START #COMMIT";
		Date time = new Date();
		addLog(log, time.toString());
		int rows = 0;
		if (mode) {
			StringReplacement.setSerial(System.currentTimeMillis());
			rows = DTM.getRowCount();
			srcs = new ArrayList<String>();
			dests = new ArrayList<String>();
			for (int i = 0; i < rows; i++) {
				srcs.add(DTM.getValueAt(i, 0).toString());
				dests.add(DTM.getValueAt(i, 1).toString());
			}
			Object[] src = srcs.toArray();
			Object[] dest = dests.toArray();
			for (File file : files) {
				log = StringReplacement.replace(file, src, dest, check_safe.isSelected());
				String[] lo = log.split("\n");
				for (String l : lo) {
					String[] m = l.split("\t");
					addLog(l.replace(m[m.length-1], ""), m[m.length-1]);
				}
			}
		} else {
			XmlBlockReplacement.setSerial(System.currentTimeMillis());
			rows = DTM_T.getRowCount();
			tagtc = new HashMap<String, ArrayList<ArrayList<String>>>();
			for (int i = 0; i < rows; i++) {
				ArrayList<ArrayList<String>> tca = new ArrayList<ArrayList<String>>();
				ArrayList<String> ta = new ArrayList<String>();
				ArrayList<String> ca = new ArrayList<String>();
				tca.add(ta);
				tca.add(ca);
				tagtc.put(DTM_T.getValueAt(i, 0).toString(), tca);
			}
			for (int i = 0; i < rows; i++) {
				ArrayList<ArrayList<String>> tca = tagtc.get(DTM_T.getValueAt(i, 0).toString());
				ArrayList<String> ta = tca.get(0);
				ArrayList<String> ca = tca.get(1);
				ta.add(DTM_T.getValueAt(i, 1).toString());
				ca.add(DTM_T.getValueAt(i, 2).toString());
				tagv.put(DTM_T.getValueAt(i, 0).toString(), DTM_T.getValueAt(i, 3).toString());
			}
			for (File file : files) {
				Set<String> ks = tagtc.keySet();
				String k = "";
				Iterator<String> it = ks.iterator();
				ArrayList<Object[]> tar = new ArrayList<Object[]>();
				ArrayList<Object[]> car = new ArrayList<Object[]>();
				ArrayList<Object> var = new ArrayList<Object>();
				ArrayList<String> ta;
				while (it.hasNext()) {
					k = it.next();
					ta = new ArrayList<String>();
					ta.add(k);
					ta.addAll(1, tagtc.get(k).get(0));
					tar.add(ta.toArray());
					ta = tagtc.get(k).get(1);
					car.add(ta.toArray());
					var.add(tagv.get(k));
				}
				log = XmlBlockReplacement.replace(file, tar, car, var.toArray(), check_safe.isSelected());
				String[] lo = log.split("\n");
				for (String l : lo) {
					String[] m = l.split("\t");
					addLog(l.replace(m[m.length-1], ""), m[m.length-1]);
				}
			}
		}

		log = "END #COMMIT";
		time = new Date();
		addLog(log, time.toString());
	}

	public void overrideConfig() {
		regu = input_regu.getText();
		StringBuffer SB = new StringBuffer();
		if (!config_s.exists()) {
			try {
				config_s.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!config_t.exists()) {
			try {
				config_t.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			BufferedWriter BW = new BufferedWriter(new FileWriter(config_s));
			SB.append(regu + "\n");
			SB.append(path + "\n");
			for (int i = 0; i < srcs.size(); i++) {
				SB.append(srcs.get(i) + "\t" + dests.get(i) + "\n");
			}
			BW.write(SB.toString());
			BW.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SB = new StringBuffer();
		try {
			System.out.println("OVERRIDE TAGS");
			BufferedWriter BW = new BufferedWriter(new FileWriter(config_t));
			Set<String> ks = tagtc.keySet();
			Iterator<String> it = ks.iterator();
			while (it.hasNext()) {
				String k = it.next();
				ArrayList<String> type = tagtc.get(k).get(0);
				ArrayList<String> constraint = tagtc.get(k).get(1);
				String value = tagv.get(k);
				for (int i = 0; i < type.size(); i++) {
					SB.append(k + "\t" + type.get(i) + "\t" + constraint.get(i) + "\t" + value + "\n");
				}
				System.out.println(tagv);
			}
			BW.write(SB.toString());
			BW.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void move(int[] rows, int dir) {
		int temp = -1;
		String stemp = null;
		switch (dir) {
		case -1:
			for (int i = 0; i < rows.length - 1; i++) {
				for (int n = i + 1; n < rows.length; n++) {
					if (rows[n] < rows[i]) {
						temp = rows[n];
						rows[n] = rows[i];
						rows[i] = temp;
					}
				}
			}
			for (int i = 0; i < rows.length; i++) {
				if (rows[i] <= 0) {
					while (i + 1 < rows.length && rows[i + 1] - rows[i] <= 1) {
						i++;
					}
				} else {
					stemp = srcs.get(rows[i]);
					DTM.setValueAt(stemp, rows[i] - 1, 0);
					DTM.setValueAt(srcs.get(rows[i] - 1), rows[i], 0);
					srcs.set(rows[i], srcs.get(rows[i] - 1));
					srcs.set(rows[i] - 1, stemp);
					stemp = dests.get(rows[i]);
					DTM.setValueAt(stemp, rows[i] - 1, 1);
					DTM.setValueAt(dests.get(rows[i] - 1), rows[i], 1);
					dests.set(rows[i], dests.get(rows[i] - 1));
					dests.set(rows[i] - 1, stemp);
					rows[i] -= 1;
				}
			}
			break;
		case 1:
			for (int i = 0; i < rows.length - 1; i++) {
				for (int n = i + 1; n < rows.length; n++) {
					if (rows[n] > rows[i]) {
						temp = rows[n];
						rows[n] = rows[i];
						rows[i] = temp;
					}
				}
			}
			for (int i = 0; i < rows.length; i++) {
				if (rows[i] + 1 >= srcs.size()) {
					while (i + 1 < rows.length && rows[i] - rows[i + 1] <= 1) {
						i++;
					}
				} else {
					stemp = srcs.get(rows[i]);
					DTM.setValueAt(stemp, rows[i] + 1, 0);
					DTM.setValueAt(srcs.get(rows[i] + 1), rows[i], 0);
					srcs.set(rows[i], srcs.get(rows[i] + 1));
					srcs.set(rows[i] + 1, stemp);
					stemp = dests.get(rows[i]);
					DTM.setValueAt(stemp, rows[i] + 1, 1);
					DTM.setValueAt(dests.get(rows[i] + 1), rows[i], 1);
					dests.set(rows[i], dests.get(rows[i] + 1));
					dests.set(rows[i] + 1, stemp);
					rows[i] += 1;
				}
			}
			break;
		}
		body.clearSelection();
		for (int row : rows) {
			body.addRowSelectionInterval(row, row);
		}
	}

	public String getRegu() {
		return regu;
	}

	public String getPath() {
		path = input_path.getText();
		return path;
	}

	public JDialog getDialog() {
		return error;
	}

	public void scrollTo(JTable table, int row) {
		table.scrollRectToVisible(table.getCellRect(row, 0, true));
	}

	public boolean getStopflag() {
		return stopflag;
	}

	public void setState(int S) {
		state = S;
		JButton commit = getActions().get("commit");
		switch (state) {
		case 0:
			commit.setEnabled(true);
			commit.setText("commit");
			break;
		case 1:
			commit.setEnabled(false);
			commit.setText("busy...");
			break;
		}
	}

	public void addLog(String info, String time) {
		LDTM.addRow(new String[] { info.trim(), time });
		scrollTo(log, log.getRowCount() - 1);
	}

	public void addChange(String src, String dest) {
		DTM.addRow(new String[] { src, dest });
		scrollTo(body, body.getRowCount() - 1);
	}

	public void addChange_t(String tag, String type, String con, String val) {
		DTM_T.addRow(new String[] { tag, type, con, val });
		scrollTo(body, body.getRowCount() - 1);
	}

	public void loadTopPanel(String T) {
		panel_top.removeAll();
		buttons.put("add", new JButton("add"));
		buttons.put("remove", new JButton("remove"));
		buttons.put("commit", new JButton("commit"));
		buttons.put("up", new JButton("↑"));
		buttons.put("down", new JButton("↓"));
		JPanel panel_top_r0 = new JPanel();
		JPanel panel_top_rb = new JPanel();
		panel_top.add(panel_top_r0);
		switch (T) {
		case "text":
			panel_top.setLayout(new GridLayout(2, 1));
			panel_top_r0.setLayout(new GridLayout(2, 2));
			panel_top_rb.setLayout(new GridLayout(1, 5));
			JTextField input_before = new JTextField(17);
			JTextField input_after = new JTextField(17);
			panel_top_r0.add(new JLabel("BEFORE:"));
			panel_top_r0.add(new JLabel("AFTER:"));
			panel_top_r0.add(input_before);
			panel_top_r0.add(input_after);
			panel_top.setMinimumSize(
					new Dimension(panel_main.getWidth(), input_before.getHeight() + buttons.get("add").getHeight()));
			buttons.get("add").addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String before = input_before.getText();
					String after = input_after.getText();
					srcs.add(before);
					dests.add(after);
					addChange(before, after);
					System.out.println("ADD");
				}

			});

			buttons.get("remove").addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					int[] inds = body.getSelectedRows();
					ArrayList<Integer> removedinds = new ArrayList<Integer>();
					for (int i : inds) {
						for (int ri : removedinds) {
							if (ri <= i) {
								i--;
							}
						}
						srcs.remove(i);
						dests.remove(i);
						DTM.removeRow(i);
						removedinds.add(i);
					}
					removedinds.clear();
					System.out.println("REMOVE");
				}

			});

			break;
		case "tag":
			panel_top.setLayout(new GridLayout(3, 1));
			panel_top_r0.setLayout(new GridLayout(2, 3));
			JPanel panel_top_r1 = new JPanel();
			panel_top_r1.setLayout(new GridLayout(2, 1));
			panel_top_rb.setLayout(new GridLayout(1,3));
			JTextField input_tag = new JTextField();
			JComboBox<String> input_type = new JComboBox<String>();
			input_type.addItem("not");
			for (int i = 0; i < 10; i++) {
				input_type.addItem(String.valueOf(i + 1));
			}
			JTextField input_constraint = new JTextField();
			JTextArea input_value = new JTextArea();
			JScrollPane vs = new JScrollPane(input_value);
			vs.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			vs.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			panel_top_r0.add(new JLabel("TAG:"));
			panel_top_r0.add(new JLabel("TYPE:"));
			panel_top_r0.add(new JLabel("MATCH:"));
			panel_top_r0.add(input_tag);
			panel_top_r0.add(input_type);
			panel_top_r0.add(input_constraint);
			panel_top_r1.add(new JLabel("VALUE:"));
			panel_top_r1.add(vs);
			panel_top.add(panel_top_r1);

			buttons.get("add").addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String tag = input_tag.getText();
					String type = (String) input_type.getSelectedItem();
					String constraint = input_constraint.getText();
					String value = input_value.getText();
					addChange_t(tag, type, constraint, value);
					System.out.println("ADD");
				}

			});

			buttons.get("remove").addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					int[] inds = body.getSelectedRows();
					ArrayList<Integer> removedinds = new ArrayList<Integer>();
					for (int i : inds) {
						for (int ri : removedinds) {
							if (ri <= i) {
								i--;
							}
						}

						DTM_T.removeRow(i);
						removedinds.add(i);
					}
					removedinds.clear();
					System.out.println("REMOVE");
				}

			});
			break;
		}
		buttons.get("up").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int rows[] = body.getSelectedRows();
				move(rows, -1);
			}

		});

		buttons.get("down").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int rows[] = body.getSelectedRows();
				move(rows, 1);
			}

		});
		buttons.get("commit").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new Thread(new Demo()).start();
			}

		});
		panel_top_rb.add(buttons.get("add"));
		panel_top_rb.add(buttons.get("remove"));
		if(mode) {
			panel_top_rb.add(buttons.get("up"));
			panel_top_rb.add(buttons.get("down"));
			System.out.println(mode);
		}
		panel_top_rb.add(buttons.get("commit"));
		panel_top.add(panel_top_rb);
		if (System.currentTimeMillis() - LAUNCH_TIME > 1000) {
			adaptSize();
		}
		panel_top.repaint();
	}

	public void adaptSize() {
		int width = panel_top.getWidth();
		double mwidth = getWidth();
		/*
		 * if (mwidth < size.width) { panel_top.setPreferredSize(new Dimension(width,
		 * height_top * (mode?2:3))); } else { panel_top.setPreferredSize(new
		 * Dimension(width, height_top * (mode?1:2))); }
		 */
		int x = panel_body.getX();
		int y = panel_top.getY() + panel_top.getHeight();
		panel_body.setLocation(x, y);
		panel_left.setPreferredSize(new Dimension((int) (mwidth * ratio), panel_log.getHeight()));
	}

	public boolean getMode() {
		return mode;
	}
	
	public int getDepth() {
		int d = (int)input_depth.getValue();
		return d+1;
	}
}
