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

/**
 * @author cnmbx
 *
 */
public class TextDataReader {

	public static String read(File F) {
		BufferedReader BR = null;
		String ret = "*EMPTY*";
		try {
			BR = new BufferedReader(new FileReader(F));
			if(!BR.ready()) {
				System.out.println(F.getName()+" FAILED!!!");
				BR.close();
				return "";
			}
			String line=null;
			System.out.println(F.getName()+" STARTED");
			StringBuffer SB = new StringBuffer();
			while((line=BR.readLine())!=null) {
				SB.append(line+"\n");
			}
			BR.close();
			ret = SB.toString();
			System.out.println(F.getName()+" FINISHED");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
}
