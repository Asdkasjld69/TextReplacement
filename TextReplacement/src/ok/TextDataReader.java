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
public class TextDataReader {

	public static String[] read(File F) {
		BufferedReader BR = null;
		String[] ret = {"*EMPTY*",""};
		StringBuffer cont = new StringBuffer();
		StringBuffer log = new StringBuffer();
		try {
			BR = new BufferedReader(new FileReader(F));
			Date time = new Date();
			if(!BR.ready()) {
				log.append("READ "+F.getName()+" #FAILED!!!\t"+time.toString()+"\n");
				ret[1] = log.toString();
				BR.close();
				return ret;
			}
			String line=null;
			time = new Date();
			log.append("READ "+F.getName()+" #STARTED\t"+time.toString()+"\n");
			StringBuffer SB = new StringBuffer();
			while((line=BR.readLine())!=null) {
				SB.append(line+"\n");
			}
			BR.close();
			ret[0] = SB.toString();
			time = new Date();
			log.append("READ "+F.getName()+" #FINISHED\t"+time.toString()+"\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ret[1] = log.toString();
		return ret;
	}
}
