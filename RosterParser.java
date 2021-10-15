import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.TreeMap;
import java.util.HashSet;
import java.util.Map;
//import java.util.concurrent.TimeUnit;

import java.io.*;

public class RosterParser {
	
	private String teamsURL;
	private String playersURL;

	private int mapCounter = 0;
	
	private BufferedWriter output;
	
	BufferedWriter urlMap;
	BufferedWriter writeNums;
	BufferedWriter writeNames;
	
	BufferedReader urlRead;
	BufferedReader numRead;
	BufferedReader nameRead;
	
	//Boolean if the program should be writing the TXT file
	private boolean isWriting = false;
	
	private boolean writeMaps = false;
	
	private boolean getMapsOnline = false;
	//Map of Players to URLs
	private TreeMap<String, String> playerMap = new TreeMap<String, String>();
	
	//Map of players to a number
	private TreeMap<Integer, String> numMap = new TreeMap<Integer, String>();
	private TreeMap<String, Integer> revNums = new TreeMap<String, Integer>();
	
	public RosterParser () {
		this.teamsURL = "https://www.basketball-reference.com/teams";
		this.playersURL = "https://www.basketball-reference.com/players/";

		readInMaps();
        
        if (isWriting) {
        	this.writeAdjList();
        }
        
        if (writeMaps) {
        	this.writePlayerMaps();
        }
        
        if (getMapsOnline) {
        	initiatePlayerMap();
        }
	}
	
	
	/*
	 * Function that writes the adjacency list to a file
	 * DO NOT CALL THIS, it takes an hour and will overwrite 
	 * the file
	 * 
	 * Input: None
	 * Output: None
	 * 
	 */
	private void writeAdjList () {
		try {
			this.output = new BufferedWriter(new FileWriter("/Users/nickeissler/Documents/CIS 121/Degrees of Shaq/Files/AdjList.txt", false));
		} catch (IOException e) {
			System.out.println("IOException while creating writer");
		}
		
		for (Map.Entry<Integer, String> entry : numMap.entrySet() ) {
			try {
				String mainID = "" + entry.getKey();
				output.write(mainID);
				output.write((char) 32);
				//System.out.println();
				System.out.println(mainID + ": ");
				
				
				for (String mate : this.getTeamMates(entry.getKey()) ) {
					int id;
					if (this.revNums.containsKey(mate)) {
						id = this.revNums.get(mate);
					} else {
						System.out.println(mate);
						continue;
					}
					String mName = "" + id;
					output.write(mName);
					output.write((char) 32);
					//System.out.print(mName + " ");
				}
				output.newLine();
			} catch (IOException e) {
				System.out.println("Could not write line" + entry.getKey());
			}
		}
		try {
			output.close();
		} catch (IOException e) {
			System.out.println("Could not close");
		}
		
		
	}
	
	private void writePlayerMaps () {
		try {
			this.urlMap = new BufferedWriter (new FileWriter(System.getProperty("user.dir") + "/files/urlMap.txt", false));
			this.writeNums = new BufferedWriter (new FileWriter(System.getProperty("user.dir") + "/files/numMap.txt", false));
			this.writeNames = new BufferedWriter (new FileWriter(System.getProperty("user.dir") + "/files/nameMap.txt", false));
		} catch (IOException e) {
			System.out.println("Could not write maps");
		}

		for (Map.Entry<String, String> entry : this.playerMap.entrySet()) {
			try {
				urlMap.write(entry.getKey() + "@" + entry.getValue());
				urlMap.newLine();
			} catch (IOException e) {
				System.out.println(e + "While writing URL map");
			}
		}
		try {
			urlMap.close();
		} catch (IOException e) {
			System.out.println(e + "while closing url map");
		}

		for (Map.Entry<Integer, String> entry : this.numMap.entrySet()) {
			try {
				writeNums.write(entry.getKey() + ":" + entry.getValue());
				writeNums.newLine();
			} catch (IOException e) {
				System.out.println(e + "while writing num map");
			}
		}
		try {
			writeNums.close();
		} catch (IOException e) {
			System.out.println(e + "while closing num map");
		}
		
		for  (Map.Entry<String, Integer> entry : this.revNums.entrySet()) {

			try {
				writeNames.write(entry.getKey() + ":" + entry.getValue());
				writeNames.newLine();
			} catch (IOException e) {
				System.out.println(e + "while writing rev num map");
			}
		}
		try {
			writeNames.close();
		} catch (IOException e) {
			System.out.println(e + "while closing name map");
		}
	}
	
