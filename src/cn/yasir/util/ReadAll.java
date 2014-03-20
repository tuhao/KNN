package cn.yasir.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ReadAll {

	public static String readAll(String file,String encoding){

		File f = new File(file);
		byte[] fileBuff = new byte[(int)f.length()];
		FileInputStream in = null;
		try {
			in = new FileInputStream(f);
			in.read(fileBuff);
			return new String(fileBuff,encoding);
		} catch (Exception e) {
			return null;
		}finally{
			try {
				if (in != null){
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
