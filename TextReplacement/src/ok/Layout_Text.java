package ok;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class Layout_Text {
	private static String[] headers = { "Before", "After" };
	private static String[] lheaders = { "Message", "Time" };
	private static String[][] values = { { "EXAMPLE", "EXAMPLE" } };
	private String regu = "";
	private DefaultTableModel DTM = new DefaultTableModel(null, headers);
	private DefaultTableModel LDTM = new DefaultTableModel(null, lheaders) {

		@Override
		public void setValueAt(Object aValue, int row, int column) {
			// TODO Auto-generated method stub
			return;
		}

	};
	private ArrayList<String> srcs = new ArrayList<String>();
	private ArrayList<String> dests = new ArrayList<String>();
	private JTextField input_regu = new JTextField(32);
	private JButton add = new JButton("add");
	private JButton remove = new JButton("remove");
	private JButton commit = new JButton("commit");
	private JButton up = new JButton("up");
	private JButton down = new JButton("down");
	private Map<String, JButton> buttons = new HashMap<String, JButton>();
	private Dimension size = new Dimension(716,400);
	private double ratio = 0.4;
	private JDialog dialog_about = new JDialog();
	JLabel intro = new JLabel("<html>A <font color='#66ccff'>Tool</font> for <font color='red'><b>MASS</b></font> <u>text</u> <i>replacing</i>!</html>");
	
	public Layout_Text() {
		JFrame main = new JFrame();
		JMenuBar mbar = new JMenuBar();
		JButton about = new JButton("About");
		JButton switchmode = new JButton("Tag");
		about.setBackground(new Color(238,238,238));
		switchmode.setBackground(new Color(112,204,255));
		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dialog_about.setVisible(true);
				dialog_about.setLocation(main.getX(), main.getY());
			}
			
		});
		dialog_about.setTitle("About me");
		dialog_about.setSize(224, 128);
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
		main.setJMenuBar(mbar);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		main.setTitle("Replace Text");
		Dimension minsize = new Dimension(600,400);
		main.setMinimumSize(minsize);
		main.setSize(size);
		main.setLocation((dim.width - main.getWidth()) / 2, (dim.height - main.getHeight()) / 2);
		JPanel panel_main = new JPanel();
		JPanel panel_top = new JPanel();
		JPanel panel_bottom = new JPanel();
		JPanel panel_left = new JPanel();
		JTable body = new JTable(DTM);
		JTable log = new JTable(LDTM) {

			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				// TODO Auto-generated method stub
				Component comp = super.prepareRenderer(renderer, row, column);
				comp.setBackground(new Color(255,255,255));
				comp.setForeground(new Color(0,0,0));
				String[] mess = LDTM.getValueAt(row, 0).toString().split("#");
				if(mess.length>0) {
					if(mess[mess.length-1].equals("COMMIT")) {
						comp.setBackground(new Color(255,64,255));
					}
					if(mess[mess.length-1].equals("STARTED")) {
						comp.setBackground(new Color(64,64,255));
						comp.setForeground(new Color(255,255,255));
					}
					if(mess[mess.length-1].contains("FINISHED")) {
						comp.setBackground(new Color(64,255,64));
					}
					if(mess[mess.length-1].contains("FAILED")) {
						comp.setBackground(new Color(255,64,64));
						comp.setForeground(new Color(255,255,255));
					}
				}
				return comp;
			}
			
		};
		panel_main.setLayout(new BorderLayout());
		panel_top.setLayout(new FlowLayout());
		panel_bottom.setLayout(new FlowLayout());
		panel_left.setLayout(new BorderLayout());
		JScrollPane panel_body = new JScrollPane(body);
		JScrollPane panel_log = new JScrollPane(log);
		JTextField input_before = new JTextField(16);
		JTextField input_after = new JTextField(16);
		JLabel connect = new JLabel("->");
		JLabel label_regu = new JLabel("Regu:");
		JLabel drag = new JLabel(" ");
		Cursor cu = new Cursor(Cursor.E_RESIZE_CURSOR);
		drag.setCursor(cu);
		buttons.put("add", add);
		buttons.put("remove", remove);
		buttons.put("commit", commit);
		buttons.put("up", up);
		buttons.put("down", down);
		panel_top.add(input_before);
		panel_top.add(connect);
		panel_top.add(input_after);
		panel_top.add(add);
		panel_top.add(remove);
		panel_top.add(up);
		panel_top.add(down);
		panel_top.add(commit);
		panel_top.setMinimumSize(new Dimension(panel_main.getWidth(), input_before.getHeight() + add.getHeight()));
		panel_bottom.add(label_regu);
		panel_bottom.add(input_regu);
		panel_left.add(panel_log);
		panel_left.add(drag,BorderLayout.EAST);
		panel_main.add(panel_top, BorderLayout.NORTH);
		panel_main.add(panel_body, BorderLayout.CENTER);
		panel_main.add(panel_bottom, BorderLayout.SOUTH);
		panel_main.add(panel_left,BorderLayout.WEST);
		main.add(panel_main);
		panel_main.setVisible(true);
		panel_top.setVisible(true);
		panel_body.setVisible(true);
		panel_bottom.setVisible(true);
		panel_log.setVisible(true);
		panel_log.setPreferredSize(new Dimension((int)(main.getWidth()*ratio),panel_log.getHeight()));
		main.setVisible(true);
		int height_top = panel_top.getHeight();
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String before = input_before.getText();
				String after = input_after.getText();
				srcs.add(before);
				dests.add(after);
				DTM.addRow(new String[] { before, after });
			}

		});

		remove.addActionListener(new ActionListener() {

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
			}

		});

		up.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int rows[] = body.getSelectedRows();
				move(rows,-1);
			}

		});
		
		down.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int rows[] = body.getSelectedRows();
				move(rows,1);
			}

		});
		
		drag.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				int mousex = e.getXOnScreen();
				panel_left.setPreferredSize(new Dimension(mousex-panel_left.getLocationOnScreen().x<100?100:main.getWidth()-(mousex-panel_left.getLocationOnScreen().x)<100?main.getWidth()-100:mousex-panel_left.getLocationOnScreen().x,panel_left.getHeight()));
				panel_left.revalidate();
				ratio = (double)panel_left.getWidth()/panel_main.getWidth();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	
		main.addWindowStateListener(new WindowStateListener() {

			@Override
			public void windowStateChanged(WindowEvent e) {
				// TODO Auto-generated method stub
				int width = panel_top.getWidth();
				double mwidth = main.getWidth();
				if (mwidth < size.width) {
					panel_top.setPreferredSize(new Dimension(width, height_top * 2));
				} else {
					panel_top.setPreferredSize(new Dimension(width, height_top));
				}

				int x = panel_body.getX();
				int y = panel_top.getY() + panel_top.getHeight();
				panel_body.setLocation(x, y);
				panel_left.setPreferredSize(new Dimension((int)(mwidth*ratio),panel_log.getHeight()));
			}
			
		});
		panel_main.addComponentListener(new ComponentListener() {

			@Override
			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub
				int width = panel_top.getWidth();
				double mwidth = main.getWidth();
				if (mwidth < size.width) {
					panel_top.setPreferredSize(new Dimension(width, height_top * 2));
				} else {
					panel_top.setPreferredSize(new Dimension(width, height_top));
				}

				int x = panel_body.getX();
				int y = panel_top.getY() + panel_top.getHeight();
				panel_body.setLocation(x, y);
				panel_left.setPreferredSize(new Dimension((int)(mwidth*ratio),panel_log.getHeight()));
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
		//main.setResizable(false);
	}

	public void loadConfig(File config) {
		if (!config.exists()) {
			String p = config.getPath().replace(config.getName(), "");
			File pa = new File(p);
			if(!pa.exists()) {
				pa.mkdirs();
			}
			try {
				config.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		String[] conf = TextDataReader.read(config);
		boolean reguflag = true;
		if(conf[0].length()>0) {
			String[] confs = conf[0].split("\n");
			System.out.println("======================================");
			for (String cfs : confs) {
				if(reguflag) {
					reguflag = false;
					regu=cfs;
					input_regu.setText(cfs);
					continue;
				}
				String[] cs = cfs.split("\t");
				System.out.println("[" + cs[0] + " -> " + cs[1] + "]");
				srcs.add(cs[0]);
				dests.add(cs[1]);
				DTM.addRow(new String[] { cs[0], cs[1] });
			}
			System.out.println("======================================");
		}
		String[] logs = conf[1].split("\n");
		for(String l:logs) {
			String[] log = l.split("\t");
			LDTM.addRow(new String[] {log[0],log[1]});
		}
	}

	public Map<String, JButton> getActions() {
		return buttons;
	}

	public void commitChanges(ArrayList<File> files) {
		int rows = DTM.getRowCount();
		srcs = new ArrayList<String>();
		dests = new ArrayList<String>();
		for(int i=0;i<rows;i++) {
			srcs.add(DTM.getValueAt(i, 0).toString());
			dests.add(DTM.getValueAt(i, 1).toString());
		}
		Object[] src = srcs.toArray();
		Object[] dest = dests.toArray();
		String log = "COMMIT #COMMIT";
		Date time =new Date();
		LDTM.addRow(new String[] {log,time.toString()});
		for (File file : files) {
			log = StringReplacement.replace(file, src, dest);
			String[] lo = log.split("\n");
			for(String l:lo) {
				String[] m = l.split("\t");
				LDTM.addRow(new String[] {m[0],m[1]});
			}
		}
	}

	public void overrideConfig(File config) {
		regu = input_regu.getText();
		StringBuffer SB = new StringBuffer();
		if (!config.exists()) {
			try {
				config.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			BufferedWriter BW = new BufferedWriter(new FileWriter(config));
			SB.append(regu+"\n");
			for (int i = 0; i < srcs.size(); i++) {
				SB.append(srcs.get(i) + "\t" + dests.get(i) + "\n");
			}
			BW.write(SB.toString());
			BW.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void move(int[] rows,int dir) {
		int temp = -1;
		String stemp = null;
		switch(dir) {
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
				System.out.print(rows[i]);
				if (rows[i] <= 0) {
					while (i + 1 < rows.length && rows[i + 1] - rows[i] <= 1) {
						i++;
					}
				} else {
					stemp = srcs.get(rows[i]);
					DTM.setValueAt(stemp, rows[i]-1, 0);
					DTM.setValueAt(srcs.get(rows[i]-1), rows[i], 0);
					srcs.set(rows[i], srcs.get(rows[i]-1));
					srcs.set(rows[i]-1, stemp);
					stemp = dests.get(rows[i]);
					DTM.setValueAt(stemp, rows[i]-1, 1);
					DTM.setValueAt(dests.get(rows[i]-1), rows[i], 1);
					dests.set(rows[i], dests.get(rows[i]-1));
					dests.set(rows[i]-1, stemp);
				}
			}break;
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
				System.out.print(rows[i]);
				if (rows[i]+1 >= srcs.size()) {
					while (i + 1 < rows.length && rows[i] - rows[i+1] <= 1) {
						i++;
					}
				} else {
					stemp = srcs.get(rows[i]);
					DTM.setValueAt(stemp, rows[i]+1, 0);
					DTM.setValueAt(srcs.get(rows[i]+1), rows[i], 0);
					srcs.set(rows[i], srcs.get(rows[i]+1));
					srcs.set(rows[i]+1, stemp);
					stemp = dests.get(rows[i]);
					DTM.setValueAt(stemp, rows[i]+1, 1);
					DTM.setValueAt(dests.get(rows[i]+1), rows[i], 1);
					dests.set(rows[i], dests.get(rows[i]+1));
					dests.set(rows[i]+1, stemp);
				}
			}break;
		}
	}
	
	public String getRegu() {
		return regu;
	}
}
