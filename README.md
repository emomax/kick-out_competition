# Friday Kick-out Competition
A wild collection of Friday Kick-Out competitions.

## Prerequisites
Make sure to have java and maven installed.

## Local server for testing
To set up the server-side locally in order to show the frontend, you whip out your favourite terminal and cd your way into the repository. After that you run "mvn spring-boot:run". This will start the server and hook it up to port 8080.

To compete against someone else, just select your game engine in GameRunner.java, set the correct IP in RemoteGame.java  (can be found through ipconfig/ifconfig). Select a cool username and set the gamename to what you and your opponent decides upon (they must also specify the same gamename, and the IP to the server - your IP if you run the server, ofc).

To try the server yourself, the ServerTest.java starts up two threads which takes turn making moves, utilizing two locally defined robots. The server tests should exist in all the different projects.

Goes without saying, a four-in-a-rowbot A.I. can't compete against a treasure hunter.

## Local frontend to view the games
To view the scoreboard of the Treasure Hunter games locally, you just cd into the *TreasureHunterFrontend* directory and start a server from there (a simple one, is 'python -m SimpleHTTPServer'). The index file will attempt to shoot the get requests towards the server, so make sure that the following line is pointed to the IP of your server (localhost, if you host it):

*$.getJSON ("//localhost:8080/treasureHunterGameSummary.json", function(gameSummary) {*

For the four-in-a-rowbot, you do the same steps as above, but in the *scoreboard* directory.


Note: The server will save played network games on disk, in the file 'savedGames', 'savedTreasureHunterGames' and so on.

# Projects
These are the current available projects:

## Treasure Hunter
In-house developed game where the two competitors attempts to find the shortest way to travel in order to collect as many treasures as possible!

* Import the project and make sure that it builds
* Implement *getNextMove()* in class MyN00bTreasureHunterGameEngine.java
* **OPTIONAL** - Rename 'MyN00bTreasureHunterGameEngine' to something wicked
* Enjoy the red and the yellow gargoyle insanely looking to collect more treasures than the other!

## Four-in-a-rowbot
Let's *(build a robot to)* play a classic game of four-in-a-row! You *(and your robot)* will compete against other people (and their robots)!

* Import the project and make sure that it builds
* Implement *getCoordinatesForNextMakerToPlace()* in class MyN00bGameEngine.java
* **OPTIONAL** - Rename 'MyN00bGameEngine' to something wicked
* Watch Arnold Schwarzenegger return from the future to protect you from people who never wanted you to write that magnificent A.I.

## Credits
* Max Teleman - *for writing the fantastic four-in-a-row-bot framework.*
* Josef Rönn (@jronn) - *for fixing the optimisation issues of saving and sending too much data in Treasure Hunter.*
