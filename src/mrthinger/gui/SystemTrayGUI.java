package mrthinger.gui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import mrthinger.DamageControlMain;
import mrthinger.main.SelfUpdate;
import mrthinger.util.Reference;

public class SystemTrayGUI {

	  public static volatile PopupMenu popup;
	  public static volatile MenuItem timeRemainingMenuItem;
	  public static volatile MenuItem updateFoundMenuItem;
	  public static volatile TrayIcon trayIcon;
	  public static volatile Image noneImage;
	  public static volatile Image goImage;
	  public static volatile Image pauseImage;
	  public static volatile Image stopImage;
	  
	public static void createTrayIcon(final Stage stage, Scene changeIDScene) {
     if (SystemTray.isSupported()) {
         SystemTray tray = SystemTray.getSystemTray();
         try {
        	   noneImage = ImageIO.read(DamageControlMain.class.getResourceAsStream("assets/DamageControlICON_none.png"));
               goImage = ImageIO.read(DamageControlMain.class.getResourceAsStream("assets/DamageControlICON_go.png"));
               pauseImage = ImageIO.read(DamageControlMain.class.getResourceAsStream("assets/DamageControlICON_pause.png"));
               stopImage = ImageIO.read(DamageControlMain.class.getResourceAsStream("assets/DamageControlICON_stop.png"));
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
             public void actionPerformed(ActionEvent e) {
                 System.exit(0);
             }
         };

         ActionListener showListener = new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
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
         
         ActionListener updateListener = new ActionListener()
         {
           public void actionPerformed(ActionEvent e) {
             Platform.runLater(new Runnable()
             {
               public void run() {
                 SelfUpdate.update(SelfUpdate.updater.latestProgramLink);
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
        
        updateFoundMenuItem = new MenuItem("Update Damage Control");
        updateFoundMenuItem.addActionListener(updateListener);

        
        trayIcon = new TrayIcon(noneImage, "Damage Control v." + Reference.version + " - Dota 2", popup);

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
