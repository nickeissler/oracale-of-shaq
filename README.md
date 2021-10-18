# Oracle of Shaq
Created by Nicholas Eissler and Matthew Cornell

## Description:
This project is an NBA-focused version of the "Oracle of Bacon". Where the oracle of Bacon connects actors having appeared in a movie together, 
Degrees of Shaq connects players who have played on the same team. The goal of this project was to investigate how interconnected the NBA is historically.

### RosterParser.java:
Code used to scrape website (NBAreference.com) for player URLs, teammates, and profile pictures.  Each player was enumerated and mapped to every unique teammate over their career.  Special characters and names were also accounted for in this file.  Used to construct adjacency list of all players to their teammates and maps to player names and enumeration as well as names to personal URL.

### Searcher.java:
The main class used to find the shortest path using the maps created in RosterParser.  Takes in two names of past or present NBA players and runs a BFS on the adjacency list to find the shortest path.

### NetsGui.java:
Class used to create GUI.  Displays textbox prompting for player names and displays shortest path found with pictures of players from NBAreference.com if availible.

## How To Run:
This program is run from the searcher class. No command line arguments are necessary. Running the Searcher class will bring up two dialog boxes, prompting the user 
for the name of an nba player. After filling out one, the next will pop up. Once the use has input two name, the program will produce a window that will load in a 
list of players that connects the two inputs. Under each player name is a button. Clicking on the button will show the players picture, and then a description of 
the player, also scraped from basketball-reference.com using JSoup. If the user inputs an invalid name, the program will say so, and ask again. The input name must 
be an exact match of a player who has been on an NBA roster for a season INCLUDING punctionation (hyphens and apostrophes).

IMPORTANT NOTE: The program will not run without the JSoup Library installed. The JAR has been included in the ZIP and must be imported into eclipse for the 
program to run

