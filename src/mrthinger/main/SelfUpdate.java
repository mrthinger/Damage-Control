package mrthinger.main;

import java.io.File;
import java.io.IOException;

import mrthinger.gui.SystemTrayGUI;
import mrthinger.util.Reference;
import mrthinger.util.Util;

public class SelfUpdate
implements Runnable
{
	public static volatile boolean running = false;
	public static volatile SelfUpdateParse updater;

	public void run()
	{
		while (Thread.currentThread().isAlive()) {
			if (running) {
				checkForUpdate();
			}

			try
			{
				Thread.sleep(300000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void checkForUpdate()
	{
		updater = new SelfUpdateParse(
				Util.downloadFile(Reference.selfUpdateLink, Reference.selfUpdateFile));
		Util.delete(new File(Reference.selfUpdateFile));

		if (updater.latestVersion > Reference.version){

			System.out.println("Program update found!");

			boolean updateFoundAdded = false;
			for (int i = 0; i < SystemTrayGUI.popup.getItemCount(); i++)
			{
				if (SystemTrayGUI.popup.getItem(i).equals(SystemTrayGUI.updateFoundMenuItem)) {
					updateFoundAdded = true;
				}

			}

			if (!updateFoundAdded)
				SystemTrayGUI.popup.add(SystemTrayGUI.updateFoundMenuItem);
		}else{
			System.out.println("Program is up to date.");
		}
	}

	public static void update(String linkToNewProgram)
	{
		Refresh.running = false;

		String newFilePath = Util.getDesktopDir() + "DamageControl_v" + updater.latestVersion + ".jar";

		Util.downloadFile(linkToNewProgram, newFilePath);
		System.out.println("downloaded new program file");

		try
		{
			Runtime.getRuntime().exec("java -jar " + newFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.exit(0);
	}
}