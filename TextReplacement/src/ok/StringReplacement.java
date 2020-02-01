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
public class StringReplacement {

	public static void replace(File F,String[] src,String[] dest) {
		BufferedReader BR = null;
		BufferedWriter BW = null;
		try {
			BR = new BufferedReader(new FileReader(F));
			if(!BR.ready()) {
				System.out.println("NOT READY YET!!!");
				BR.close();
				return;
			}
			
			int len = src.length>dest.length?src.length:dest.length;
			String line=null;
			StringBuffer SB = new StringBuffer();
			System.out.println(F.getName()+" STARTED");
			while((line=BR.readLine())!=null) {
				boolean flag = false;
				for(int i=0;i<len;i++) {
					if(line.contains(src[i])) {
						line = line.replaceAll(src[i], dest[i]);
						flag = true;
					}
				}
				if(flag) {
					System.out.println("REPLACE!"+line);
				}
				SB.append(line+"\n");
			}
			BR.close();
			BW = new BufferedWriter(new FileWriter(F));
			BW.write(SB.toString());
			BW.close();
			System.out.println(F.getName()+" FINISHED");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
