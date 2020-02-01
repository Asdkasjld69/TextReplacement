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

	public static void read(File F) {
		BufferedReader BR = null;
		try {
			BR = new BufferedReader(new FileReader(F));
			if(!BR.ready()) {
				System.out.println(F.getName()+" FAILED!!!");
				BR.close();
				return;
			}
			String line=null;
			System.out.println(F.getName()+" STARTED");
			while((line=BR.readLine())!=null) {
				System.out.println(line);
			}
			BR.close();
			System.out.println(F.getName()+" FINISHED");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
