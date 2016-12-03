
//
//	mmmmmmm
//	   #     m mm   mmm    mmm    mmm   m   m   m mm   mmm
//	   #     #"  " #"  #  "   #  #   "  #   #   #"  " #"  #
//	   #     #     #""""  m"""#   """m  #   #   #     #""""
//	   #     #     "#mm"  "mm"#  "mmm"  "mm"#   #     "#mm"
//
//	 m    m                 m                            "
//	 #    # m   m  m mm   mm#mm   mmm    m mm          mmm    mmm
//	 #mmmm# #   #  #"  #    #    #"  #   #"  "           #   #   "
//	 #    # #   #  #   #    #    #""""   #               #    """m
//	 #    # "mm"#  #   #    "mm  "#mm"   #       #       #   "mmm"
//	                                                     #
//	                                                   ""
// ******************************************************************

$(function() {
	// Document is ready
	updateScoreboard();
	setInterval(updateScoreboard, 3000);
});

var emptyImage = $('<img />').prop('src', '/assets/img/empty.png');
var treasureImage = $('<img />').prop('src', '/assets/img/chest.png');
var wallImage = $('<img />').prop('src', '/assets/img/wall.png');
var redUpImage = $('<img />').prop('src', '/assets/img/imp_red_up.png');
var redDownImage = $('<img />').prop('src', '/assets/img/imp_red_down.png');
var redLeftImage = $('<img >').prop('src', '/assets/img/imp_red_left.png');
var redRightImage = $('<img >').prop('src', '/assets/img/imp_red_right.png');
var yellowUpImage = $('<img >').prop('src', '/assets/img/imp_yellow_up.png');
var yellowDownImage = $('<img >').prop('src', '/assets/img/imp_yellow_down.png');
var yellowLeftImage = $('<img >').prop('src', '/assets/img/imp_yellow_left.png');
var yellowRightImage = $('<img >').prop('src', '/assets/img/imp_yellow_right.png');

emptyImage.load(imgLoaded);
treasureImage.load(imgLoaded);
wallImage.load(imgLoaded);
redUpImage.load(imgLoaded);
redDownImage.load(imgLoaded);
redLeftImage.load(imgLoaded);
redRightImage.load(imgLoaded);
yellowUpImage.load(imgLoaded);
yellowDownImage.load(imgLoaded);
yellowLeftImage.load(imgLoaded);
yellowRightImage.load(imgLoaded);

var imagesLoaded = 0;

function imgLoaded() {
	if (imagesLoaded >= 11) {			var currentReplay;
		console.log('All images have finished loading!');
	}
}

var imgs = {
	"EMPTY": emptyImage,
	"TREASURE": treasureImage,
	"WALL": wallImage,
	"RED_UP": redUpImage,
	"RED_DOWN": redDownImage,
	"RED_LEFT": redLeftImage,
	"RED_RIGHT": redRightImage,
	"YELLOW_UP": yellowUpImage,
	"YELLOW_DOWN": yellowDownImage,
	"YELLOW_LEFT": yellowLeftImage,
	"YELLOW_RIGHT": yellowRightImage
};

var currentFrameShowing = 0;
var currentMatchShowing = '';
var lastFetchedGameUUID = '';
var gameSummaries = {};
var timeoutEvents = [];

var playedGames = fromJSON(localStorage.getItem('playedGames'));
if (playedGames === null) playedGames = {};


// Do the necessary ajax calls to fetch fresh game data and populate the tables with it.
function updateScoreboard() {
	if (currentMatchShowing !== '') {
		return;
	}

	var queryParams = lastFetchedGameUUID === '' ? '' : "?lastSeenUUID=" + lastFetchedGameUUID;

	$.getJSON ("//localhost:8080/treasureHunterGameSummary.json" + queryParams, function(gameSummary) {

		for (var game in gameSummary) {
			 var contentRow = $('<tr>');

			if (gameSummaries[gameSummary[game]['uuid']] !== undefined) {
				continue;
			}

			gameSummaries[gameSummary[game]['uuid']] = gameSummary[game];
			lastFetchedGameUUID = gameSummary[game]['uuid'];
		}

		populateGameTable();
	});

}