	private void readInMaps () {
		try {
			this.urlRead = new BufferedReader (new FileReader(System.getProperty("user.dir") + "/files/urlMap.txt"));
			this.numRead = new BufferedReader (new FileReader(System.getProperty("user.dir") + "/files/numMap.txt"));
			this.nameRead = new BufferedReader (new FileReader(System.getProperty("user.dir") + "/files/nameMap.txt"));
		} catch (IOException e) {
			System.out.println("Could not read maps");
		}
		
		for (int i = 0; i < 4800; i++) {
			String wholeURL = "";
			String wholeNum = "";
			String wholeName = "";
			try {
				wholeURL = urlRead.readLine();
				wholeNum = numRead.readLine();
				wholeName = nameRead.readLine();
			} catch (IOException e) {
				System.out.println (e + "while reading line");
			}
			String[] cutURL = wholeURL.split("@");
			String urlName = cutURL[0].trim();
			String url = cutURL[1].trim();

			this.playerMap.put(urlName, url);
			
			String[] cutNum = wholeNum.split(":");
			String IDnum = cutNum[0].trim();
			String Namenum = cutNum[1].trim();
			this.numMap.put(Integer.parseInt(IDnum), Namenum);
			
			String[] cutName = wholeName.split(":");
			String NameName = cutName[0].trim();
			int NameID = Integer.parseInt(cutName[1].trim());
			this.revNums.put(NameName, NameID);
			
			
		}
	}
	
	
	/*
	 * Function that intitates the player map by going through every letter
	 * and getting players with that last inital, then adding that map to the 
	 * overall player map
	 * 
	 * Input: None
	 * Output: None
	 * 
	 */
	private void initiatePlayerMap () {
		TreeMap<String, String> fin = new TreeMap<String, String>();
		
		for (int i = 97; i < 123; i++) {
			char letter = (char) i;
			
			TreeMap<String, String> thisLetter = this.getPlayersByLetter(letter);
			fin.putAll(thisLetter);
		}
		
		this.playerMap = fin;
		
	}
	
	/*
	 * The website groups all players by last intial, so this function appends
	 * a given letter onto the players URL and gets the list of all players 
	 * of that letter
	 * 
	 * Input: Char of the desired letter
	 * Output: Map of every player of that letter to their page's URL
	 * 
	 */
	private TreeMap<String, String> getPlayersByLetter (char letter) {

		TreeMap<String, String> fin = new TreeMap<String, String>();
		Document letterDoc = null;
		String letterURL = playersURL + letter;
		
		try {
			letterDoc = Jsoup.connect(letterURL).get();
		} catch (IOException e) {
			System.out.println("Could not connect to players (letter " + letter + ")");
		}
		
		Elements tables = letterDoc.getElementsByClass("overthrow table_container");
		Element table = tables.get(0);
		Elements rows = table.getElementsByAttributeValue("scope", "row");
		
		for (Element row : rows) {
			String name = row.text().toLowerCase();

			//removes HOF asterisk
			String ast = "" + ((char) 42);
			if (name.contains(ast)) {
				name = name.substring(0, name.length() - 1);
			}
			
			Elements hrefEles = row.getElementsByAttribute("href");
			Element hrefEle = hrefEles.get(0);
			String suffix = hrefEle.attr("href");
			String[] cut = suffix.split("players/");
			String suff = cut[1];
			String wholeURL = this.playersURL + suff;
			
			//ACCOUNTS FOR DUPLICATE NAMES
			while (fin.containsKey(name)) {
				name = name + "#";
			}
			
			//CALLS HELPER FUNCTION TO SORT OUT SPECIAL CHARACTERS
			if (name.contains("�")) {
				name = removeWierdChar(name);
			}
			
			//ADD THE NAME TO ALL NECESSARY MAPS
			numMap.put(mapCounter, name);
			revNums.put(name, mapCounter);
			mapCounter++;
			fin.put(name, wholeURL);
			
		}
		return fin;
	}
	
	
	/*
	 * Function that gets every single player a given player
	 * has played with in his entire career, HashSet accounting for
	 * duiplicates. It gets every team the player has played on, then 
	 * calls the getRoster Function on that team
	 * 
	 * Input: Int of the Player's ID Number
	 * Output: HashSet of all the players the
	 * 	player has played with
	 * 
	 */
	private HashSet<String> getTeamMates (int playerID) {
		String playerName = this.numMap.get(playerID);
		HashSet<String> teammates = new HashSet<String>();
		
		String player = playerName.toLowerCase();
		String URL = this.playerMap.get(player);
		
		Document playerDoc = null;
		try {
			playerDoc = Jsoup.connect(URL).get();
		} catch (IOException e) {
			System.out.println("Could not get Player Page (getTeamMates)");
		}
		
		Element table = playerDoc.getElementById("per_game");
		Element body = table.selectFirst("tbody");
		
		Elements teams = body.getElementsByAttributeValue("data-stat", "team_id");
		
		for (Element team : teams) {
			Element href = team.selectFirst("a");
			if (href == null) {
				continue;
			}
			String url = href.attr("href");
			String[] cut = url.split("teams");
			String suffix = cut[1];
			String wholeUrl = this.teamsURL + suffix;
			HashSet<String> rost = this.getRosterSet(wholeUrl);
			teammates.addAll(rost);
		}
		//removed the player himself
		teammates.remove(playerName);
		return teammates;
		
	}
	
	
	/*
	 * Function that returns a set of the players on a roster given a URL
	 * 
	 * Input: URL of the Team
	 * Output: HashSet of the Roster
	 */
	private HashSet<String> getRosterSet (String url) {
		HashSet<String> fin = new HashSet<String>();
		Document rosterDoc = null;
		
		
		try {
			rosterDoc = Jsoup.connect(url).get();
		} catch (IOException e) {
			System.out.println("Could not Get Roster");
		}
		
		Element table = rosterDoc.getElementById("div_roster");
		
		Element body = table.selectFirst("tbody");
		Elements players = body.getElementsByAttributeValue("data-stat", "player");
		
		for (Element player : players) {
			String name = player.text().toLowerCase();
			
			//ACCOUNTS FOR A TWO WAY CONTRACT MARKER
			if (name.contains("tw")) {
				String[] cts = name.split("\\s\\(");
				
				name = cts[0];
			}
			
			//THESE ALL ACCOUNT FOR NAMES THAT ARE INCONSISTENT ON THE SITE
			if (name.contains("jr.")) {
				name = name.substring(0, name.length() - 4);
			}
			if (name.equals("taurean prince")) {
				name = "taurean waller-prince";
			} 
			if (name.equals("mo bamba")) {
				name = "mohamed bamba";
			}
			if (name.contains("frank mason iii")) {
				name = "frank mason";
			}
			if (name.equals("marvin bagley iii")) {
				name = "marvin bagley";
			}
			if (name.equals("gary payton ii")) {
				name = "gary payton#";
			}
			if (name.equals("glenn robinson iii")) {
				name = "glenn robinson#";
			}
			
			fin.add(name);
		}
		
		
		
		return fin;
		
		
	}
	
