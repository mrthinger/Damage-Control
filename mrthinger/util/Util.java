package mrthinger.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Util {

	public static boolean isMac() {
		if (getOS().equals("Mac OS X")) {
			return true;
		} else {
			return false;
		}
	}

	public static String getOS() {
		return System.getProperty("os.name");
	}
	
	public static String getUserDir() {
		return System.getProperty("user.home");
	}

	public static String getDesktopDir() {
		return getUserDir() + "/Desktop/";
	}

	public static String getDCDir() {
		if (isMac()) {
			return getUserDir() + "/Library/Application Support/DamageControl/";

		} else {
			return getUserDir() + "/AppData/Roaming/DamageControl/";
		}

	}

	public static String downloadFile(String link, String saveTo) {

		try {
			URL url = new URL(link);
			URLConnection conn = url.openConnection();
			InputStream in = conn.getInputStream();
			FileOutputStream out = new FileOutputStream(saveTo);

			byte[] b = new byte[1024];
			int count;

			while ((count = in.read(b)) >= 0) {
				out.write(b, 0, count);

			}

			out.flush();
			out.close();
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to Download");

		}

		return saveTo;

	}

	public static void delete(File file) {

		if (file.isDirectory()) {

			if (file.list().length == 0) {

				file.delete();

			} else {

				String files[] = file.list();

				for (String temp : files) {

					File fileDelete = new File(file, temp);

					delete(fileDelete);
				}

				if (file.list().length == 0) {
					file.delete();
				}
			}

		} else {

			file.delete();
		}
	}

}
