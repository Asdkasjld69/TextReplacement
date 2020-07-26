/**
 * 
 */
package ok;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * @author cnmbx
 *
 */
public class XmlBlockReplacement {
	private static long serial = System.currentTimeMillis();
	private static String path = "";
	public static String replace(File F,ArrayList<Object[]> tag,ArrayList<Object[]> src,Object[] dest,boolean safe) {
		BufferedReader BR = null;
		BufferedWriter BW = null;
		File backup = null;
		StringBuffer log = new StringBuffer();
		String filepath = F.getAbsolutePath();
		String filename = F.getName();
		int rows=0;
		Date time = new Date();
		log.append("<log><message>"+filename+" #STARTED</message><time>"+time.toString()+"</time></log>");
		if(safe) {
			backup = new File(path+"/backup-"+serial+filepath.replace(path, "").replace(filename, ""));
			if(!backup.exists()) {
				backup.mkdirs();
			}
		}
		try {
			BR = new BufferedReader(new InputStreamReader(new FileInputStream(F),"UTF-8"));
			if(!BR.ready()) {
				time = new Date();
				log.append("<log><message>"+filename+" NOT READY #FAILED!!!</message><time>"+time.toString()+"</time></log>");
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
						while(!line.contains(">")) {
							line.replace("\n", "");
							line += BR.readLine();
						}
						HashMap<String,Boolean> ofm = new HashMap<String,Boolean>();
						for(int n=0;n<src.get(i).length;n++) {
							if(!((String) tag.get(i)[n+1]).contains("not")) {
								ofm.put((String)tag.get(i)[n+1],line.contains((String)src.get(i)[n]));
							}
							else {
								ofm.put((String)tag.get(i)[n+1],!line.contains((String)src.get(i)[n]));
							}
						}
						Set<String> ks = ofm.keySet();
						Iterator<String> it = ks.iterator();
						String t = "";
						while(it.hasNext()) {
							t = it.next();
							if(t.startsWith("not")) {
								String notr = t.replaceFirst("not", "");
								if(ofm.containsKey(notr)) {
									if(ofm.get(t)&&ofm.get(notr)) {
										flag = true;break;
									}
								}
								else {
									if(ofm.get(t)) {
										flag = true;break;
									}
								}
							}
							else {
								String not = "not".concat(t);
								if(ofm.containsKey(not)) {
									if(ofm.get(t)&&ofm.get(not)) {
										flag = true;break;
									}
								}
								else {
									if(ofm.get(t)) {
										flag = true;break;
									}
								}
							}
						}
						System.out.println(ofm);
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
					log.append("<log><message>"+tline+" #REPLACED!</message><time>"+time.toString()+"</time></log>");
					rows++;
				}
				SB.append(line+"\n");
			}
			BR.close();
			File tmp = new File(filepath+".new");
			BW = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmp),"UTF-8"));
			BW.write(SB.toString());
			BW.close();
			if(!safe) {
				F.delete();
			}
			else {
				F.renameTo(new File(backup.getAbsolutePath()+"/"+filename));
			}
			tmp.renameTo(F.getAbsoluteFile());
			time = new Date();
			log.append("<log><message>"+filename+" #FINISHED("+rows+")</message><time>"+time.toString()+"</time></log>");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			time = new Date();
			log.append("<log><message>"+filename+" #FAILED</message><time>"+time.toString()+"</time></log>");
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
	public static String getPath() {
		return path;
	}
	public static void setPath(String path) {
		XmlBlockReplacement.path = path;
	}
	
}
