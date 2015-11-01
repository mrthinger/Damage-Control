/*
 * Evan Pierce (MrThinger)
 * 10.30.15
 * Programmed this after going on an 8 game lose streak
 * when I was one game from 6k twice in a row.
 */

package mrthinger;

import javafx.application.Application;
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
import mrthinger.gui.SystemTrayGUI;
import mrthinger.main.Data;
import mrthinger.main.Refresh;

public class DamageControlMain extends Application {

	@Override
	public void start(Stage primaryStage) {
		//Stops program from exiting when all windows are closed
		Platform.setImplicitExit(false);
		
		Data.init();
		
		Thread refresh = new Thread(new Refresh());
			refresh.setDaemon(true);
			refresh.start();
			
			//Invalid ID display
			Text msgInvalidID = new Text();
			msgInvalidID.setText("Invalid Account ID.");
			StackPane paneInvalidID = new StackPane();
			
			paneInvalidID.getChildren().add(msgInvalidID);
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
			
			
		Task<Void> updateDisp = new Task<Void>(){

			@Override
			protected Void call() throws Exception {
				
				
				
				while(true){
					if(Refresh.dispWarning == true){
						
						Platform.runLater(new Runnable() {
					            @Override
					            public void run() {
					            	primaryStage.setScene(warningScene);
					            	primaryStage.show();
					            	primaryStage.toFront();
					            }
					          });          
					        }
					if(Refresh.dispStop == true){
						
						Platform.runLater(new Runnable() {
					            @Override
					            public void run() {
					            	
					            	timeRemaining.setText("Time remaining: " + Refresh.timeRemaining);
					            	primaryStage.setScene(stopScene);
					            	primaryStage.show();
					            	primaryStage.toFront();
					            }
					          });          
					        }
					if(Refresh.dispValidID == true){
						
						Platform.runLater(new Runnable() {
					            @Override
					            public void run() {
					            	primaryStage.setScene(invalidIDScene);
					            	primaryStage.show();
					            	primaryStage.toFront();
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
		
		
		primaryStage.setTitle("Damage Control - DotA 2");
	       
		  TextField accIDTextField = new TextField();
		  if(Data.getAccID() == 0){
			  accIDTextField.setText("dotabuff account ID"); 
		  }else{
			  accIDTextField.setText(Long.toString(Data.getAccID())); 
		  }
		 
		  accIDTextField.setAlignment(Pos.CENTER);
		  accIDTextField.autosize();
		  
		  Button btn = new Button();
	        btn.setText("Enter");
	        btn.setAlignment(Pos.BOTTOM_RIGHT);
	        
	        btn.setOnAction(new EventHandler<ActionEvent>() {
	 
	            @Override
	            public void handle(ActionEvent event) {
	            	
	            	onEnter(primaryStage, accIDTextField);
	            }
	        });
	        accIDTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {
					
					if(event.getCode().equals(KeyCode.ENTER)){

		        		onEnter(primaryStage, accIDTextField);
						
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
	        primaryStage.setScene(accIDScene);
	        
	        
	        if(Data.getAccID() == 0){
		        primaryStage.show();
	        }else{
	        	onEnter(primaryStage,accIDTextField);
	        }

	        SystemTrayGUI.createTrayIcon(primaryStage, accIDScene);
		
	}
	
	private void onEnter(Stage primaryStage, TextField accIDTextField) {
		//assume ID is correct until proven otherwise
		Refresh.validID=true;
		//reset if this message has been sent on an account by account basis
		Refresh.invalidMSGDisplayed=false;
		
		Refresh.accountID = accIDTextField.getText();
		Refresh.running = true;
		
		Data.setData(4, accIDTextField.getText());
		
		System.out.println(accIDTextField.getText());
		primaryStage.hide();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
