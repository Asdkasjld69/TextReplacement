package demo;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import ok.StringReplacement;
import ok.TextDataReader;
import ok.XmlBlockReplacement;

public class Demo {
	private static File path = new File("path");
	private static File config = new File("config/StringToReplace.txt");
	private static String[] suffix = {"xaf","xml","txt"};
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		StringBuffer SB = new StringBuffer();
		int i=0;
		for(String suff:suffix) {
			if(i>0) {
				SB.append("|");
			}
			SB.append(".+."+suff);
			i++;
		}
		String suf = SB.toString();
		
		ArrayList<File> files = iterFile(path,suf);
		String conf = TextDataReader.read(config);
		String[] confs = conf.split("\n");
		ArrayList<String> srcs = new ArrayList<String>();
		ArrayList<String> dests = new ArrayList<String>();
		System.out.println("======================================");
		for(String cfs:confs) {
			String[] cs = cfs.split("\t");
			System.out.println("["+cs[0]+" -> "+cs[1]+"]");
			srcs.add(cs[0]);
			dests.add(cs[1]);
		}
		System.out.println("======================================");
		
		System.out.println("================STRING================");
		Object[] src = srcs.toArray();
		Object[] dest = dests.toArray();
		for(File file:files) {
			StringReplacement.replace(file, src, dest);
		}
		
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
					if(pathname.isDirectory()||file.getName().matches(suffix)) {
						return true;
					}
					return false;
				}
				
			});
			if(files!=null) {
				for(File f:files) {
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
					ArrayList<File> itl = iterDir(f);
					fl.addAll(itl);
				}
			}
		}
		return fl;
	}

}
