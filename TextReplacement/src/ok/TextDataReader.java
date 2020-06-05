/**
 * 
 */
package ok;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Date;

/**
 * @author cnmbx
 *
 */
public class TextDataReader {

	public static String[] read(File F) {
		BufferedReader BR = null;
		String[] ret = {"",""};
		StringBuffer log = new StringBuffer();
		try {
			BR = new BufferedReader(new InputStreamReader(new FileInputStream(F),"UTF-8"));
			Date time = new Date();
			if(!BR.ready()) {
				log.append("<log><message>"+"READ "+F.getName()+" #FAILED!!!</message><time>"+time.toString()+"</time></log>");
				ret[1] = log.toString();
				BR.close();
				return ret;
			}
			String line=null;
			time = new Date();
			log.append("<log><message>"+"READ "+F.getName()+" #STARTED</message><time>"+time.toString()+"</time></log>");
			StringBuffer SB = new StringBuffer();
			while((line=BR.readLine())!=null) {
				SB.append(line+"\n");
			}
			BR.close();
			ret[0] = SB.toString();
			time = new Date();
			log.append("<log><message>"+"READ "+F.getName()+" #FINISHED</message><time>"+time.toString()+"</time></log>");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ret[1] = log.toString();
		return ret;
	}
}
