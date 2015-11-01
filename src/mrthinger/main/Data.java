package mrthinger.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import mrthinger.util.Reference;
import mrthinger.util.Util;

public class Data {

	public static File saveFile = null;
	
	@SuppressWarnings("unchecked")
	public static void init(){
		
		saveFile = new File(Reference.saveDataFile);
		File dcFolder = new File(Util.getDCDir());
		File matchFile = new File(Reference.matchFile);
		File matchHistoryFile = new File(Reference.matchHistoryFile);
		
		if(matchFile.exists()){
			Util.delete(matchFile);
		}
		if(matchHistoryFile.exists()){
			Util.delete(matchHistoryFile);
		}
		
		if(!dcFolder.exists()){
			dcFolder.mkdir();
		}
		
		if(!saveFile.exists()){
			try {
				saveFile.createNewFile();
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("accID", 0);
				jsonObject.put("resumeTime", 0);

				jsonObject.put("match1ID", 0);
				jsonObject.put("match2ID", 0);
				jsonObject.put("match3ID", 0);
				
				FileWriter fw = new FileWriter(saveFile);
				fw.write(jsonObject.toJSONString());
				fw.flush();
				fw.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		
		
		
	}
	
	
	/*
	 * dataIDs:
	 * 1 = match1ID
	 * 2 = match2ID
	 * 3 = match3ID
	 * 4 = accID
	 * 5 = resumeTime
	 */
	@SuppressWarnings("unchecked")
	public static void setData(int dataID, String dataValue){
		

		try {
			
			int currentMatch1ID = getMatchID(1);
			int currentMatch2ID = getMatchID(2);
			int currentMatch3ID = getMatchID(3);
			long unixTime = getResumeTime();
			long accID = getAccID();
			
			
			JSONObject jsonObject = new JSONObject();
			
			switch(dataID){
			case 1:
				
				jsonObject.put("match1ID", dataValue);
				jsonObject.put("match2ID", currentMatch2ID);
				jsonObject.put("match3ID", currentMatch3ID);
				jsonObject.put("accID", accID);
				jsonObject.put("resumeTime", unixTime);
				
				break;
			case 2:
				
				jsonObject.put("match1ID", currentMatch1ID);
				jsonObject.put("match2ID", dataValue);
				jsonObject.put("match3ID", currentMatch3ID);
				jsonObject.put("accID", accID);
				jsonObject.put("resumeTime", unixTime);
				
				break;	
			case 3:
				
				jsonObject.put("match1ID", currentMatch1ID);
				jsonObject.put("match2ID", currentMatch2ID);
				jsonObject.put("match3ID", dataValue);
				jsonObject.put("accID", accID);
				jsonObject.put("resumeTime", unixTime);
				
				break;
			case 4:
				
				jsonObject.put("match1ID", currentMatch1ID);
				jsonObject.put("match2ID", currentMatch2ID);
				jsonObject.put("match3ID", currentMatch3ID);
				jsonObject.put("accID", dataValue);
				jsonObject.put("resumeTime", unixTime);
				
				break;
			case 5:
				
				jsonObject.put("match1ID", currentMatch1ID);
				jsonObject.put("match2ID", currentMatch2ID);
				jsonObject.put("match3ID", currentMatch3ID);
				jsonObject.put("accID", accID);
				jsonObject.put("resumeTime", dataValue);
				
				break;
			default:System.out.println("Invalid data ID: " + dataID);
			}

			
			FileWriter fw = new FileWriter(saveFile);
			fw.write(jsonObject.toJSONString());
			fw.flush();
			fw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	public static int getMatchID(int matchNumber){

		int id = 0;
		
			try {
				JSONParser parser = new JSONParser();

				FileReader file = new FileReader(saveFile);
		
			Object obj = parser.parse(file);

			JSONObject saveFileJSON = (JSONObject) obj;
			file.close();
			
			 id = Integer.parseInt(saveFileJSON.get("match" + matchNumber + "ID").toString());
			
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			return id;
	}
	
	public static long getResumeTime(){
		
		long time = 0;
		
		try {
			JSONParser parser = new JSONParser();

			FileReader file = new FileReader(saveFile);
	
		Object obj = parser.parse(file);

		JSONObject saveFileJSON = (JSONObject) obj;
		file.close();
		
		 time = Long.parseLong(saveFileJSON.get("resumeTime").toString());
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return time;
	}
	
	public static long getAccID(){
		long id = 0;
		
		try {
			JSONParser parser = new JSONParser();

			FileReader file = new FileReader(saveFile);
	
		Object obj = parser.parse(file);

		JSONObject saveFileJSON = (JSONObject) obj;
		file.close();
		
		id = Long.parseLong(saveFileJSON.get("accID").toString());
		}catch(NumberFormatException e){
			Refresh.validID=false;
			System.out.println("ID had an invalid character");
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}catch (ParseException e) {
			e.printStackTrace();
		}
		
		return id;
		
	}
	
}
