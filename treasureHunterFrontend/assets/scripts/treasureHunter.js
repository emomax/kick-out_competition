
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
var gameSummaries = {};

var playedGames = fromJSON(localStorage.getItem('playedGames'));
if (playedGames === null) playedGames = {};


// Do the necessary ajax calls to fetch fresh game data and populate the tables with it.
function updateScoreboard() {
	if (currentMatchShowing !== '') {
		return;
	}

	$.getJSON ("//localhost:8080/treasureHunterGameSummary.json", function(gameSummary) {
		$('.table tr').remove();

		var headerRow = buildHeader(gameSummary);
		var contentRow;

		for (var game in gameSummary) {
			 contentRow = $('<tr>');

			if (gameSummaries[gameSummary[game['uuid']]] !== undefined) {
				return;
			}

			if (playedGames[gameSummary[game]['uuid']] === undefined) {
				// This is an unplayed game. Add a play button.
				gameSummaries[gameSummary[game]['uuid']] = gameSummary[game];

				var value = '<input type="hidden" value="' + gameSummary[game]["uuid"] + '">';
		 		var playButton = '<input type="button" value="Play"></input>';

				contentRow.append($('<td colspan="12">').html(value + playButton));
				contentRow.find('input[type=button]').click(playGame);

				$('.table tbody').append(contentRow);

				continue;
			}

			for (var data in gameSummary[game]) {
				var value = gameSummary[game][data];
				var hide = false;

				if (data === 'playerMoves') {
					value = '<input type="button" value="Replay match">';
				}
				else if (data === 'initialBoardState') {
					gameSummaries[gameSummary[game]['uuid']] = gameSummary[game];
					value = '<input type="button" value="Replay match">';
				}
				else if (data === 'boardStateUpdates') {
					hide = true;
				}
				else if (data === 'redPlayerGameTime' || data === 'yellowPlayerGameTime') {
					value /= 1000.0;
				}
				else if (/*data === 'redPlayerGameTime' || data === 'yellowPlayerGameTime' ||*/ data === 'uuid') {
					value = '<input type="hidden" value="' + gameSummary[game]["uuid"] + '">';
				}

				if (hide === false) {
				    contentRow.append($('<td>').html(value));
				}
			}

			contentRow.find('input[type=button]').click(playGame);
			$('.table tbody').append(contentRow);
		}

		$('.table thead').append(headerRow);
		$(".table tbody").each(function(elem,index){
			  var arr = $.makeArray($("tr",this).detach());
			  arr.reverse();
			  $(this).append(arr);
		});

	});
}

function buildHeader(games) {
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

	for (var data in games[0]) {
		if (translationTable[data] !== undefined) {
			headerRow.append($('<th>').text(translationTable[data]));
		}
	}
	return headerRow;
}

function playGame(e) {
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

function drawNextFrame() {
	var rounds = gameSummaries[currentMatchShowing]['boardStateUpdates'];

	if (currentFrameShowing >= rounds.length) {
		showGameBriefing();
		return;
	}

	var columnLength = rounds[0].length;
	var rowLength = rounds[0][0].length;

	var gameField = $('#currentGameCanvas');
	var context = gameField[0].getContext('2d');

	var state = rounds[currentFrameShowing];

	var regex = /^old-(\d+):(\d+)state(.+)new-(\d+):(\d+)state(.*)$/g;
	var match = regex.exec(state);

	var oldX = match[1];
	var oldY = match[2];
	var oldState = match[3];

	var oldImg = imgs[oldState][0];
	context.clearRect(oldX * oldImg.width, oldY * oldImg.height, oldImg.width, oldImg.height);
	context.drawImage(oldImg, oldX * oldImg.width, oldY * oldImg.height);

	var newX = match[4];
	var newY = match[5];
	var newState = match[6];

	var newImg = imgs[newState][0];
	context.clearRect(newX * newImg.width, newY * newImg.height, newImg.width, newImg.height);
	context.drawImage(newImg, newX * newImg.width, newY * newImg.height);

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

		setTimeout(fadeToBlack, 50);
	};

	fadeToBlack();
}

function showResultTitle() {
	var gameField = $('#currentGameCanvas')[0];
	var context = gameField.getContext('2d');

	var yellowPlayerName = gameSummaries[currentMatchShowing]['yellowPlayerName'];
	var redPlayerName = gameSummaries[currentMatchShowing]['redPlayerName'];

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

		setTimeout(showYellowPlayerTitle, 800);
	};

	var showYellowPlayerTitle = function() {
		context.font="40px Georgia";
		context.fillStyle = '#F9C02F';
		context.fillText(yellowPlayerName, gameField.width / 2 - 80 - yellowPlayerName.length * 20, 190);

		setTimeout(showYellowScore, 500);
	};

	var yellowScore = gameSummaries[currentMatchShowing]['yellowPlayerTreasures'];
	var yellowScoreIterator = 0;

	var showYellowScore = function() {
		if (yellowScoreIterator > yellowScore) {
			setTimeout(showRedPlayerTitle, 400);
			return;
		}

		context.fillStyle = '#000';
		context.fillRect(gameField.width / 2 - 80 - (yellowPlayerName.length * 16 / 2) - (2 * 25), 235, 100, 100);

		context.font="50px Georgia";
		context.fillStyle = '#fff';
		context.fillText(yellowScoreIterator, gameField.width / 2 - 80 - (yellowPlayerName.length * 16 / 2) - (2 * 25), 280);
		yellowScoreIterator++;
		setTimeout(showYellowScore, 200);
	};

	var showRedPlayerTitle = function() {
		context.font="40px Georgia";
		context.fillStyle = '#a90329';
		context.fillText(redPlayerName, gameField.width / 2 + 80, 190);

		setTimeout(showRedScore, 500);
	};

	var redScore = gameSummaries[currentMatchShowing]['redPlayerTreasures'];
	var redScoreIterator = 0;

	var showRedScore = function() {
		if (redScoreIterator > redScore) {
			imgData  = context.getImageData(0, 0, gameField.width, gameField.height);
			setTimeout(fadeToBlackEnding, 4000);
			return;
		}

		context.fillStyle = '#000';
		context.fillRect(gameField.width / 2 + 80 + redPlayerName.length * 15 / 2, 235, 100, 200);

		context.font="50px Georgia";
		context.fillStyle = '#fff';
		context.fillText(redScoreIterator, gameField.width / 2 + 80 + redPlayerName.length * 15 / 2, 280);
		redScoreIterator++;
		setTimeout(showRedScore, 200);
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

		setTimeout(fadeToBlackEnding, 50);
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
	currentMatchShowing = '';
	currentFrameShowing = 0;
	updateScoreboard();

	$('.result-row').remove();
}

function fromJSON(jsonString) {
	return JSON.parse(jsonString);
}

function toJSON(item) {
	return JSON.stringify(item);
}