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

import org.apache.poi.hssf.record.BackupRecord;

/**
 * @author cnmbx
 *
 */
public class XmlBlockReplacement {
	private static long serial = System.currentTimeMillis();
	public static String replace(File F,ArrayList<Object[]> tag,ArrayList<Object[]> src,Object[] dest,boolean safe) {
		BufferedReader BR = null;
		BufferedWriter BW = null;
		File backup = null;
		StringBuffer log = new StringBuffer();
		int rows=0;
		Date time = new Date();
		log.append(F.getName()+" #STARTED\t"+time.toString()+"\n");
		if(safe) {
			backup = new File(F.getAbsolutePath().replace(F.getName(), "")+"backup-"+serial);
			if(!backup.exists()) {
				backup.mkdirs();
			}
		}
		try {
			BR = new BufferedReader(new FileReader(F));
			if(!BR.ready()) {
				time = new Date();
				log.append(F.getName()+" #FAILED!!!\t"+time.toString()+"\n");
				BR.close();
				return log.toString();
			}
	
			String line=null;
			String tline = "";
			StringBuffer SB = new StringBuffer();
			while((line=BR.readLine())!=null) {
				boolean flag = false;
				tline = line;
				for(int i=0;i<tag.size();i++) {
					if(line.contains("<"+tag.get(i)[0])){
						flag = true;
						while(!line.contains(">")) {
							line.replace("\n", "");
							line += BR.readLine();
						}
						HashMap<String,Boolean> ofm = new HashMap<String,Boolean>();
						for(int n=0;n<src.get(i).length;n++) {
							switch((String)tag.get(i)[n+1]) {
							case "not":	break;
							case "":	break;
							default:	ofm.put((String)tag.get(i)[n+1],false);
							}
						}
						for(int n=0;n<src.get(i).length;n++) {
							switch((String)tag.get(i)[n+1]) {
							case "not":	if(line.contains((String)src.get(i)[n])) {
											flag = false;
										}break;
							case "":	if(!line.contains((String)src.get(i)[n])) {
											flag = false;
										}break;
							default:	if(line.contains((String)src.get(i)[n])) {
											ofm.put((String)tag.get(i)[n+1],true);
										}
							}
							if(!flag) {
								break;
							}
						}
						if(flag) {
							flag = !ofm.containsValue(false);
						}
					}
					if(flag) {
						while((line=BR.readLine())!=null){
							if(line.contains("/"+(String)tag.get(i)[0]+">")) {
								break;
							}
						}
						line = (String)dest[i];
						break;
					}
				}
				if(flag) {
					time = new Date();
					log.append(tline+" #REPLACED!\t"+time.toString()+"\n");
					rows++;
				}
				SB.append(line+"\n");
			}
			BR.close();
			File tmp = new File(F.getAbsolutePath().replace(".txt", "-NEW.txt"));
			BW = new BufferedWriter(new FileWriter(tmp));
			BW.write(SB.toString());
			BW.close();
			if(!safe) {
				F.delete();
			}
			else {
				F.renameTo(new File(backup.getAbsolutePath()+"/"+F.getName()));
			}
			tmp.renameTo(F.getAbsoluteFile());
			time = new Date();
			log.append(F.getName()+" #FINISHED("+rows+")\t"+time.toString()+"\n");
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
		XmlBlockReplacement.serial = serial;
	}
	
}
