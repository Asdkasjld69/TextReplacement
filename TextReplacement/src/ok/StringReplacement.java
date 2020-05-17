package ok;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.rtf.RTFEditorKit;



public class StringReplacement {

	private static String RichStyleFont = "rtf";
	private static long serial = System.currentTimeMillis();
	public static String replace(File F,ArrayList<Object[]> change, boolean safe) {
		BufferedReader BR = null;
		BufferedWriter BW = null;
		StringBuffer log = new StringBuffer();
		String filepath = F.getAbsolutePath();
		String filename = F.getName();
		int rows = 0;
		File backup = null;
		if(safe) {
			backup = new File(F.getAbsolutePath().replace(filename, "")+"backup-"+serial);
			System.out.println(backup.getAbsolutePath());
			if(!backup.exists()) {
				backup.mkdirs();
			}
		}
		Date time = new Date();
		log.append("<log><message>"+filename+" #STARTED</message><time>"+time.toString()+"</time></log>");
		if(filename.split("\\.")[1].matches(RichStyleFont)) {
			//RTF
			DefaultStyledDocument DSD = new DefaultStyledDocument();
			try {
				InputStream in = new FileInputStream(F);
				RTFEditorKit rtf = new RTFEditorKit();
				rtf.read(in, DSD, 0);
				String[] lines = DSD.getText(0, DSD.getLength()).split("\n");
				String line = null;
				String tline = null;
				String ttline = null;
				StringBuffer SB = new StringBuffer();
				for(int n=0;n<lines.length;n++) {
					line = lines[n];
					ttline = lines[n];
					boolean flag = false;
					for(int i=0;i<change.size();i++) {
						if(tline!=null) {
							line = tline;
							ttline += "\n"+tline;
						}
						switch((int)change.get(i)[2]) {
						case 0:
							if(line.contains((String)change.get(i)[0])) {
								line = line.replace((String)change.get(i)[0], (String)change.get(i)[1]);
								tline = null;
								flag = true;
							}
							break;
						case 1:
							if(!line.replaceAll((String)change.get(i+1)[0],(String)change.get(i+1)[1]).equals(line)) {
								line = line.replaceAll((String)change.get(i)[0], (String)change.get(i)[1]);
								tline = null;
								flag = true;
							}
							break;
						case 2:
							if(line.matches((String)change.get(i)[0])) {
								line = (String)change.get(i)[1];
								tline = null;
								flag = true;
							}
							break;
						case 3:
							if(line.matches((String)change.get(i)[0])) {
								if(i+1<=change.size()-1) {
									switch ((int)change.get(i+1)[2]) {
									case 0:
										n++;
										while(n<lines.length){
											line = lines[n];
											if(line.contains((String)change.get(i+1)[0])){
												tline = line;
												break;
											}
											ttline += "\n"+line;
											n++;
										}
										break;
									case 1:
										n++;
										while(n<lines.length){
											line = lines[n];
											if(!line.replaceAll((String)change.get(i+1)[0],(String)change.get(i+1)[1]).equals(line)){
												tline = line;
												break;
											}
											ttline += "\n"+line;
											n++;
										}
										break;
									case 2:
									case 3:
										n++;
										while(n<lines.length) {
											line = lines[n];
											if(line.matches((String)change.get(i+1)[0])) {
												tline = lines[n];
												break;
											}
											n++;
										}
										break;
									}
									i++;
								}
								else {
									n++;
									while(n<lines.length) {
										line = lines[n];
										n++;
									}
								}
								n--;
								line = (String)change.get(i)[1];
								flag = true;
							}
							SB.append(line+"\n");
							break;
						}
					}
					if(flag) {
						time = new Date();
						log.append("<log><message>"+ttline+" #REPLACED!</message><time>"+time.toString()+"</time></log>");
						rows++;
					}
					SB.append(line+"\n");
				}
				DSD.replace(0, DSD.getLength(), SB.toString(), null);
				in.close();
				File tmp = new File(filepath+".new");
				OutputStream out = new FileOutputStream(tmp);
				rtf.write(out, DSD,0, DSD.getLength());
				out.close();
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

				time = new Date();
				log.append("<log><message>"+filename+" #FAILED!!!</message><time>"+time.toString()+"</time></log>");
				e.printStackTrace();
			}
			return log.toString();
		}
		else {
			//Ordinary
			try {
				BR = new BufferedReader(new FileReader(F));
				if(!BR.ready()) {
					time = new Date();
					log.append("<log><message>"+filename+" NOT READY #FAILED!!!</message><time>"+time.toString()+"</time></log>");
					BR.close();
					return log.toString();
				}
				
				String line=null;
				String tline = null;
				String ttline = null;
				StringBuffer SB = new StringBuffer();			
				while((line=BR.readLine())!=null) {
					ttline = line;
					boolean flag = false;
					for(int i=0;i<change.size();i++) {
						if(tline != null) {
							 line = tline;
							 ttline += "\n" + tline;
						}
						switch((int)change.get(i)[2]) {
						case 0:
							if(line.contains((String)change.get(i)[0])) {
								line = line.replace((String)change.get(i)[0], (String)change.get(i)[1]);
								tline = null;
								flag = true;
							}
							break;
						case 1:
							if(!line.replaceAll((String)change.get(i)[0],(String)change.get(i)[1]).equals(line)) {
								line = line.replaceAll((String)change.get(i)[0], (String)change.get(i)[1]);
								tline = null;
								flag = true;
							}
							break;
						case 2:
							if(line.matches((String)change.get(i)[0])) {
								line = (String)change.get(i)[1];
								tline = null;
								flag = true;
							}
							break;
						case 3:
							if(line.matches((String)change.get(i)[0])) {
								if(i+1<=change.size()-1) {
									switch ((int)change.get(i+1)[2]) {
									case 0:
										line = BR.readLine();
										while(line!=null){
											if(line.contains((String)change.get(i+1)[0])){
												tline = line;
												break;
											}
											ttline += "\n"+line;
											line = BR.readLine();
										}
										break;
									case 1:
										line = BR.readLine();
										while(line!=null){
											if(!line.replaceAll((String)change.get(i+1)[0],(String)change.get(i+1)[1]).equals(line)){
												tline = line;
												break;
											}
											ttline += "\n"+line;
											line = BR.readLine();
										}
										break;
									case 2:
									case 3:
										line = BR.readLine();
										while(line!=null) {
											if(line.matches((String)change.get(i+1)[0])) {
												tline = line;
												break;
											}
											ttline += "\n"+line;
											line = BR.readLine();
										}
										break;
									}
								}
								else {
									line = BR.readLine();
									while(line!=null) {
										ttline += "\n"+line;
										line = BR.readLine();
									}
								}
								line = (String)change.get(i)[1];
								SB.append(line+"\n");
								flag = true;
							}
							break;
						}
					}
					if(flag) {
						time = new Date();
						log.append("<log><message>"+ttline+" #REPLACED!</message><time>"+time.toString()+"</time></log>");
						rows++;
					}
					SB.append(line+"\n");
				}
				BR.close();
				File tmp = new File(filepath+".new");
				BW = new BufferedWriter(new FileWriter(tmp));
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

				time = new Date();
				log.append("<log><message>"+filename+" #FAILED!!!</message><time>"+time.toString()+"</time></log>");
				e.printStackTrace();
			}
			return log.toString();
		}
	}
	public static long getSerial() {
		return serial;
	}
	public static void setSerial(long serial) {
		StringReplacement.serial = serial;
	}
	
	
}
