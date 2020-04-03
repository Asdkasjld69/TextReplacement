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
import java.util.Date;

/**
 * @author cnmbx
 *
 */
public class StringReplacement {

	public static String replace(File F,Object[] src,Object[] dest) {
		BufferedReader BR = null;
		BufferedWriter BW = null;
		int rows = 0;
		StringBuffer log = new StringBuffer();
		Date time = new Date();
		try {
			BR = new BufferedReader(new FileReader(F));
			if(!BR.ready()) {
				log.append(F.getName()+" #FAILED!!!\t"+time.toString()+"\n");
				BR.close();
				return log.toString();
			}
			
			int len = src.length>dest.length?src.length:dest.length;
			String line=null;
			StringBuffer SB = new StringBuffer();
			time = new Date();
			log.append(F.getName()+" #STARTED\t"+time.toString()+"\n");
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
			BW = new BufferedWriter(new FileWriter(F));
			BW.write(SB.toString());
			BW.close();
			time = new Date();
			log.append(F.getName()+" #FINISHED("+rows+")\t"+time.toString()+"\n");
			System.out.println("FINISH");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return log.toString();
	}
}