	//HELPER FUNCTION TO GET RID OF SPECIAL CHARS
	private static String removeWierdChar (String name) {
		if (name.contains("faverani")) {
			return "vítor faverani";
		} else if (name.contains("cristiano")) {
			return "cristiano felício";
		} else if (name.contains("rudy")) {
			return "rudy fernández";
		} else if (name.contains("juan")) {
			return "juan hernangómez";
		} else if (name.contains("willy")) {
			return "willy hernangómez";
		} else if (name.contains("nen")) {
			return "nenê hilário";
		} else if (name.contains("skal")) {
			return "skal labissière";
		} else if (name.contains("laprov")) {
			return "nicolás laprovíttola";
		} else if (name.contains("lasme")) {
			return "stéphane lasme";
		} else if (name.contains("felipe")) {
			return "felipe lópez";
		} else if (name.contains("pez")) {
			return "raül lópez";
		} else if (name.contains("luwawu")) {
			return "timothé luwawu-cabarrot";
		} else if (name.contains("ortiz")) {
			return "josé ortiz";
		} else {
			return name;
		}
	}
	
	//HELPER FUNCTION TO RETURN A COPY OF THE NUMBER MAP
	public TreeMap<Integer, String> getNumMap () {
		TreeMap<Integer, String> copy = new TreeMap<Integer, String>();
		copy.putAll(numMap);
		return copy;
	}
	
	//HELPER FUNCTION TO RETURN A COPY OF THE REVERSE NUMBER MAP
	public TreeMap<String, Integer> getRevNums() {
		TreeMap<String, Integer> copy = new TreeMap<String, Integer>();
		copy.putAll(revNums);
		return copy;
	}
	
	public String getPlayerDescripiton(String name) {
		String bio = "";
		try {
			Document page = Jsoup.connect(this.playerMap.get(name)).get();
			Element body = page.selectFirst("body");
			if (body != null) {
				Element div = body.selectFirst("div[id=meta]");
				if (div != null) {
					bio = div.text();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return bio;
	}

	public String getProfilePic(String name) {
		String url = "";
		try {
			Document page = Jsoup.connect(this.playerMap.get(name)).get();
			Element body = page.selectFirst("body");
			if (body != null) {
				Element div = body.selectFirst("div[class=media-item]");
				if (div == null) {
					return "https://d2p3bygnnzw9w3.cloudfront.net/req/202005051/tlogo/bbr/NBA-2020.png";
				}
				if (div != null) {
					Element img = div.selectFirst("img");
					if (img != null) {
						url = img.attr("src");
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		if (url.isEmpty()) {
			return "https://d2p3bygnnzw9w3.cloudfront.net/req/202005051/tlogo/bbr/NBA-2020.png";
		} else
			return url;
	}
}
