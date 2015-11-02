package mrthinger.gui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import mrthinger.DamageControlMain;

public class SystemTrayGUI {

	public volatile static PopupMenu popup;
	public volatile static MenuItem timeRemainingMenuItem;
	
	public volatile static TrayIcon trayIcon;
	public volatile static Image goImage;
	public volatile static Image stopImage;

	public static void createTrayIcon(final Stage stage, Scene changeIDScene) {
     if (SystemTray.isSupported()) {
         SystemTray tray = SystemTray.getSystemTray();
         try {
				goImage = ImageIO.read(DamageControlMain.class.getResourceAsStream("assets/DamageControlICON_go.png"));
				stopImage = ImageIO.read(DamageControlMain.class.getResource("assets/DamageControlICON_stop.png"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}



         stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
             @Override
             public void handle(WindowEvent t) {
                 hide(stage);
             }
         });
         final ActionListener closeListener = new ActionListener() {
             @Override
             public void actionPerformed(java.awt.event.ActionEvent e) {
                 System.exit(0);
             }
         };

         ActionListener showListener = new ActionListener() {
             @Override
             public void actionPerformed(java.awt.event.ActionEvent e) {
                 Platform.runLater(new Runnable() {
                     @Override
                     public void run() {
                    	 stage.setScene(changeIDScene);
                         stage.show();
                         stage.toFront();
                     }
                 });
             }
         };
         // create a popup menu
         popup = new PopupMenu();

         MenuItem accountItem = new MenuItem("Change Account");
         accountItem.addActionListener(showListener);
         popup.add(accountItem);

         MenuItem closeItem = new MenuItem("Exit");
         closeItem.addActionListener(closeListener);
         popup.add(closeItem);
         
        timeRemainingMenuItem = new MenuItem();
        
        trayIcon = new TrayIcon(goImage, "Damage Control - Dota 2", popup);

        trayIcon.addActionListener(showListener);

         try {
             tray.add(trayIcon);
         } catch (AWTException e) {
             System.err.println(e);
         }

         
     }
 }

 private static void hide(final Stage stage) {
     Platform.runLater(new Runnable() {
         @Override
         public void run() {
             if (SystemTray.isSupported()) {
                 stage.hide();
             } else {
                 System.exit(0);
             }
         }
     });
 }
	
}