function populateGameTable() {
	$('.table-new tr').remove();
	$('.table-viewed tr').remove();
	$('.table-viewed thead').append(buildHeader(gameSummaries[Object.keys(gameSummaries)[0]]));

	for (var game in gameSummaries) {
		var contentRow = $('<tr class="new-game-row">');

		if (playedGames[gameSummaries[game]["uuid"]] === undefined) {
			// This is an unplayed game. Add a play button.
			var value = '<input type="hidden" value="' + gameSummaries[game]["uuid"] + '">';
			var playButton = '<input type="button" class="play-btn" value="Play"></input>';
			var redPlayerNameLabel = '<span class="red-name-label">' + gameSummaries[game]["redPlayerName"] + '</span>';
			var yellowPlayerNameLabel = '<span class="yellow-name-label">' + gameSummaries[game]["yellowPlayerName"] + '</span>';

			contentRow.append($('<td colspan="12">').html(value + playButton + redPlayerNameLabel + " VS " + yellowPlayerNameLabel));
			contentRow.find('input[type=button]').click(playGame);

			$('.table-new tbody').prepend(contentRow);
			continue;
		}
		else {
			for (var data in gameSummaries[game]) {
				var value = gameSummaries[game][data];

				if (data === 'playerMoves' || data === 'initialBoardState') {
					value = '<input type="button" value="Replay match">';
				}
				else if (data === 'boardStateUpdates' || data === 'gameOutcome') {
					continue;
				}
				else if (data === 'redPlayerGameTime' || data === 'yellowPlayerGameTime') {
					value /= 1000.0;
				}
				else if (/*data === 'redPlayerGameTime' || data === 'yellowPlayerGameTime' ||*/ data === 'uuid') {
					value = '<input type="hidden" value="' + gameSummaries[game]["uuid"] + '">';
				}

				contentRow.append($('<td>').html(value));
			}

			contentRow.find('input[type=button]').click(playGame);
			$('.table-viewed tbody').prepend(contentRow);
		}

	}
}

function buildHeader(game) {
	var headerRow = $('<tr>');
	var translationTable = {
		'redPlayerName': 'Player Red',
		'redPlayerTreasures' : 'Red score',
		'redPlayerGameTime': 'Red computation time',
		'yellowPlayerName': 'Player Yellow',
		'yellowPlayerTreasures' : 'Yellow score',
		'yellowPlayerGameTime': 'Yellow computation time',
		'totalTreasures': 'Total treasures',
		'gameName': 'Name of the Game',
		'playerMoves': 'Replay',
		'draws': 'Draws',
		'redWins': 'Red victories',
		'yellowWins': 'Yellow victories',
		'initialBoardState': '',
		'gameStartDate': 'Battle took place',
		'uuid': ''
	};

	for (var data in game) {
		if (translationTable[data] !== undefined) {
			headerRow.append($('<th>').text(translationTable[data]));
		}
	}
	return headerRow;
}

function playGame(e) {
	clearExistingGame();

	var currentRow = $(this).closest('tr');
	var uuid = currentRow.find('input[type=hidden]').val();
	var initialBoardState = gameSummaries[uuid]['initialBoardState'];

	var resultRow = $('<tr class="result-row">');
	var resultCell = $('<td colspan="12" style="text-align: center;">');

	// Set up canvas
	var numColumns = initialBoardState.length;
	var numRows = initialBoardState[0].length;

	var canvas = $('<canvas id="currentGameCanvas"></canvas>');
	canvas.prop('width', numColumns * 32);
	canvas.prop('height', numRows * 32);

	currentRow.after(resultRow.append(resultCell.append(canvas)));

	currentMatchShowing = uuid;

	var gameField = $('#currentGameCanvas');
	var context = gameField[0].getContext('2d');

	// Clear screen before drawing initial board
	context.clearRect(0, 0, gameField.width(), gameField.height());

	for (var row = 0; row < numRows; row++) {
		for (var column = 0; column < numColumns; column++) {
			var state = initialBoardState[column][row];
			var img;
			img = imgs[state][0];

			context.drawImage(img, column * img.width, row * img.height);
		}
	}

	currentReplay = setInterval(function() {
		drawNextFrame();
	}, 100);
}

function clearExistingGame() {
	if (typeof currentReplay != 'undefined') {
		clearInterval(currentReplay);
	}

	for (var i = 0; i < timeoutEvents.length; i++) {
		clearTimeout(timeoutEvents[i]);
	}
	timeoutEvents = [];

	$('.result-row').remove();
	currentMatchShowing = '';
	currentFrameShowing = 0;
}

function drawNextFrame() {
	var boardUpdates = gameSummaries[currentMatchShowing]['boardStateUpdates'];

	if (currentFrameShowing >= boardUpdates.length) {
		showGameBriefing();
		return;
	}

	var gameField = $('#currentGameCanvas');
	var context = gameField[0].getContext('2d');

	var boardUpdate = fromJSON(boardUpdates[currentFrameShowing]);

	for (var i = 0; i < boardUpdate['changedTiles'].length; i++) {
		var tile = boardUpdate['changedTiles'][i];
		var state = tile.state;
		if (state === 'RED' || state === 'YELLOW') {
			state += '_' + tile.direction;
		}

		var img = imgs[state][0];
		var x = tile.coordinates.x;
		var y = tile.coordinates.y;

		context.clearRect(x * img.width, y * img.height, img.width, img.height);
		context.drawImage(img, x * img.width, y * img.height);
	}

	currentFrameShowing++;
}

function showGameBriefing() {
	clearInterval(currentReplay);

	var gameField = $('#currentGameCanvas')[0];
	var context = gameField.getContext('2d');
	var imgData = context.getImageData(0, 0, gameField.width, gameField.height);

	var transparency = 0.0;

	var fadeToBlack = function() {
		if (transparency >= 1) {
			showResultTitle();
			return;
		}
		context.putImageData(imgData, 0, 0);

		context.fillStyle = 'rgba(0, 0, 0, ' + transparency + ')';
		context.fillRect(0, 0, gameField.width, gameField.height);
		transparency += 0.02;

		timeoutEvents.push(setTimeout(fadeToBlack, 50));
	};

	fadeToBlack();
}

