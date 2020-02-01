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
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author cnmbx
 *
 */
public class XmlBlockReplacement {

	public static void replace(File F,String[][] tag,String[][] src,String[] dest) {
		BufferedReader BR = null;
		BufferedWriter BW = null;
		try {
			BR = new BufferedReader(new FileReader(F));
			if(!BR.ready()) {
				System.out.println("NOT READY YET!!!");
				BR.close();
				return;
			}
	
			String line=null;
			StringBuffer SB = new StringBuffer();
			System.out.println(F.getName()+" STARTED");
			while((line=BR.readLine())!=null) {
				boolean flag = false;
				for(int i=0;i<tag.length;i++) {
					if(line.contains("<"+tag[i][0])){
						flag = true;
						HashMap<String,Boolean> ofm = new HashMap<String,Boolean>();
						for(int n=0;n<src[i].length;n++) {
							switch(tag[i][n+1]) {
							case "not":	break;
							case "":	break;
							default:	ofm.put(tag[i][n+1],false);
							}
						}
						for(int n=0;n<src[i].length;n++) {
							switch(tag[i][n+1]) {
							case "not":	if(line.contains(src[i][n])) {
											flag = false;
										}break;
							case "":	if(!line.contains(src[i][n])) {
											flag = false;
										}break;
							default:	if(line.contains(src[i][n])) {
											ofm.put(tag[i][n+1],true);
										}
							}
							if(!flag) {
								break;
							}
						}
						if(flag) {
							flag = !ofm.containsValue(false);
						}
					}
					if(flag) {
						while((line=BR.readLine())!=null){
							if(line.contains("/"+tag[i][0]+">")) {
								break;
							}
						}
						line = dest[i];
						break;
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
