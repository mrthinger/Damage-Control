package mrthinger.gui;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mrthinger.DamageControlMain;
import mrthinger.main.Data;
import mrthinger.main.Refresh;

public class GUI {

	public static void init(Stage stage){

		//Enter dotabuff ID display
		//If account id is 0 its the first time the program has been opened or id is invalid.
		TextField accIDTextField = new TextField();
		if(Data.getAccID() == 0){
			accIDTextField.setText("Enter Account ID"); 
		}else{
			accIDTextField.setText(Long.toString(Data.getAccID())); 
		}

		accIDTextField.setAlignment(Pos.CENTER);
		accIDTextField.autosize();

		Button btn = new Button();
		btn.setText("Enter");

		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				DamageControlMain.onEnter(stage, accIDTextField.getText());
			}
		});
		accIDTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if(event.getCode().equals(KeyCode.ENTER)){

					DamageControlMain.onEnter(stage, accIDTextField.getText());

				}	
			}
		});

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));

		grid.add(accIDTextField, 0, 0);
		grid.add(btn, 1, 0);

		Scene accIDScene = new Scene(grid, 500, 100);
		stage.setScene(accIDScene);


		//Invalid ID display
		Text msgInvalidID = new Text();
		msgInvalidID.setText("Invalid Account ID.");

		Button changeSceneBtn = new Button();
		changeSceneBtn.setText("Change Account ID");

		changeSceneBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				Platform.runLater(new Runnable(){

					@Override
					public void run() {
						accIDTextField.setText("Enter Account ID"); 
						stage.setScene(accIDScene);
						stage.show();
						stage.toFront();
					}});
			}
		});

		GridPane paneInvalidID = new GridPane();

		paneInvalidID.setAlignment(Pos.CENTER);
		paneInvalidID.setHgap(10);
		paneInvalidID.setVgap(10);
		paneInvalidID.setPadding(new Insets(10, 10, 10, 10));

		paneInvalidID.add(msgInvalidID,0,0);
		paneInvalidID.add(changeSceneBtn,1,0);
		Scene invalidIDScene = new Scene(paneInvalidID, 500, 100);

		//Warning display
		Text msgWarning = new Text();
		msgWarning.setText("Consider taking a short break and getting a snack.");
		StackPane paneWarning = new StackPane();
		
		paneWarning.getChildren().add(msgWarning);
		Scene warningScene = new Scene(paneWarning, 500, 100);

		//Stop display
		Text msgStop = new Text();
		msgStop.setText("You need to STOP playing for a while.");

		Text timeRemaining = new Text();
		GridPane paneStop = new GridPane();

		paneStop.setAlignment(Pos.CENTER);
		paneStop.setHgap(10);
		paneStop.setVgap(10);
		paneStop.setPadding(new Insets(10, 10, 10, 10));

		paneStop.add(msgStop,0,0);
		paneStop.add(timeRemaining,0,1);
		Scene stopScene = new Scene(paneStop, 500, 100);

		//notifications from refresh thread
		Task<Void> updateDisp = new Task<Void>(){

			@Override
			protected Void call() throws Exception {
				while(true){
					if(Refresh.dispWarning == true){

						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								stage.setScene(warningScene);
								stage.show();
								stage.toFront();
							}
						});          
					}
					if(Refresh.dispStop == true){

						Platform.runLater(new Runnable() {
							@Override
							public void run() {

								timeRemaining.setText("Time remaining: " + Refresh.timeRemaining);
								stage.setScene(stopScene);
								stage.show();
								stage.toFront();
							}
						});          
					}
					if(Refresh.dispValidID == true){

						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								stage.setScene(invalidIDScene);
								stage.show();
								stage.toFront();
							}
						});          
					}
					Thread.sleep(1000);	
				}
			}
		};

		Thread update = new Thread(updateDisp);
		update.setDaemon(true);
		update.start();

		stage.setTitle("Damage Control - DotA 2");

		//creates system tray
		SystemTrayGUI.createTrayIcon(stage, accIDScene);

	}

}