function showResultTitle() {
	var gameField = $('#currentGameCanvas')[0];
	var context = gameField.getContext('2d');

	var yellowPlayerName = gameSummaries[currentMatchShowing]['yellowPlayerName'];
	var redPlayerName = gameSummaries[currentMatchShowing]['redPlayerName'];

	var gameOutcome = gameSummaries[currentMatchShowing].gameOutcome;

	var showTitle = function() {
		context.fillStyle = '#000';
		context.fillRect(0, 0, gameField.width, gameField.height);

		context.font="70px Georgia";
		// Create gradient
		var gradient = context.createLinearGradient(0, 0, gameField.width, 0);
		gradient.addColorStop("0.15","#FCEABB");
		gradient.addColorStop("0.49","#fccd4d");
		gradient.addColorStop("0.5","#f8b500");
		gradient.addColorStop("0.6","#fbdf93");
		// Fill with gradient
		context.fillStyle=gradient;
		context.fillText("RESULTS", gameField.width / 2 - "RESULTS".length * 45 / 2, 100);

		timeoutEvents.push(setTimeout(showYellowPlayerTitle, 800));
	};

	var showYellowPlayerTitle = function() {
		context.font="40px Georgia";
		context.fillStyle = '#F9C02F';
		context.fillText(yellowPlayerName, gameField.width / 2 - 80 - yellowPlayerName.length * 20, 190);

		timeoutEvents.push(setTimeout(showYellowScore, 500));
	};

	var yellowScore = gameSummaries[currentMatchShowing]['yellowPlayerTreasures'];
	var yellowScoreIterator = 0;

	var showYellowScore = function() {
		if (yellowScoreIterator > yellowScore) {
			timeoutEvents.push(setTimeout(showRedPlayerTitle, 400));
			return;
		}

		context.fillStyle = '#000';
		context.fillRect(gameField.width / 2 - 80 - (yellowPlayerName.length * 16 / 2) - (2 * 25), 235, 100, 100);

		context.font="50px Georgia";
		context.fillStyle = '#fff';
		context.fillText(yellowScoreIterator, gameField.width / 2 - 80 - (yellowPlayerName.length * 16 / 2) - (2 * 25), 280);
		yellowScoreIterator++;
		timeoutEvents.push(setTimeout(showYellowScore, 200));
	};

	var showRedPlayerTitle = function() {
		context.font="40px Georgia";
		context.fillStyle = '#a90329';
		context.fillText(redPlayerName, gameField.width / 2 + 80, 190);

		timeoutEvents.push(setTimeout(showRedScore, 500));
	};

	var redScore = gameSummaries[currentMatchShowing]['redPlayerTreasures'];
	var redScoreIterator = 0;

	var showRedScore = function() {
		if (redScoreIterator > redScore) {
			timeoutEvents.push(setTimeout(showGameOutcome, 1000));
			return;
		}

		context.fillStyle = '#000';
		context.fillRect(gameField.width / 2 + 80 + redPlayerName.length * 15 / 2, 235, 100, 200);

		context.font="50px Georgia";
		context.fillStyle = '#fff';
		context.fillText(redScoreIterator, gameField.width / 2 + 80 + redPlayerName.length * 15 / 2, 280);
		redScoreIterator++;
		timeoutEvents.push(setTimeout(showRedScore, 200));
	};

	var showGameOutcome = function() {
		context.font= "40px Georgia";
		context.fillStyle = '#fff';
		context.fillText(gameOutcome, gameField.width / 2 - (gameOutcome.length * 19 / 2), gameField.height - 150);
		imgData = context.getImageData(0, 0, gameField.width, gameField.height);

		timeoutEvents.push(setTimeout(fadeToBlackEnding, 2500));
	};

	var imgData;
	var transparency = 0.0;

	var fadeToBlackEnding = function() {
		if (transparency >= 1) {
			endPreview();
			return;
		}
		context.putImageData(imgData, 0, 0);

		context.fillStyle = 'rgba(0, 0, 0, ' + transparency + ')';
		context.fillRect(0, 0, gameField.width, gameField.height);
		transparency += 0.02;

		timeoutEvents.push(setTimeout(fadeToBlackEnding, 50));
	};

	showTitle();
}


function endPreview() {
	//context.clearRect(0, 0, gameField.width(), gameField.height());

	var newPlayedGames = fromJSON(localStorage.getItem('playedGames'));
	if (newPlayedGames === null) newPlayedGames = {};

	newPlayedGames[currentMatchShowing] = 'PLAYED';
	localStorage.setItem('playedGames', toJSON(newPlayedGames));

	playedGames = fromJSON(localStorage.getItem('playedGames'));
	clearExistingGame();
	updateScoreboard();
}

function fromJSON(jsonString) {
	return JSON.parse(jsonString);
}

function toJSON(item) {
	return JSON.stringify(item);
}
