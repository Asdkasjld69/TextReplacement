package demo;

import java.io.File;
import java.util.ArrayList;

import ok.StringReplacement;
import ok.XmlBlockReplacement;

public class Demo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File path = new File("xaf");
		File[] files=path.listFiles();
		/*String[] src = {"\"Bone_Spine","\"Bone_Leg_Left_Root","\"Bone_Leg_Right_Root"};
		String[] dest = {"\"Protag_Bone_Spine","\"Protag_Bone_Leg_Left_Root","\"Protag_Bone_Leg_Right_Root"};
		for(File file:files) {
			if(file.getName().matches(".+.xaf")) {
				StringReplacement.replace(file, src , dest);
			}
		}*/
		String[][] tag = {{"Controller","1","1","1","1","not"},{"Controller",""}};
		String[][] src = {{"filterType=\"pos\"","filterType=\"posx\"","filterType=\"posy\"","filterType=\"posz\"","name=\"Protag_Bone_Spine"},{"filterType=\"scale\""}};
		String[] dest = {"",""};
		for(File file:files) {
			if(file.getName().matches(".+.xaf")) {
				XmlBlockReplacement.replace(file, tag , src, dest);
			}
		}
	}

}
