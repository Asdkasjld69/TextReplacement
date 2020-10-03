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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
	private static final String[] headers = { "Before", "After", "Mode" };
	private static final String[] headers_t = { "Tag", "Type", "Match", "Value" };
	private static final String[] headers_f = { "Before", "After", "Index"};
	private static final String[] headers_a = { "File", "Array", "Length"};
	private static final String[] lheaders = { "Message", "Time" };
	private static final String[] modes = {"Text","Tag","Filename","Array","Encode"};
	private static List<String[]> signs;
	private static Map<Integer,DefaultTableModel> tms;
	private String regrex;
	private String path;
	private File config_path;
	private File config;
	private ArrayList<Object[]> change_text;
	private ArrayList<Object[]> srcs_f;
	private ArrayList<Object> dests_f;
	private HashMap<String, ArrayList<ArrayList<String>>> tagtm;
	private HashMap<String, String> tagv;
	private JTextField input_regrex;
	private JTextField input_path;
	private JSpinner input_depth;
	private JSpinner input_size;
	private JTable body;
	private JTable log;
	private JScrollPane panel_body;
	private JScrollPane panel_log;
	private JPanel panel_top;
	private JPanel panel_main;
	private JPanel panel_left;
	private Map<String, JButton> buttons;
	private Map<Object, Boolean> qualify;
	private Dimension size;
	private double ratio;
	private JDialog dialog_about;
	private JDialog error;
	private JLabel intro;
	private JCheckBox check_safe;
	private JComboBox<String> encode;
	private boolean stopflag;
	private int state;
	private int mode;
	public static long LAUNCH_TIME;
	public final String SYSENCODE = "UTF-8";

	public Layout_Text() {
		super();
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		
		signs = new ArrayList<String[]>();
		signs.add(new String[] {"&", "&amp;"});
		signs.add(new String[] {"<", "&lt;"});
		signs.add(new String[] {">", "&gt;"});
		signs.add(new String[] {"'", "&apos;"});
		signs.add(new String[] {"\"", "&quot;"});
		regrex = "";
		path = "path";
		config_path = new File("config");
		config = new File(config_path.getPath() + "/config.xml");
		buttons = new HashMap<String, JButton>();
		qualify = new HashMap<Object, Boolean>();
		encode = new JComboBox<String>();
		encode.addItem("UTF-8");
		encode.addItem("Unicode");
		encode.addItem("GBK");
		encode.setBackground(Color.WHITE);
		JButton button_remove = new JButton("remove");
		JButton button_up = new JButton("↑");
		JButton button_down = new JButton("↓");
		JButton button_commit = new JButton("commit");
		JButtonWithData button_tmode = new JButtonWithData("NORMAL");
		JButtonWithData button_index = new JButtonWithData("INDEX");
		button_tmode.setCycle_text(new String[] {"NORMAL","REGREX(PARTIAL)","REGREX(SINGLE LINE)","REGREX(WRAP LINES)"});
		button_tmode.setCycle(new Object[]{0,1,2,3});
		button_tmode.setColor(0, new Color(255,255,255));
		button_tmode.setColor(1, new Color(128,255,196));
		button_tmode.setColor(2, new Color(128,196,255));
		button_tmode.setColor(3, new Color(196,128,255));
		button_remove.addActionListener(new ActionListener() {

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
					tms.get(mode).removeRow(i);
					removedinds.add(i);
				}
				removedinds.clear();
				System.out.println("REMOVE");
			}

		});
		button_up.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int rows[] = body.getSelectedRows();
				move(rows, -1, tms.get(mode));
			}

		});

		button_down.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int rows[] = body.getSelectedRows();
				move(rows, 1, tms.get(mode));
			}

		});
		button_commit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				qualify.clear();
				int rows = tms.get(mode).getRowCount();
				switch(mode) {
				case 0:for(int i=0;i<rows;i++) {
							qualify.put(new Object[] {i, 0},checkRegrexSyntax(tms.get(0).getValueAt(i, 0).toString()));
						}
						break;
				}
				
				new Thread(new Demo()).start();
			}

		});
		buttons.put("text_mode", button_tmode);
		buttons.put("up", button_up);
		buttons.put("down", button_down);
		buttons.put("remove", button_remove);
		buttons.put("commit", button_commit);
		buttons.put("filename_index", button_index);
		DefaultTableModel DTM = new DefaultTableModel(null, headers) {

			/**
			 * 
			 */
			private static final long serialVersionUID = -2751372245925669029L;

			@Override
			public void setValueAt(Object aValue, int row, int column) {
				// TODO Auto-generated method stub
				String val = aValue.toString();
				if(this.getColumnName(column).equals("Mode")&&val!=null&&!val.equals("")) {
					try {
						int mi = Integer.parseInt(val);
						if(mi<0||mi>3) {
							return;
						}
					}
					catch(Exception e) {
						return;
					}
					super.setValueAt(aValue, row, column);
				}
				else {
					super.setValueAt(aValue, row, column);
				}
			}
			
		};
		DefaultTableModel DTM_T = new DefaultTableModel(null, headers_t);
		DefaultTableModel DTM_F = new DefaultTableModel(null, headers_f) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 5590360337000050184L;

			@Override
			public void setValueAt(Object aValue, int row, int column) {
				// TODO Auto-generated method stub
				String val = aValue.toString();
				if(this.getColumnName(column).equals("Index")&&val!=null&&!val.equals("")) {
					try {
						Integer.parseInt(val);
					}
					catch(Exception e) {
						return;
					}
					super.setValueAt(aValue, row, column);
				}
				else {
					super.setValueAt(aValue, row, column);
				}
			}
		};
		DefaultTableModel DTM_A = new DefaultTableModel(null, headers_a) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 2386873316166318539L;

			@Override
			public void setValueAt(Object aValue, int row, int column) {
				// TODO Auto-generated method stub
				return;
			}
			
		};
		DefaultTableModel LDTM = new DefaultTableModel(null, lheaders) {

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
		tms = new HashMap<Integer,DefaultTableModel>();
		tms.put(-1, LDTM);
		tms.put(0, DTM);
		tms.put(1, DTM_T);
		tms.put(2, DTM_F);
		tms.put(3, DTM_A);
		change_text = new ArrayList<Object[]>();
		srcs_f = new ArrayList<Object[]>();
		dests_f = new ArrayList<Object>();
		tagtm = new HashMap<String, ArrayList<ArrayList<String>>>();
		tagv = new HashMap<String, String>();
		input_regrex = new JTextField(20);
		input_path = new JTextField();
		input_depth = new JSpinner(new SpinnerNumberModel(0,0,100,1));
		input_size = new JSpinner(new SpinnerNumberModel(1.0,0.0,512.0,1.0));
		body = new JTable(DTM) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1417313921920001140L;

			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				// TODO Auto-generated method stub
				Component cell = super.prepareRenderer(renderer, row, column);
				Color back = Color.WHITE;
				Color fore = Color.BLACK;
				switch(mode) {
				case 0:
					boolean flag = true;
					if(column==2) {
						switch(Integer.parseInt(DTM.getValueAt(row, column).toString())) {
						case 1: back = new Color(96,224,164);break;
						case 2:	back = new Color(96,164,224);break;
						case 3: back = new Color(164,96,224);break;
						}
					}
					if(Integer.parseInt(DTM.getValueAt(row, 2).toString())>0) {
						flag = checkRegrexSyntax(DTM.getValueAt(row, 0).toString());
						if(!flag) {
							back = new Color(224,64,64);
						}
					}
					break;
				}
				
				int[] rows = getSelectedRows();
				for(int i=0;i<rows.length;i++) {
					if(row==rows[i]) {
						back = back.darker();
						fore = Color.WHITE;
						break;
					}
				}
				cell.setBackground(back);
				cell.setForeground(fore);
				return cell;
			}
			
		};
		log = new JTable(LDTM) {

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
					String message = mess[mess.length-1];
					if (message.equals("COMMIT")) {
						comp.setBackground(new Color(255, 96, 255));
					}
					if (message.equals("STARTED")) {
						comp.setBackground(new Color(128, 196, 255));
					}
					if (message.matches("FINISHED(.*)")) {
						comp.setBackground(new Color(128, 255, 128));
					}
					if (message.matches("FAILED.*")) {
						comp.setBackground(new Color(255, 96, 96));
						comp.setForeground(new Color(255, 255, 255));
					}
				}
				return comp;
			}
		};
		panel_body = new JScrollPane(body);
		panel_log = new JScrollPane(log);
		panel_top = new JPanel();
		panel_main = new JPanel();
		panel_left = new JPanel();
		
		size = new Dimension(716, 400);
		ratio = 0.4;
		dialog_about = new JDialog();
		error = new JDialog();
		intro = new JLabel(
				"<html>A <font color='#66ccff'>Tool</font> for <font color='red'><b>MASS</b></font> <u>text</u> <i>replacing</i>!</html>");
		check_safe = new JCheckBox("Safe");
		stopflag = false;
		state = 0;
		mode = 0;
		LAUNCH_TIME = System.currentTimeMillis();
		JMenuBar mbar = new JMenuBar();
		JButton about = new JButton("About");
		JMenu switchmode = new JMenu("Mode");
		ActionListener mode_action = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				mode = Integer.parseInt(e.getActionCommand());
				switch(mode) {
				case 0:
					loadTopPanel("text");
					body.setModel(DTM);
					setTitle("Replace Text");break;
				case 1:
					loadTopPanel("tag");
					body.setModel(DTM_T);
					setTitle("Replace Tag");break;
				case 2:
					loadTopPanel("filename");
					body.setModel(DTM_F);
					setTitle("Replace Filename");break;
				case 3:
					loadTopPanel("array");
					body.setModel(DTM_A);
					setTitle("To Array");break;
				}
				for(int i=0;i<switchmode.getItemCount();i++) {
					if(i==mode) {
						switchmode.getItem(i).setText("*"+modes[i]+"*");
					}
					else {
						switchmode.getItem(i).setText(modes[i]);
					}
				}
				panel_top.revalidate();
			}

		};
		for(int i=0;i<modes.length;i++) {
			JMenuItem mode_temp = new JMenuItem(modes[i]);
			mode_temp.setActionCommand(String.valueOf(i));
			switchmode.add(mode_temp);
			mode_temp.addActionListener(mode_action);
		}
		for(int i=0;i<switchmode.getItemCount();i++) {
			if(i==mode) {
				switchmode.getItem(i).setText("*"+modes[i]+"*");
			}
			else {
				switchmode.getItem(i).setText(modes[i]);
			}
		}
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
		mbar.add(new JLabel("PATH:"));
		mbar.add(input_path);
		mbar.add(about);
		this.setJMenuBar(mbar);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setTitle(Demo.title);
		Dimension minsize = new Dimension(648, 400);
		this.setMinimumSize(minsize);
		this.setSize(size);
		this.setLocation((dim.width - this.getWidth()) / 2, (dim.height - this.getHeight()) / 2);
		check_safe.setSelected(true);
		
		JPanel panel_bottom = new JPanel();
		loadTopPanel("text");
		panel_main.setLayout(new BorderLayout());
		panel_bottom.setLayout(new FlowLayout());
		panel_left.setLayout(new BorderLayout());

		JLabel label_regrex = new JLabel("Regrex:");
		JLabel drag = new JLabel(" ");
		
		Cursor cu = new Cursor(Cursor.E_RESIZE_CURSOR);
		drag.setCursor(cu);
		panel_bottom.add(label_regrex);
		panel_bottom.add(input_regrex);
		panel_bottom.add(new JLabel("Depth:"));
		panel_bottom.add(input_depth);
		panel_bottom.add(new JLabel("Size:"));
		panel_bottom.add(input_size);
		panel_bottom.add(new JLabel("MB"));
		panel_bottom.add(check_safe);
		panel_bottom.add(encode);
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
		if (!config.exists()) {
			try {
				config.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String[] conf = TextDataReader.read(config,true,SYSENCODE);
		System.out.println(conf[0]);
		String config = conf[0];
		ArrayList<String> temp = null;
		ArrayList<String> temp2 = null;
		ArrayList<String> temp3 = null;
		temp = scanTag("regrex",config,false);
		if(temp != null) {
			regrex = convertToString(temp.get(0));
		}
		temp = scanTag("path",config,false);
		if(temp != null) {
			path = convertToString(temp.get(0));
		}
		temp = scanTag("encode",config,false);
		if(temp != null) {
			encode.setSelectedItem(convertToString(temp.get(0)));
		}
		temp = scanTag("maxsize",config,false);
		if(temp != null) {
			input_size.setValue(Double.parseDouble(temp.get(0)));
		}
		temp = scanTag("text",config,false);
		if(temp != null) {
			temp2 = scanTag("item",temp.get(0),false);
			if(temp2!=null) {
				for(String item:temp2) {
					temp3 = scanTag("from",item,false);
					String from = "";
					String to = "";
					int mode = 0;
					if(temp3!=null) {
						from = convertToString(temp3.get(0));
					}
					temp3 = scanTag("to",item,false);
					if(temp3!=null) {
						to = convertToString(temp3.get(0));
					}
					temp3 = scanTag("mode",item,false);
					if(temp3!=null) {
						mode = Integer.parseInt(convertToString(temp3.get(0)));
					}
					change_text.add(new Object[] {from,to,mode});
				}
			}
		}
		
		temp = scanTag("tag",config,false);
		ArrayList<String> temp4 = null;
		ArrayList<String> temp5 = null;
		if(temp!=null) {
			temp2 = scanTag("item",temp.get(0),false);
			if(temp2!=null) {
				for(String item:temp2) {
					temp3 = scanTag("target",item);
					if(temp3 != null) {
						temp4 = scanTag("constraint",item,false);
						ArrayList<ArrayList<String>> ttm = new ArrayList<ArrayList<String>>();
						ArrayList<String> tt = new ArrayList<String>();
						ArrayList<String> tm = new ArrayList<String>();
						if(temp4 != null) {
							for(String constraint:temp4) {
								temp5 = scanTag("type",constraint,false);
								if(temp5 != null) {
									tt.add(convertToString((temp5.get(0))));
									temp5 = scanTag("match",constraint,false);
									tm.add(temp5==null?"":convertToString((temp5.get(0))));
								}
								else {
									tt.add("1");
									tm.add("");
								}
							}
						}
						else {
							tt.add("1");
							tm.add("");
						}
						ttm.add(tt);
						ttm.add(tm);
						tagtm.put(convertToString(scanTag("target",temp3.get(0),false).get(0)), ttm);
						temp4 = scanTag("value",item,false);
						tagv.put(convertToString(scanTag("target",temp3.get(0),false).get(0)), temp4.get(0)==null?"":temp4.get(0));
					}
				}
			}
		}
		temp = scanTag("filename",config,false);
		if(temp!=null) {
			temp2 = scanTag("item",temp.get(0),false);
			if(temp2!=null) {
				for(String item:temp2) {
					temp3 = scanTag("from",item,false);
					temp4 = scanTag("index",item,false);
					srcs_f.add(new Object[] {temp3!=null?convertToString(temp3.get(0)):"",temp4!=null?temp4.get(0):""});
					temp3 = scanTag("to",item,false);
					dests_f.add(temp3!=null?convertToString(temp3.get(0)):"");
				}
			}
		}
		
		loadInputs();
		temp = scanTag("log",conf[1],false);
		for (String log:temp) {
			temp2 = scanTag("message",log,false);
			temp3 = scanTag("time",log,false);
			addRow(new String[] {temp2==null?"":temp2.get(0), temp3==null?"":temp3.get(0)},tms.get(-1),this.log);
		}
	}

	public Map<String, JButton> getActions() {
		return buttons;
	}
	
	public void applyConfig() {
		setRegrex(input_regrex.getText());
		setPath(input_path.getText());
	}
	
	public void loadInputs() {
		input_path.setText(path);
		input_regrex.setText(regrex);
		for(int i=0;i<change_text.size();i++) {
			addRow(change_text.get(i),tms.get(0),body);
		}
		Set<String> ks = tagtm.keySet();
		Iterator<String> it = ks.iterator();
		String stemp;
		ArrayList<String> temp;
		ArrayList<String> temp2;
		while(it.hasNext()) {
			stemp = it.next();
			temp = tagtm.get(stemp).get(0);
			temp2 = tagtm.get(stemp).get(1);
			for(int i=0;i<temp.size();i++) {
				addRow(new String[] {stemp,temp.get(i),temp2.get(i),tagv.get(stemp)},tms.get(1),body);
			}
		}
		for(int i=0;i<srcs_f.size();i++) {
			Object[] cons = srcs_f.get(i);
			addRow(new String[] {(String) cons[0],(String) dests_f.get(i),cons.length>1?(String) cons[1]:""},tms.get(2),body);
		}
	}

	public void commitChanges(ArrayList<File> files) {
		String log = "START #COMMIT";
		Date time = new Date();
		addRow(new String[] {log, time.toString()},tms.get(-1),this.log);
		int rows = 0;
		rows = tms.get(mode).getRowCount();
		DefaultTableModel tm = tms.get(mode);
		switch(mode) {
		case 0:
			StringReplacement.setSerial(System.currentTimeMillis());
			StringReplacement.setPath(new File(path).getAbsolutePath());
			change_text.clear();
			for (int i = 0; i < rows; i++) {
				change_text.add(new Object[] {tm.getValueAt(i, 0).toString(),tm.getValueAt(i, 1).toString(),Integer.parseInt(tm.getValueAt(i, 2).toString())});
			}
			for (File file : files) {
				log = StringReplacement.replace(file, change_text, check_safe.isSelected(),(String)encode.getSelectedItem());
				ArrayList<String> temp = scanTag("log",log,false);
				ArrayList<String> temp2 = null;
				ArrayList<String> temp3 = null;
				for (String lo:temp) {
					temp2 = scanTag("message",lo,false);
					temp3 = scanTag("time",lo,false);
					addRow(new String[] {temp2==null?"":temp2.get(0), temp3==null?"":temp3.get(0)},tms.get(-1),this.log);
				}
			}
			break;
		case 1:
			XmlBlockReplacement.setSerial(System.currentTimeMillis());
			XmlBlockReplacement.setPath(new File(path).getAbsolutePath());
			tagtm.clear();
			for (int i = 0; i < rows; i++) {
				ArrayList<ArrayList<String>> tca = new ArrayList<ArrayList<String>>();
				ArrayList<String> ta = new ArrayList<String>();
				ArrayList<String> ca = new ArrayList<String>();
				tca.add(ta);
				tca.add(ca);
				tagtm.put(tm.getValueAt(i, 0).toString(), tca);
			}
			for (int i = 0; i < rows; i++) {
				ArrayList<ArrayList<String>> tca = tagtm.get(tm.getValueAt(i, 0).toString());
				ArrayList<String> ta = tca.get(0);
				ArrayList<String> ca = tca.get(1);
				ta.add(tm.getValueAt(i, 1).toString());
				ca.add(tm.getValueAt(i, 2).toString());
				tagv.put(tm.getValueAt(i, 0).toString(), tm.getValueAt(i, 3).toString());
			}
			for (File file : files) {
				Set<String> ks = tagtm.keySet();
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
					ta.addAll(1, tagtm.get(k).get(0));
					tar.add(ta.toArray());
					ta = tagtm.get(k).get(1);
					car.add(ta.toArray());
					var.add(tagv.get(k));
				}
				log = XmlBlockReplacement.replace(file, tar, car, var.toArray(), check_safe.isSelected());
				ArrayList<String> temp = scanTag("log",log,false);
				ArrayList<String> temp2 = null;
				ArrayList<String> temp3 = null;
				for (String lo:temp) {
					temp2 = scanTag("message",lo,false);
					temp3 = scanTag("time",lo,false);
					addRow(new String[] {temp2==null?"":temp2.get(0), temp3==null?"":temp3.get(0)},tms.get(-1),this.log);
				}
			}
			break;
		case 2:
			FilenameReplacement.setSerial(System.currentTimeMillis());
			FilenameReplacement.setPath(new File(path).getAbsolutePath());
			srcs_f.clear();
			dests_f.clear();
			for(int i=0;i<rows;i++) {
				String before = (String) tm.getValueAt(i, 0);
				String after = (String) tm.getValueAt(i, 1);
				String index = (String) tm.getValueAt(i, 2);
				if(index!=null&&!index.trim().isEmpty()) {
					srcs_f.add(new Object[] {before,index});
				}
				else {
					srcs_f.add(new Object[] {before});
				}
				dests_f.add(after);	
			}
			for(File file:files) {
				log = FilenameReplacement.replace(file, srcs_f, dests_f, check_safe.isSelected(),(String)encode.getSelectedItem());
				ArrayList<String> temp = scanTag("log",log,false);
				ArrayList<String> temp2 = null;
				ArrayList<String> temp3 = null;
				for (String lo:temp) {
					temp2 = scanTag("message",lo,false);
					temp3 = scanTag("time",lo,false);
					addRow(new String[] {temp2==null?"":temp2.get(0), temp3==null?"":temp3.get(0)},tms.get(-1),this.log);
				}
			}
			break;
		case 3:
			while(tms.get(mode).getRowCount()>0) {
				tms.get(mode).removeRow(0);
			}
			for(File file:files) {
				String[] ret = TextDataReader.read(file,false,(String)encode.getSelectedItem());
				StringBuffer arrs = new StringBuffer();
				boolean isFirst = true;
				arrs.append("[");
				for(String s:ret[0].split("\n")) {
					if(!isFirst) {
						arrs.append(",");
					}
					arrs.append("\""+s.trim().replace("\"", "\\\"")+"\"");
					isFirst = false;
				}
				arrs.append("]");
				
				log = ret[1];
				addRow(new Object[] {file.getName(),arrs.toString(),ret[0].split("\n").length},tms.get(mode),this.body);
				ArrayList<String> temp = scanTag("log",log,false);
				ArrayList<String> temp2 = null;
				ArrayList<String> temp3 = null;
				for (String lo:temp) {
					temp2 = scanTag("message",lo,false);
					temp3 = scanTag("time",lo,false);
					addRow(new String[] {temp2==null?"":temp2.get(0), temp3==null?"":temp3.get(0)},tms.get(-1),this.log);
				}
			}
			break;
		}
		log = "END #COMMIT";
		time = new Date();
		addRow(new String[] {log, time.toString()},tms.get(-1),this.log);
	}

	public void overrideConfig() {
		regrex = input_regrex.getText();
		StringBuffer SB = new StringBuffer();
		BufferedWriter BW = null;
		if (!config.exists()) {
			try {
				config.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			BW = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(config),SYSENCODE));
			SB.append("<config>");
			SB.append("<regrex>" + convertToXML(regrex) + "</regrex>");
			SB.append("<path>" + convertToXML(path) + "</path>");
			SB.append("<encode>" + convertToXML((String)encode.getSelectedItem()) + "</encode>");
			SB.append("<maxsize>" + convertToXML(input_size.getValue().toString()) + "</maxsize>");
			System.out.println("OVERRIDE TEXT");
			SB.append("<text>");
			for (Object[] ch:change_text) {
				SB.append("<item>");
				SB.append("<from>" + convertToXML((String) ch[0]) + "</from>");
				SB.append("<to>" + convertToXML((String) ch[1]) + "</to>");
				SB.append("<mode>" + ch[2] + "</mode>");
				SB.append("</item>");
			}
			SB.append("</text>");
			System.out.println("OVERRIDE TAGS");
			SB.append("<tag>");
			Set<String> ks = tagtm.keySet();
			Iterator<String> it = ks.iterator();
			while (it.hasNext()) {
				String k = it.next();
				ArrayList<String> type = tagtm.get(k).get(0);
				ArrayList<String> match = tagtm.get(k).get(1);
				String value = tagv.get(k);
				SB.append("<item>");
				SB.append("<target>"+ convertToXML(k) + "</target>");
				for (int i = 0; i < type.size(); i++) {
					SB.append("<constraint>");
					SB.append("<type>"+ convertToXML(type.get(i)) + "</type>");
					SB.append("<match>"+ convertToXML(match.get(i)) + "</match>");
					SB.append("</constraint>");
				}
				SB.append("<value>"+convertToXML(value)+"</value>");
				SB.append("</item>");
			}
			SB.append("</tag>");
			SB.append("<filename>");
			for(int i=0;i<srcs_f.size();i++) {
				SB.append("<item>");
				SB.append("<from>"+convertToXML((String) srcs_f.get(i)[0])+"</from>");
				if(srcs_f.get(i).length>1) {
					SB.append("<index>"+convertToXML((String) srcs_f.get(i)[1])+"</index>");
				}
				SB.append("<to>"+convertToXML((String) dests_f.get(i))+"</to>");
				SB.append("</item>");
			}
			SB.append("</filename>");
			SB.append("</config>");
			System.out.println(SB.toString());
			System.out.println("OVERRIDING!");
			BW.write(SB.toString());
			BW.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void move(int[] rows, int dir, DefaultTableModel DTM) {
		int temp = -1;
		Object stemp = null;
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
					for(int ci=0;ci<DTM.getColumnCount();ci++) {
						stemp = DTM.getValueAt(rows[i], ci);
						DTM.setValueAt(DTM.getValueAt(rows[i]-1, ci), rows[i], ci);
						DTM.setValueAt(stemp, rows[i] - 1, ci);
					}
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
				if (rows[i] + 1 >= DTM.getRowCount()) {
					while (i + 1 < rows.length && rows[i] - rows[i + 1] <= 1) {
						i++;
					}
				} else {
					for(int ci=0;ci<DTM.getColumnCount();ci++) {
						stemp = DTM.getValueAt(rows[i], ci);
						DTM.setValueAt(DTM.getValueAt(rows[i]+1, ci), rows[i], ci);
						DTM.setValueAt(stemp, rows[i] + 1, ci);
					}
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

	public String getRegrex() {
		return regrex;
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
	
	public void addRow(Object[] data,DefaultTableModel dtm,JTable table) {
		dtm.addRow(data);
		scrollTo(table,table.getRowCount()-1);
	}

	public void loadTopPanel(String T) {
		panel_top.removeAll();
		buttons.put("add", new JButton("add"));
		JPanel panel_top_r0 = new JPanel();
		JPanel panel_top_rb = new JPanel();
		switch (T) {
		case "text":
			panel_top.setLayout(new GridLayout(2, 1));
			panel_top_r0.setLayout(new GridLayout(2, 3));
			panel_top_rb.setLayout(new GridLayout(1, 5));
			JTextField input_before = new JTextField(17);
			JTextField input_after = new JTextField(17);
			
			panel_top_r0.add(new JLabel("BEFORE:"));
			panel_top_r0.add(new JLabel("AFTER:"));
			panel_top_r0.add(new JLabel("MODE:"));
			panel_top_r0.add(input_before);
			panel_top_r0.add(input_after);
			panel_top_r0.add(buttons.get("text_mode"));
			panel_top.setMinimumSize(
					new Dimension(panel_main.getWidth(), input_before.getHeight() + buttons.get("add").getHeight()));
			buttons.get("add").addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String before = input_before.getText();
					String after = input_after.getText();
					/*srcs.add(before);
					dests.add(after);*/
					JButtonWithData mbutt = (JButtonWithData) buttons.get("text_mode");
					addRow(new Object[] {before,after, mbutt.getData()},tms.get(mode),body);
					System.out.println("ADD");
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
			for (int i = 0; i < 10; i++) {
				input_type.addItem("not"+String.valueOf(i + 1));
			}
			for (int i = 0; i < 10; i++) {
				input_type.addItem(String.valueOf(i + 1));
			}
			JTextField input_constraint = new JTextField();
			JTextArea input_value = new JTextArea();
			input_value.setLineWrap(true);
			JScrollPane vs = new JScrollPane(input_value);
			//vs.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
					addRow(new Object[] {tag, type, constraint, value},tms.get(mode),body);
					System.out.println("ADD");
				}

			});
			break;
		case "filename":
			panel_top.setLayout(new GridLayout(2, 1));
			panel_top_r0.setLayout(new GridLayout(2, 3));
			panel_top_rb.setLayout(new GridLayout(1, 5));
			JTextField input_before_name = new JTextField(17);
			JTextField input_after_name = new JTextField(17);
			JButtonWithData button_index = (JButtonWithData) buttons.get("filename_index");
			JSpinner input_index = new JSpinner(new SpinnerNumberModel(0,0,100,1));
			button_index.setTargets(new JComponent[] {input_index});
			input_index.setVisible((boolean) button_index.getData());
			panel_top_r0.add(new JLabel("BEFORE:"));
			panel_top_r0.add(new JLabel("AFTER:"));
			panel_top_r0.add(button_index);
			panel_top_r0.add(input_before_name);
			panel_top_r0.add(input_after_name);
			panel_top_r0.add(input_index);
			panel_top.setMinimumSize(
					new Dimension(panel_main.getWidth(), input_before_name.getHeight() + buttons.get("add").getHeight()));
			buttons.get("add").addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String before = input_before_name.getText();
					String after = input_after_name.getText();
					boolean flag = false;
					flag = (boolean) button_index.getData();
					Object[] before_full = flag?new String[2]:new String[1];
					before_full[0]=before;
					if(flag) {
						before_full[1]=input_index.getValue().toString();
					}
					/*srcs_f.add(before_full);
					dests_f.add(after);*/
					addRow(new Object[] { before_full[0],after,flag? before_full[1]:""},tms.get(mode),body);
					System.out.println("ADD");
				}

			});
			break;
		case "array":
			panel_top.setLayout(new GridLayout(1, 1));
			panel_top_r0.setLayout(new GridLayout(1, 2));
			panel_top_rb.setLayout(new GridLayout(1, 1));
			break;
		}
		switch(T) {
		case "array":
			panel_top_rb.add(buttons.get("commit"));
			break;
		default:
			panel_top.add(panel_top_r0);
			panel_top_rb.add(buttons.get("add"));
			panel_top_rb.add(buttons.get("remove"));
			panel_top_rb.add(buttons.get("up"));
			panel_top_rb.add(buttons.get("down"));
			panel_top_rb.add(buttons.get("commit"));
		}
		panel_top.add(panel_top_rb);
		if (System.currentTimeMillis() - LAUNCH_TIME > 1000) {
			adaptSize();
		}
		panel_top.repaint();
	}

	public void adaptSize() {
		double mwidth = getWidth();
		int x = panel_body.getX();
		int y = panel_top.getY() + panel_top.getHeight();
		panel_body.setLocation(x, y);
		panel_left.setPreferredSize(new Dimension((int) (mwidth * ratio), panel_log.getHeight()));
	}
	
	public ArrayList<String> scanTag(String tag, String src) {
		ArrayList<String> ret = new ArrayList<String>();
		String rets = src;
		String target_s = "<"+tag+">";
		String target_e = "</"+tag+">";
		while(rets.contains(target_s)&&rets.contains(target_e)) {
			int start = rets.indexOf(target_s);
			int end = start+rets.substring(start).indexOf(target_e);
			if(start>-1 && start<=end) {
				ret.add(rets.substring(start, end+target_e.length()+1));
			}
			rets = rets.substring(end+target_e.length()+1);
		}
		return ret.size()>0?ret:null;
	}
	
	public ArrayList<String> scanTag(String tag, String src, boolean keep) {
		ArrayList<String> ret = new ArrayList<String>();
		String rets = src;
		String target_s = "<"+tag+">";
		String target_e = "</"+tag+">";
		while(rets.contains(target_s)&&rets.contains(target_e)) {
			int start = rets.indexOf(target_s);
			int end = start+rets.substring(start).indexOf(target_e);
			if(start>-1 && start<=end) {
				ret.add(rets.substring(start+(keep?0:target_s.length()), end+(keep?target_e.length()+1:0)));
			}
			rets = rets.substring(end+target_e.length());
		}
		return ret.size()>0?ret:null;
	}
	
	public static String convertToXML(String s) {
		String ret = s;
		for(String[] sign:signs) {
			ret = ret.replace(sign[0], sign[1]);
		}
		return ret;
	}
	
	public static String convertToString(String s) {
		String ret = s;
		for(String[] sign:signs) {
			ret = ret.replace(sign[1], sign[0]);
		}
		return ret;
	}

	public int getMode() {
		return mode;
	}
	
	public int getDepth() {
		int d = (int)input_depth.getValue();
		return d+1;
	}

	public void setRegrex(String regrex) {
		this.regrex = regrex;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public JTable getBody() {
		return body;
	}

	public void setBody(JTable body) {
		this.body = body;
	}

	public JTable getLog() {
		return log;
	}

	public void setLog(JTable log) {
		this.log = log;
	}

	public Map<Integer, DefaultTableModel> getTms() {
		return tms;
	}
	
	public Map<Object, Boolean> getQualify() {
		return qualify;
	}

	public JSpinner getInput_size() {
		return input_size;
	}

	public void setInput_size(JSpinner input_size) {
		this.input_size = input_size;
	}

	public boolean checkRegrexSyntax(String reg) {
		boolean flag = true;
		String test = "TEST";
		try {
			test.matches(reg);
		}
		catch(Exception e) {
			flag = false;
		}
		return flag;
	}
}
