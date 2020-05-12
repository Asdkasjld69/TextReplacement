/**
 * 
 */
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
import java.util.Date;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.rtf.RTFEditorKit;

/**
 * @author cnmbx
 *
 */
public class StringReplacement {
	private static String OfficeWord = "doc|docx";
	private static String RichStyleFont = "rtf";
	private static long serial = System.currentTimeMillis();
	public static String replace(File F,Object[] src,Object[] dest, boolean safe) {
		BufferedReader BR = null;
		BufferedWriter BW = null;
		StringBuffer log = new StringBuffer();
		String filename = F.getAbsolutePath();
		int rows = 0;
		int len = src.length>dest.length?src.length:dest.length;
		File backup = null;
		if(safe) {
			backup = new File(F.getAbsolutePath().replace(F.getName(), "")+"backup-"+serial);
			System.out.println(backup.getAbsolutePath());
			if(!backup.exists()) {
				backup.mkdirs();
			}
		}
		Date time = new Date();
		log.append(F.getName()+" #STARTED\t"+time.toString()+"\n");
		//rtf
		if(F.getName().split("\\.")[1].matches(RichStyleFont)) {
			DefaultStyledDocument DSD = new DefaultStyledDocument();
			try {
				InputStream in = new FileInputStream(F);
				RTFEditorKit rtf = new RTFEditorKit();
				rtf.read(in, DSD, 0);
				String[] lines = DSD.getText(0, DSD.getLength()).split("\n");
				String tline = "";
				StringBuffer SB = new StringBuffer();
				for(String line:lines) {
					boolean flag = false;
					tline = line;
					for(int i=0;i<len;i++) {
						if(line.contains((String)src[i])) {
							line = line.replaceAll((String)src[i], (String)dest[i]);
							flag = true;
						}
					}
					if(flag) {
						time = new Date();
						log.append(tline+" #REPLACED!\t"+time.toString()+"\n");
						rows++;
					}
					SB.append(line+"\n");
				}
				DSD.replace(0, DSD.getLength(), SB.toString(), null);
				in.close();
				File tmp = new File(filename+".new");
				OutputStream out = new FileOutputStream(tmp);
				rtf.write(out, DSD,0, DSD.getLength());
				out.close();
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
				log.append(F.getName()+" #FAILED!!!\t"+time.toString()+"\n");
				e.printStackTrace();
			}
			return log.toString();
		}		
		//ordinary text
		try {
			BR = new BufferedReader(new FileReader(F));
			if(!BR.ready()) {
				time = new Date();
				log.append(F.getName()+" NOT READY #FAILED!!!\t"+time.toString()+"\n");
				BR.close();
				return log.toString();
			}
			
			String line=null;
			String tline = "";
			StringBuffer SB = new StringBuffer();			
			while((line=BR.readLine())!=null) {
				boolean flag = false;
				tline = line;
				for(int i=0;i<len;i++) {
					if(line.contains((String)src[i])) {
						line = line.replaceAll((String)src[i], (String)dest[i]);
						flag = true;
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
			File tmp = new File(filename+".new");
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
			log.append(F.getName()+" #FAILED!!!\t"+time.toString()+"\n");
			e.printStackTrace();
		}
		return log.toString();
	}
	public static long getSerial() {
		return serial;
	}
	public static void setSerial(long serial) {
		StringReplacement.serial = serial;
	}
	
	
}
