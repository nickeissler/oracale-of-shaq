# Oracle of Shaq
Created by Nicholas Eissler and Matthew Cornell

Project Name: Degrees of Shaq

# Description:
This project is an NBA-focused version of the "Oracle of Bacon". Where the oracle of Bacon connects actors having appeared in a movie together, 
Degrees of Shaq connects players who have played on the same team. The goal of this project was to investigate how interconnected the NBA is historically.

Our project implements the use of graph algorithms and physical and information networks. Our graph algorithm is a BFS search of the graph of NBA players. 
This search yields the shortest path between two players (bfsSearch method in the Searcher class). For physical and information networks, we used Jsoup to scrape 
data from basketball-reference.com, in order to get Maps attaching players to their pages' URL, get the teams they have played on, and get those teams rosters. 
Then, by creating a map of players to ID integers, we wrote the information to a .txt file that acted as an Adjacency list to search through. 

# How To Run:
This program is run from the searcher class. No command line arguments are necessary. Running the Searcher class will bring up two dialog boxes, prompting the user 
for the name of an nba player. After filling out one, the next will pop up. Once the use has input two name, the program will produce a window that will load in a 
list of players that connects the two inputs. Under each player name is a button. Clicking on the button will show the players picture, and then a description of 
the player, also scraped from basketball-reference.com using JSoup. If the user inputs an invalid name, the program will say so, and ask again. The input name must 
be an exact match of a player who has been on an NBA roster for a season INCLUDING punctionation (hyphens and apostrophes).

IMPORTANT NOTE: The program will not run without the JSoup Library installed. The JAR has been included in the ZIP and must be imported into eclipse for the 
program to run

