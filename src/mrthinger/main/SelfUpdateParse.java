package mrthinger.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SelfUpdateParse
{
  public static File sU;
  public static JSONObject sUO;
  public double latestVersion;
  public String latestProgramLink;

  public SelfUpdateParse(String selfUpdateFilePath)
  {
    sU = new File(selfUpdateFilePath);
    readMatchJSON();

    this.latestVersion = Double.parseDouble(sUO.get("version").toString());
    this.latestProgramLink = sUO.get("link").toString();
  }

  private static void readMatchJSON()
  {
    JSONParser parser = new JSONParser();
    try
    {
      FileReader file = new FileReader(sU);
      Object obj = parser.parse(file);

      sUO = (JSONObject)obj;

      file.close();
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }
}