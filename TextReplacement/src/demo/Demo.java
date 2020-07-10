package demo;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;

import ok.Layout_Text;

public class Demo implements Runnable {
	public static String title = "Replace Text";
	private static File config_path = new File("config");
	private static File lock_flag = new File(config_path.getPath()+"/flag");
	private static Layout_Text L = null;
	private static boolean ABORT_FLAG = false;
	
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
	}
	
	public static ArrayList<File> iterFile(File file,String suffix,int depth) {
		ArrayList<File> fl = new ArrayList<File>();
		if(depth<0) {
			return fl;
		}
		if(file.isDirectory()) {
			File[] files = file.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					// TODO Auto-generated method stub
					if(ABORT_FLAG) {
						return false;
					}
					boolean flag = false;
					try {
						flag = (pathname.getName().matches(suffix)&&pathname.length()<=L.getSizeThreshold()*1024*1024);
					}
					catch(Exception e) {
						ABORT_FLAG = true;
						L.addRow(new String[] {"Filename Regrex Syntax Error #FAILED", new Date().toString()}, L.getTms().get(-1), L.getLog());
					}
					if(pathname.isDirectory()||flag) {
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
					ArrayList<File> itl = iterFile(f,suffix,depth-1);
					fl.addAll(itl);
				}
			}
		}
		else {
			fl.add(file);
		}
		return fl;
	}
	
	public static ArrayList<File> iterFile(File file,int depth) {
		ArrayList<File> fl = new ArrayList<File>();
		if(depth<0) {
			return fl;
		}
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			if(files!=null) {
				for(File f:files) {
					if(L.getStopflag()) {
						break;
					}
					ArrayList<File> itl = iterFile(f,depth-1);
					fl.addAll(itl);
				}
			}
		}
		else {
			fl.add(file);
		}
		return fl;
	}
	
	public static ArrayList<File> iterDir(File file,int depth) {
		ArrayList<File> fl = new ArrayList<File>();
		if(depth<0) {
			return fl;
		}
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
					ArrayList<File> itl = iterDir(f,depth-1);
					fl.addAll(itl);
				}
			}
		}
		return fl;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ABORT_FLAG = false;
		File path = new File(L.getPath());
		if(!path.exists()) {
			JOptionPane.showConfirmDialog(L, "Non-existing path", "Error", JOptionPane.WARNING_MESSAGE);
			return;
		}
		L.applyConfig();
		if(L.getQualify().containsValue(false)) {
			JOptionPane.showConfirmDialog(L, "Regrex Syntax Error!", "Error", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if(JOptionPane.showConfirmDialog(L,"<html>All Files following the <font color='red'><b>Regrex</b></font> under <u>\""+path.getAbsolutePath()+"\"</u> will be checked recrusively!</html>","Are you sure?" ,JOptionPane.OK_OPTION)!=0) {
			return;
		}
		L.setState(1);
		ArrayList<File> files = iterFile(path,L.getRegrex(),L.getDepth());
		if(ABORT_FLAG) {
			L.setState(0);
			return;
		}
		L.commitChanges(files);
		L.overrideConfig();
		L.setState(0);
	}

}
