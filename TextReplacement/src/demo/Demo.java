package demo;

import java.io.File;
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
		ArrayList<File> files = iterDir(path,suffix);
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
			for(String suf:suffix) {
				if(file.getName().matches(".+."+suf)) {
					StringReplacement.replace(file, src, dest);
					break;
				}
			}
		}
		
		System.out.println("================BLOCK=================");
		String[][] tag = {{"Controller","1","1","1","1","not"},{"Node","1","1"},{"Controller",""}};
		Object[][] src2 = {{"filterType=\"pos\"","filterType=\"posx\"","filterType=\"posy\"","filterType=\"posz\"","Bone_Spine"},{"name=\"Bone0","name=\"Protag\""},{"filterType=\"scale\""}};
		Object[] dest2 = {"","",""};
		for(File file:files) {
			for(String suf:suffix) {
				if(file.getName().matches(".+."+suf)) {
					XmlBlockReplacement.replace(file, tag , src2, dest2);
					break;
				}
			}
		}
	}
	
	public static ArrayList<File> iterDir(File file,String[] suffix) {
		ArrayList<File> fl = new ArrayList<File>();
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for(File f:files) {
				ArrayList<File> itl = iterDir(f,suffix);
				for(File itf:itl) {
					fl.add(itf);
				}
			}
		}
		else {
			fl.add(file);
		}
		return fl;
	}

}
