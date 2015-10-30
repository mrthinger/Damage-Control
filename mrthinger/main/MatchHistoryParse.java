package mrthinger.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MatchHistoryParse {

	public static File mH;
	public static JSONObject mHO;

	public int[] latestMatchIDs;

	public MatchHistoryParse(String matchHistory) {

		mH = new File(matchHistory);
		readMatchJSON();

		JSONObject results = (JSONObject) mHO.get("result");
		JSONArray matches = (JSONArray) results.get("matches");

		JSONObject match1 = (JSONObject) matches.get(0);
		JSONObject match2 = (JSONObject) matches.get(1);
		JSONObject match3 = (JSONObject) matches.get(2);

		int match1ID = Integer.parseInt(match1.get("match_id").toString());	
		int match2ID = Integer.parseInt(match2.get("match_id").toString());
		int match3ID = Integer.parseInt(match3.get("match_id").toString());

		latestMatchIDs = new int[] { match1ID, match2ID, match3ID };
		

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
