import java.io.*;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.TreeMap;
import javax.swing.JOptionPane;

public class Searcher {
	
	private RosterParser parser;
	
	private BufferedReader input;
	
	private TreeMap<Integer, String> numMap;
	private TreeMap<String, Integer> revMap;
	
	private String filePath;
	
	private ArrayList<LinkedList<Integer>> adjList = new ArrayList<LinkedList<Integer>>();
	
	public Searcher () {
		this.parser = new RosterParser();
		this.numMap = parser.getNumMap();
		this.revMap = parser.getRevNums();
		
		this.filePath = System.getProperty("user.dir") + "/Files/AdjList.txt";
		try {
			this.input = new BufferedReader(new FileReader(this.filePath));
			this.intitiateList();
		} catch (IOException e) {
			System.out.println("Could not connect to the file");
		}
	}
	
	private void intitiateList () {
		for (int i = 0; i < 4800; i++) {
			String wholeLine = "";
			try {
				wholeLine = input.readLine();
			} catch (IOException e) {
				System.out.println("Could not read line for player: " + i);
			}
			String[] cut = wholeLine.split(" ");
			LinkedList<Integer> neighbors = new LinkedList<Integer>();
			int playerID = Integer.parseInt(cut[0]);
			if (i != playerID) {
				throw new RuntimeException ("Error: initList");
			}
			for (int j = 1; j < cut.length; j++) {
				neighbors.add(Integer.parseInt(cut[j]));
			}
			adjList.add(playerID, neighbors);
		}
		
	}
	
	private LinkedList<Integer> getTeammates (int playerID) {
		LinkedList<Integer> mates = this.adjList.get(playerID);
		return mates;
	}
	
	
	/*
	 * Function to find the shortest path between 
	 * two players, using a parent node array to 
	 * keep track of the path
	 * 
	 */
	private ArrayList<Integer> bfsSearch (int start, int finish) {
		ArrayList<Integer> path = new ArrayList<Integer>();
		
		boolean [] disc = new boolean[4800];
		disc[start] = true;
		
		//Deque to keep track of what nodes need to be checked
		Deque<Integer> queue = new LinkedList<Integer>();
		queue.add(start);
		
		//A Parent node array allows us to keep track of 
		//the shortest path by knowing which node got us 
		// to this point
		int[] parentNode = new int[4800];
		parentNode[start] = -1;
		
		while (!queue.isEmpty()) {
			//Pull the next player out of the queue
			int currPlayer = queue.poll();
			
			//get this players all time teammates
			LinkedList<Integer> neighbors = this.getTeammates(currPlayer);
			
			//If he was teammates with the target, we set the target's
			// parent node to the current Player and break out of the 
			// loop
			if (neighbors.contains(finish)) {
				parentNode[finish] = currPlayer;
				break;
			} else {
				//Discover all the undiscovered neighbors and add them to the queue
				//IMPORTANT all of these nodes now have parent currPLayer, so we can
				// keep track of the path we took
				for (Integer id : neighbors) {
					if (!disc[id]) {
						queue.add(id);
						parentNode[id] = currPlayer;
						disc[id] = true;
					}
				}
			}
			
		}
		
		// Add nodes to the path by adding the target node, then its parent
		// and so on until you reach the start node
		int n = finish;
		while (n != -1) {
			path.add(n);
			n = parentNode[n];
		}
		
		return path;
	}
	
	/*
	 * Function that prints out the path
	 * between any two names;
	 */
	public void printPath (String begin, String tgt) {
		if (!this.revMap.containsKey(tgt) || !this.revMap.containsKey(begin)) {
			throw new IllegalArgumentException("Not a Player");
		}
		int start = this.revMap.get(begin);
		int end = this.revMap.get(tgt);
		
		ArrayList<Integer> path = this.bfsSearch(start, end);
		
		for (Integer id : path) {
			System.out.print(this.numMap.get(id));
			if (id != start) {
				System.out.println(", played with:");
				//System.out.println();
			}
		}
	}
	
	
	public void drawPath (String begin, String tgt) {
		int start = 0;
		int end = 0;
		try {
			start = this.revMap.get(begin);
			end = this.revMap.get(tgt);
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null, "Invalid Player Name(s)");
			this.run();
			return;
		}
		
		ArrayList<Integer> path = this.bfsSearch(start, end);
		
		NetsGui graphics = new NetsGui(path);
		graphics.drawPath(numMap);
	}
	
	public RosterParser getParser() {
		return this.parser;
	}

	public void run() {
		String start = JOptionPane.showInputDialog("Select the first player");
		String end = JOptionPane.showInputDialog("Select the second player");
		String player1 = start.toLowerCase();
		String player2 = end.toLowerCase();
		this.drawPath(player1, player2);
	}

	public static void main(String[] args) {
		new Searcher().run();
	}

}
