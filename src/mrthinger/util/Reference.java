package mrthinger.util;

public class Reference
{
	
  public static final double version = 1.11;
  public static final String selfUpdateLink = "https://dl.dropbox.com/s/38q7kro0jho2jfq/selfupdate.json";
  public static final String selfUpdateFile = Util.getDCDir() + "update.txt";
  public static final String matchLink = "https://api.steampowered.com/IDOTA2Match_570/GetMatchDetails/v001/?key=44592DA7B9E952C94CF4F635E345DD8E&match_id=";
  public static final String matchHistoryLink = "https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/V001/?key=44592DA7B9E952C94CF4F635E345DD8E&account_id=";
  public static final String matchHistoryFile = Util.getDCDir() + "matchHistory.txt";
  public static final String matchFile = Util.getDCDir() + "match.txt";
  public static final String saveDataFile = Util.getDCDir() + "data.txt";

}