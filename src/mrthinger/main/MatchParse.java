package mrthinger.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MatchParse {

	public static File mH;
	public static JSONObject mHO;
	public JSONObject results;

	public int playerSlot;
	public int team;
	public long startTime;
	public long duration;

	public boolean radiantWin;
	public boolean won;

	public MatchParse(String matchHistory) {

		mH = new File(matchHistory);
		readMatchJSON();

		results = (JSONObject) mHO.get("result");

		radiantWin = results.get("radiant_win").toString().equals("true");
		
		startTime = Long.parseLong(results.get("start_time").toString());
		duration = Long.parseLong(results.get("duration").toString());
		
		JSONArray players = (JSONArray) results.get("players");
		
		for(int i = 0; i < players.size(); i++){
			
			JSONObject player = (JSONObject)players.get(i);
			
			if(player.get("account_id").toString().equals(Refresh.accountID)){
				
				playerSlot = Integer.parseInt(player.get("player_slot").toString());
				
			}
			
			
		}
		
		//team 0 is radiant & team 1 is dire
		if (playerSlot >= 0 & playerSlot <= 4) {
			team = 0;
		} else {
			team = 1;
		}
		
		if((team == 0 && radiantWin == true) || (team == 1 && radiantWin == false)){
			won = true;
		}else{
			won = false;
		}
	
		System.out.println(won);

	}

	private static void readMatchJSON() {

		JSONParser parser = new JSONParser();

		try {
			FileReader file = new FileReader(mH);
			Object obj = parser.parse(file);

			mHO = (JSONObject) obj;

			file.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
