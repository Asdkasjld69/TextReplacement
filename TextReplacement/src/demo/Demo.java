package demo;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;

import ok.Layout_Text;

public class Demo implements Runnable {
	public static String title = "Replace Text";
	private static File config_path = new File("config");
	private static File config_s = new File(config_path.getPath()+"/StringToReplace.txt");
	private static File config_t = new File(config_path.getPath()+"/TagToReplace.txt");
	private static File lock_flag = new File(config_path.getPath()+"/flag");
	private static Layout_Text L = null;
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		if(!config_path.exists()) {
			config_path.mkdirs();
		}
		
		FileOutputStream lck = new FileOutputStream(lock_flag);
		FileLock lc = lck.getChannel().tryLock();         
	    if(lc == null) {
	      System.out.println("A previous instance is already running....");
	      HWND hwnd = User32.INSTANCE.FindWindow(null, title);
	      User32.INSTANCE.ShowWindow(hwnd, 1);
	      User32.INSTANCE.SetForegroundWindow(hwnd);
	      WinDef.RECT rect = new WinDef.RECT();
	      User32.INSTANCE.GetWindowRect(hwnd, rect);
	      int width = rect.right-rect.left;
	      int height = rect.bottom-rect.top;
	      Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	      User32.INSTANCE.MoveWindow(hwnd, rect.left<0?0:rect.right>dim.width?dim.width-width:rect.left, rect.top<0?0:rect.bottom>dim.height?dim.height-height:rect.top, width, height, true);
	      lck.close();
	      System.exit(1);
	    }
	    
	    System.out.println("This is the first instance of this program...");
	 
	    L = new Layout_Text();
		L.loadConfig();
		Map<String, JButton> buttons = L.getActions();		
		
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
		L.overrideConfig();
		L.setState(1);
		ArrayList<File> files = iterFile(path,L.getRegu());
		L.commitChanges(files);
		L.setState(0);
	}

}
