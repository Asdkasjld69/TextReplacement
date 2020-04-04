/**
 * 
 */
package ok;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

/**
 * @author cnmbx
 *
 */
public class StringReplacement {
	private static String OfficeWord = "doc|docx";
	private static String RichStyleFont = "rtf";
	public static String replace(File F,Object[] src,Object[] dest) {
		BufferedReader BR = null;
		BufferedWriter BW = null;
		StringBuffer log = new StringBuffer();
		String filename =  F.getName().split("\\.")[1];
		int rows = 0;
		int len = src.length>dest.length?src.length:dest.length;
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
				StringBuffer SB = new StringBuffer();
				boolean flag = false;
				for(String line:lines) {
					for(int i=0;i<len;i++) {
						if(line.contains((String)src[i])) {
							line = line.replaceAll((String)src[i], (String)dest[i]);
							flag = true;
						}
					}
					if(flag) {
						time = new Date();
						log.append(line+" #REPLACED!\t"+time.toString()+"\n");
						rows++;
					}
					SB.append(line+"\n");
				}
				DSD.replace(0, DSD.getLength(), SB.toString(), null);
				in.close();
				File tmp = new File(F.getAbsolutePath()+".tmp");
				OutputStream out = new FileOutputStream(tmp);
				rtf.write(out, DSD,0, DSD.getLength());
				out.close();
				F.delete();
				tmp.renameTo(F.getAbsoluteFile());
				time = new Date();
				log.append(F.getName()+" #FINISHED("+rows+")\t"+time.toString()+"\n");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.append(F.getName()+" #FAILED!!!\t"+time.toString()+"\n");
				e.printStackTrace();
			}
			return log.toString();
		}		
		//ordinary text
		try {
			BR = new BufferedReader(new FileReader(F));
			if(!BR.ready()) {
				log.append(F.getName()+" #FAILED!!!\t"+time.toString()+"\n");
				BR.close();
				return log.toString();
			}
			
			String line=null;
			StringBuffer SB = new StringBuffer();			
			while((line=BR.readLine())!=null) {
				boolean flag = false;
				for(int i=0;i<len;i++) {
					if(line.contains((String)src[i])) {
						line = line.replaceAll((String)src[i], (String)dest[i]);
						flag = true;
					}
				}
				if(flag) {
					time = new Date();
					log.append(line+" #REPLACED!\t"+time.toString()+"\n");
					rows++;
				}
				SB.append(line+"\n");
			}
			BR.close();
			File tmp = new File(F.getAbsolutePath()+".tmp");
			BW = new BufferedWriter(new FileWriter(tmp));
			BW.write(SB.toString());
			BW.close();
			F.delete();
			tmp.renameTo(F);
			time = new Date();
			log.append(F.getName()+" #FINISHED("+rows+")\t"+time.toString()+"\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return log.toString();
	}
}
