package demo;

import java.io.File;
import java.util.ArrayList;

import ok.StringReplacement;
import ok.TextDataReader;
import ok.XmlBlockReplacement;

public class Demo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File path = new File("path");
		String[] suffix = {"xaf","xml","txt"};
		File[] files=path.listFiles();
		/*String[] src = {"\"Bone_Spine","\"Bone_Leg_Left_Root","\"Bone_Leg_Right_Root"};
		String[] dest = {"\"Protag_Bone_Spine","\"Protag_Bone_Leg_Left_Root","\"Protag_Bone_Leg_Right_Root"};
		for(File file:files) {
			for(String suf:suffix) {
				if(file.getName().matches(".+."+suf)) {
					StringReplacement.replace(file, src , dest);
					break;
				}
			}
		}*/
		String[][] tag = {{"Controller","1","1","1","1","not"},{"Controller",""}};
		String[][] src = {{"filterType=\"pos\"","filterType=\"posx\"","filterType=\"posy\"","filterType=\"posz\"","name=\"Protag_Bone_Spine"},{"filterType=\"scale\""}};
		String[] dest = {"",""};
		for(File file:files) {
			for(String suf:suffix) {
				if(file.getName().matches(".+."+suf)) {
					XmlBlockReplacement.replace(file, tag , src, dest);
					break;
				}
			}
		}
	}

}
