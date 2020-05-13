/**
 * 
 */
package ok;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.poi.hssf.record.BackupRecord;

/**
 * @author cnmbx
 *
 */
public class FilenameReplacement {
	private static long serial = System.currentTimeMillis();
	public static String replace(File F,ArrayList<Object[]> srcs,ArrayList<Object> dests,boolean safe) {
		BufferedReader BR = null;
		BufferedWriter BW = null;
		File backup = null;
		StringBuffer log = new StringBuffer();
		String filepath = F.getAbsolutePath();
		String filename = F.getName();
		Date time = new Date();
		log.append(F.getName()+" #STARTED\t"+time.toString()+"\n");
		if(safe) {
			backup = new File(F.getAbsolutePath().replace(F.getName(), "")+"backup-"+serial);
			if(!backup.exists()) {
				backup.mkdirs();
			}
		}
		try {
			if(safe) {
				BR = new BufferedReader(new FileReader(F));
				if(!BR.ready()) {
					time = new Date();
					log.append(F.getName()+" NOT READY #FAILED!!!\t"+time.toString()+"\n");
					BR.close();
					return log.toString();
				}
		
				String line=null;
				StringBuffer SB = new StringBuffer();
				while((line=BR.readLine())!=null) {
					SB.append(line+"\n");
				}
				BR.close();
				for(int i=0;i<srcs.size();i++) {
					String match = (String)srcs.get(i)[0];
					String name = filename.substring(0, filename.indexOf("."));
					String suffix = filename.substring(filename.indexOf("."));
					System.out.println(name+suffix);
					if(name.contains(match)) {
						String value = (String)dests.get(i);
						if(srcs.get(i).length>1) {
							boolean flag = true;
							int occ = Integer.parseInt((String) srcs.get(i)[1]) ;
							int index = 0;
							while(occ>=0) {
								index = name.indexOf(match);
								if(index<0) {
									flag = false;
									break;
								}
								index += occ>0?match.length():0;
								name = name.substring(index);
								occ--;
							}
							if(flag) {
								index = filename.lastIndexOf(name);
								name = name.replaceFirst(match,value);
								System.out.println(filename.substring(0, index)+"+"+name+"+"+suffix);
								filename = filename.substring(0, index).concat(name).concat(suffix);
							}
						}
						else {
							filename = name.replace(match,value).concat(suffix);
						}
						System.out.println(filename);
					}
				}
				File tmp = new File(backup.getAbsolutePath()+"/"+F.getName());
				System.out.println(filepath);
				BW = new BufferedWriter(new FileWriter(tmp));
				BW.write(SB.toString());
				BW.close();
			}
			F.renameTo(new File(filepath.replace(F.getName(), filename)));
			System.out.println(F.getName());
			time = new Date();
			log.append(F.getName()+" -> "+filename+" #FINISHED\t"+time.toString()+"\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			time = new Date();
			log.append(F.getName()+" #FAILED\t"+time.toString()+"\n");
			e.printStackTrace();
		}
		return log.toString();
	}
	public static long getSerial() {
		return serial;
	}
	public static void setSerial(long serial) {
		FilenameReplacement.serial = serial;
	}
	
}