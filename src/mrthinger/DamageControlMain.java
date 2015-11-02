/*
 * Evan Pierce (MrThinger)
 * 10.30.15
 * Programmed this after going on an 8 game lose streak
 * when I was one game from 6k twice in a row.
 */

package mrthinger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import mrthinger.gui.GUI;
import mrthinger.main.Data;
import mrthinger.main.Refresh;

public class DamageControlMain extends Application {

	public static Thread refresh;

	@Override
	public void start(Stage primaryStage) {
		//Stops program from exiting when all windows are closed
		Platform.setImplicitExit(false);

		//initializes main thread
		refresh = new Thread(new Refresh());
		refresh.setDaemon(true);

		//Creates file system for program
		Data.init();

		//Initialize GUI
		GUI.init(primaryStage);

		//activates main thread if an account id has already been entered 
		if(Data.getAccID() == 0){
			primaryStage.show();
		}else{
			onEnter(primaryStage, Long.toString(Data.getAccID()));
		}

	}

	//activates main thread
	public static void onEnter(Stage primaryStage, String accID) {
		//assume ID is correct until proven otherwise
		Refresh.validID=true;
		//reset if this message has been sent on an account by account basis
		Refresh.invalidMSGDisplayed=false;
		//Sets account ID
		Refresh.accountID = accID;
		//starts thread
		Refresh.running = true;
		if(!refresh.isAlive()){
			refresh.start();
		}
		//records account id
		Data.setData(4, accID);

		System.out.println(accID);
		primaryStage.hide();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
