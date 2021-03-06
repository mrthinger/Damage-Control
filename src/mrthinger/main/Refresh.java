package mrthinger.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import mrthinger.gui.SystemTrayGUI;
import mrthinger.util.Reference;
import mrthinger.util.Util;

public class Refresh implements Runnable {

	public volatile static boolean running = false;
	public volatile static String accountID = null;
	public volatile static String timeRemaining = "0";
	
	public volatile static boolean dispWarning = false;
	public volatile static boolean dispStop = false;
	
	public volatile static boolean validID = true;
	public volatile static boolean dispValidID = false;
	public volatile static boolean invalidMSGDisplayed = false;
	
	public static volatile boolean dispDotaClosed = false;

	@Override
	public void run() {
		while (Thread.currentThread().isAlive()) {

			if(running == true && accountID != null){
				tick();
			}

			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

	private void tick() {

		//Download match history and parse it. ID validity gets checked here.
		MatchHistoryParse matchList = new MatchHistoryParse(
				Util.downloadFile(Reference.matchHistoryLink + accountID, Reference.matchHistoryFile));
		Util.delete(new File(Reference.matchHistoryFile));

		//If id is valid
		if(validID && Data.getAccID() != 0){

			//If their are new matches
			if(Data.getMatchID(1) != matchList.latestMatchIDs[0]
				&& Data.getMatchID(2) != matchList.latestMatchIDs[1] 
				&& Data.getMatchID(3) != matchList.latestMatchIDs[2]){

				System.out.println("New game found!");

				//reset remaining time
				Data.setData(5, "0");

				//parse matches
				MatchParse match1 = new MatchParse(Util.downloadFile(
						Reference.matchLink + matchList.latestMatchIDs[0], Reference.matchFile));
				Util.delete(new File(Reference.matchFile));

				MatchParse match2 = new MatchParse(Util.downloadFile(
						Reference.matchLink + matchList.latestMatchIDs[1], Reference.matchFile));
				Util.delete(new File(Reference.matchFile));

				MatchParse match3 = new MatchParse(Util.downloadFile(
						Reference.matchLink + matchList.latestMatchIDs[2], Reference.matchFile));
				Util.delete(new File(Reference.matchFile));

				//check to see if all 3 matches were played within 6 hours or less
				boolean quickPlayAll;
				if ((long)(match1.startTime-match3.startTime) <= (long)(3600 * 6)){
					System.out.println("most recent 3 matches were played within 6 hours or less of eachother");
					quickPlayAll = true;
				}else{
					System.out.println("most recent 3 matches were NOT played within 6 hours or less of eachother");
					quickPlayAll = false;
				}

				//check to see if most recent 2 matches were played within 3 hours or less
				boolean quickPlayTwo;
				if ((long)(match1.startTime-match2.startTime) <= (long)(3600 * 3)){
					System.out.println("most recent 2 matches were played within 3 hours or less of eachother");
					quickPlayTwo = true;
				}else{
					System.out.println("most recent 2 matches were NOT played within 3 hours or less of eachother");
					quickPlayTwo = false;
				}

				//3 fast loss streak
				if (match1.won == false && match2.won == false && match3.won == false && quickPlayAll == true) {
					//how long AFTER the most recent match time you'll be able to play again
					int hoursOfPunishment = 8;
					long resumeTime = (long)(match1.startTime+match1.duration) + (long)(3600 * hoursOfPunishment);
					Data.setData(5, Long.toString(resumeTime));
					updateTimeRemaining();
					//checks if their is a negative amount of time remaining before displaying message
					if(!timeRemaining.startsWith("-")){
						stopPlaying();
					}
					//2 fast loss streak
				} else if (match1.won == false && match2.won == false && match3.won == true && quickPlayTwo == true) {
					takeBreak();

				}

			}else{
				System.out.println("No new games found.");
			}
			
			//Sets new match IDs
			Data.setData(1, Integer.toString(matchList.latestMatchIDs[0]));
			Data.setData(2,	Integer.toString(matchList.latestMatchIDs[1]));
			Data.setData(3, Integer.toString(matchList.latestMatchIDs[2]));
			
		//if not valid ID
		}else{
			//display message if it hasn't been show before
			if(!invalidMSGDisplayed){
			displayAccIDInvalidMSG();
			invalidMSGDisplayed=true;
			}
			Data.setData(4, "0");
			Data.setData(5, "0");
			SystemTrayGUI.trayIcon.setImage(SystemTrayGUI.pauseImage);
			   
		}

		//What to do if you're still being punished
		if((System.currentTimeMillis()/1000) < Data.getResumeTime()){
			//Kill dota
			if(Util.isMac()){
				killDOTAMac();
			}else{
				killDOTAWindows();
			}

			updateTimeRemaining();

			//update systemtray
			  boolean tRemainingAdded = false;
		      for (int i = 0; i < SystemTrayGUI.popup.getItemCount(); i++)
		      {
		        if (SystemTrayGUI.popup.getItem(i).equals(SystemTrayGUI.timeRemainingMenuItem)) {
		          tRemainingAdded = true;
		        }

		      }

		      if (!tRemainingAdded) {
		        SystemTrayGUI.popup.add(SystemTrayGUI.timeRemainingMenuItem);
		      }

		      SystemTrayGUI.timeRemainingMenuItem.setLabel("Time Remaining: " + timeRemaining);
		      SystemTrayGUI.trayIcon.setImage(SystemTrayGUI.stopImage);
		    


		}else{
			timeRemaining = "0";
			SystemTrayGUI.popup.remove(SystemTrayGUI.timeRemainingMenuItem);
			
			if(validID && Data.getAccID() != 0){
			SystemTrayGUI.trayIcon.setImage(SystemTrayGUI.goImage);
			}
			
		}

	}


	private void displayAccIDInvalidMSG() {

		dispValidID=true;
		
		try {
			Thread.sleep(1001);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		dispValidID=false;

	}

	private void stopPlaying(){
		updateTimeRemaining();
		dispStop = true;

		System.out.println("You need to STOP playing for the day");

		try {
			Thread.sleep(1001);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		dispStop=false;

	}

	private void takeBreak(){

		dispWarning = true;

		System.out.println("Consider taking a short break and getting a snack");
		try {
			Thread.sleep(1001);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		dispWarning=false;
	}
	
	  private void displayDotaClosed()
	  {
	    updateTimeRemaining();
	    dispDotaClosed = true;
	    try
	    {
	      Thread.sleep(1001L);
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    }

	    dispDotaClosed = false;
	  }


	private void killDOTAMac() {
		Process dotaPIDProcess;
		String PID;

		try {
			dotaPIDProcess = Runtime.getRuntime().exec("pgrep dota");
			dotaPIDProcess.waitFor();

			BufferedReader br = new BufferedReader(new InputStreamReader(dotaPIDProcess.getInputStream()));
			PID = br.readLine();
			if(PID != null){
				Runtime.getRuntime().exec("kill -9 " + PID);
				System.out.println("Dota killed");
				displayDotaClosed();
			}else{
				System.out.println("Dota is not being run");
			}


		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void killDOTAWindows() {
		try {
			Process dotaRunning = Runtime.getRuntime().exec("tasklist /FI \"IMAGENAME eq dota2.exe\"");
			dotaRunning.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(dotaRunning.getInputStream()));
			String output = br.readLine();
			if(!output.contains("INFO:")){
				Runtime.getRuntime().exec("taskkill /F /IM dota2.exe");
				System.out.println("Dota killed");
				displayDotaClosed();
			}else{
				System.out.println("Dota is not being run");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void updateTimeRemaining(){
		long amountTimeRemaining = (Data.getResumeTime() - (System.currentTimeMillis()/1000));

		int hours = (int) (amountTimeRemaining / 3600);
		amountTimeRemaining -= (hours * 3600);
		int mins = (int) (amountTimeRemaining / 60);
		amountTimeRemaining -= (mins * 60);
		int secs = (int) amountTimeRemaining;

		DecimalFormat dF = new DecimalFormat("00");

		timeRemaining = dF.format(hours) +":"+dF.format(mins)+":"+dF.format(secs);

	}

}
