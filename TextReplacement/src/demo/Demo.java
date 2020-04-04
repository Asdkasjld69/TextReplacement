package demo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import ok.Layout_Text;
import ok.StringReplacement;
import ok.TextDataReader;
import ok.XmlBlockReplacement;

public class Demo implements Runnable {

	private static File config = new File("config/StringToReplace.txt");
	
	private static Layout_Text L = new Layout_Text();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		L.loadConfig(config);
		Map<String, JButton> buttons = L.getActions();
		buttons.get("add").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("ADD");
			}
			
		});
		
		buttons.get("remove").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("REMOVE");
			}
			
		});
		
		buttons.get("commit").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new Thread(new Demo()).start();
			}
			
		});
		/*
		System.out.println("================BLOCK=================");
		String[][] tag = {{"Controller","1","1","1","1","not"},{"Node","1","1"},{"Controller",""}};
		Object[][] src2 = {{"filterType=\"pos\"","filterType=\"posx\"","filterType=\"posy\"","filterType=\"posz\"","Bone_Spine"},{"name=\"Bone0","name=\"Protag\""},{"filterType=\"scale\""}};
		Object[] dest2 = {"","",""};
		for(File file:files) {
			XmlBlockReplacement.replace(file, tag , src2, dest2);
		}
		*/
		
		/*
		File[] rts = File.listRoots();
		long start = System.currentTimeMillis();
		for(File rt:rts) {
			System.out.println(rt.getAbsolutePath());
			ArrayList<File> fs = iterDir(rt);
			if(!fs.isEmpty()) {
				for(File f:fs) {
					System.out.println(f.getAbsolutePath());
				}
			}
		}
		System.out.println("time lapse:"+(System.currentTimeMillis()-start)+"ms");*/
	}
	
	public static ArrayList<File> iterFile(File file,String suffix) {
		ArrayList<File> fl = new ArrayList<File>();
		if(file.isDirectory()) {
			File[] files = file.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					// TODO Auto-generated method stub
					if(pathname.isDirectory()||pathname.getName().matches(suffix)) {
						return true;
					}
					return false;
				}
				
			});
			if(files!=null) {
				for(File f:files) {
					if(L.getStopflag()) {
						break;
					}
					ArrayList<File> itl = iterFile(f,suffix);
					fl.addAll(itl);
				}
			}
		}
		else {
			fl.add(file);
		}
		return fl;
	}
	
	public static ArrayList<File> iterFile(File file) {
		ArrayList<File> fl = new ArrayList<File>();
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			if(files!=null) {
				for(File f:files) {
					if(L.getStopflag()) {
						break;
					}
					ArrayList<File> itl = iterFile(f);
					fl.addAll(itl);
				}
			}
		}
		else {
			fl.add(file);
		}
		return fl;
	}
	
	public static ArrayList<File> iterDir(File file) {
		ArrayList<File> fl = new ArrayList<File>();
		if(file.isDirectory()) {
			fl.add(file);
			File[] files = file.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					// TODO Auto-generated method stub
					if(pathname.isDirectory()) {
						return true;
					}
					return false;
				}
				
			});
			if(files!=null) {
				for(File f:files) {
					if(L.getStopflag()) {
						break;
					}
					ArrayList<File> itl = iterDir(f);
					fl.addAll(itl);
				}
			}
		}
		return fl;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		File path = new File(L.getPath());
		if(!path.exists()) {
			JOptionPane.showConfirmDialog(L, "Non-existing path", "Error", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if(JOptionPane.showConfirmDialog(L,"<html>All Files following the <font color='red'><b>Regu</b></font> under <u>\""+path.getAbsolutePath()+"\"</u> will be checked recrusively!</html>","Are you sure?" ,JOptionPane.OK_OPTION)!=0) {
			return;
		}
		L.overrideConfig(config);
		L.setState(1);
		ArrayList<File> files = iterFile(path,L.getRegu());
		L.commitChanges(files);
		L.setState(0);
	}

}
