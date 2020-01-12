package demo;

import java.io.File;

import ok.Replacement;

public class Demo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File path = new File("xaf");
		File[] files=path.listFiles();
		String[] src = {"\"Bone_Spine","\"Bone_Leg_Left_Root","\"Bone_Leg_Right_Root"};
		String[] dest = {"\"Protag_Bone_Spine","\"Protag_Bone_Leg_Left_Root","\"Protag_Bone_Leg_Right_Root"};
		for(File file:files) {
			if(file.getName().matches(".+.xaf")) {
				Replacement.replace(file, src , dest);
			}
		}
	}

}
