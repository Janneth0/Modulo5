var data;
var gamesData;
var playersArray;
var submitButton;

updateJson();

$(function () {
    $('.submitbutton').click(function () {
        submitButton = $(this).attr('name')
    });

});

$('#login-form').on('submit', function (event) {
    event.preventDefault();

    if (submitButton == "login") {
        $.post("/api/login",
            {
                username: $("#username").val(),
                password: $("#password").val()
            })
            .done(function () {
                console.log("login ok");
                $('#loginSuccess').show("slow").delay(2000).hide("slow");
                // $("#username").val("");
                $("#password").val("");
                updateJson();

            })
            .fail(function () {
                console.log("login failed");
                $('#loginFailed').show("slow").delay(2000).hide("slow");
                $("#username").val("");
                $("#password").val("");
                $("#username").focus();
                // $('#loginFailed').hide( "slow" );
            })
            .always(function () {

            });

    } else if (submitButton == "signup") {
        $.post("/api/players",
            {
                username: $("#username").val(),
                password: $("#password").val()
            })
            .done(function (data) {
                console.log("signup ok");
                console.log(data);
                $('#signupSuccess').show("slow").delay(2000).hide("slow");
                $.post("/api/login",
                    {
                        username: $("#username").val(),
                        password: $("#password").val()
                    })
                    .done(function () {
                        console.log("login ok");
                        $('#loginSuccess').show("slow").delay(2500).hide("slow");
                        $("#username").val("");
                        $("#password").val("");


                    })
                    .fail(function () {
                        console.log("login failed");
                        $('#loginFailed').show("slow").delay(2000).hide("slow");
                        $("#username").val("");
                        $("#password").val("");
                        $("#username").focus();
                        // $('#loginFailed').hide( "slow" );
                    })
                    .always(function () {

                    });
            })
            .fail(function (data) {
                console.log("signup failed");
                //console.log(data);
                $("#username").val("");
                $("#password").val("");
                $("#username").focus();
                $('#errorSignup').text(data.responseJSON.error);
                $('#errorSignup').show("slow").delay(3000).hide("slow");
            })
            .always(function () {

            });


    } else {
        //no button pressed
    }
});

$('#logout-form').on('submit', function (event) {
    event.preventDefault();
    $.post("/api/logout")
        .done(function () {
            console.log("logout ok");
            $('#logoutSuccess').show("slow").delay(2000).hide("slow");
            updateJson();
        })
        .fail(function () {
            console.log("logout fails");
        })
        .always(function () {

        });
});

$('#createGame').on('submit', function (event) {
    event.preventDefault();
    $.post("/api/games")
        .done(function (data) {
            console.log(data);
            console.log("game created");
            gameViewUrl = "/web/place-ship.html?gp=" + data.gpid;
            //gameViewUrl = "/web/game.html?gp=" + data.gpid;
            $('#gameCreatedSuccess').show("slow").delay(2000).hide("slow");
            setTimeout(
                function () {
                    location.href = gameViewUrl;
                }, 3000);
        })
        .fail(function (data) {
            console.log(data);
            console.log("game creation failed");
            $('#errorSignup').text(data.responseJSON.error);
            $('#errorSignup').show("slow").delay(4000).hide("slow");

        })
        .always(function () {

        });
});


function fetchJson(url) {
    return fetch(url, {
        method: 'GET',
        credentials: 'include'
    }).then(function (response) {
        if (response.ok) {
            return response.json();
        }
        throw new Error(response.statusText);
    });
}

function updateJson() {
    fetchJson('/api/games').then(function (json) {
        // do something with the JSON
        data = json;
        console.log(data.games)
        gamesData = data.games;
        updateView();
    }).catch(function (error) {
        // do something getting JSON fails
    });
}

function updateView() {
    showGamesTable(gamesData);
    console.log(gamesData);
    addScoresToPlayersArray(getPlayers(gamesData));
    console.log(playersArray);
    console.log("acabo de pasar add scores");
    showScoreBoard(playersArray);
    if (data.player == "Guest") {
        $('#currentPlayer').text(data.player);
        $('#logout-form').hide("slow");
        $('#login-form').show("slow");

    } else {
        $('#currentPlayer').text(data.player.email);
        $('#login-form').hide("slow");
        $('#logout-form').show("slow");

    }
}

function showGamesTable(gamesData) {
    var mytable = $('<table></table>').attr({id: "gamesTable", class: ""});
    var table = "#gamesList tbody";
    var gpid;
    $(table).empty();
    for (var i = 0; i < gamesData.length; i++) {

        var isLoggedPlayer = false;
        var joinButtonHtml = null;

        var DateCreated = new Date(gamesData[i].creationDate);
        DateCreated = DateCreated.getMonth() + 1 + "/" + DateCreated.getDate() + " " + DateCreated.getHours() + ":" + DateCreated.getMinutes();
        var row = $('<tr></tr>').prependTo(table);
        $('<td class="textCenter">' + gamesData[i].id + '</td>').appendTo(row);
        $('<td>' + DateCreated + '</td>').appendTo(row);


        for (var j = 0; j < gamesData[i].gamePlayer.length; j++) {


            if (gamesData[i].gamePlayer.length == 2) {
                $('<td>' + gamesData[i].gamePlayer[j].player.email + '</td>').appendTo(row);
            }
            if (gamesData[i].gamePlayer.length == 1 && (data.player == "Guest" || data.player.id == gamesData[i].gamePlayer[j].player.id)) {
                $('<td>' + gamesData[i].gamePlayer[0].player.email + '</td><td class="yellow500">WAITING FOR PLAYER</td>').appendTo(row);
            }
            if (gamesData[i].gamePlayer.length == 1 && data.player.id != null && data.player.id != gamesData[i].gamePlayer[j].player.id) {
                $('<td>' + gamesData[i].gamePlayer[0].player.email + '</td><td class="yellow500">WAITING FOR PLAYER</td>').appendTo(row);
                joinButtonHtml = '<td class="textCenter"><button class="joinGameButton btn btn-info" data-gameid=' + '"' + gamesData[i].id + '"' + '>JOIN GAME</button></td>';

            }
            if (gamesData[i].gamePlayer[j].player.id == data.player.id) {
                gpid = gamesData[i].gamePlayer[j].gpid;
                isLoggedPlayer = true;
            }
        }

        if (isLoggedPlayer === true) {
            var gameUrl = "/web/game.html?gp=" + gpid;
            $('<td class="textCenter"><a href=' + '"' + gameUrl + '"' + 'class="btn btn-warning" role="button">ENTER GAME</a></td>').appendTo(row);
        } else if (joinButtonHtml !== null) {
            $(joinButtonHtml).appendTo(row);
        } else {
            $('<td class="textCenter">-</td>').appendTo(row);
        }


    }
    $('.joinGameButton').click(function (e) {
        e.preventDefault();
        var joinGameUrl = "/api/game/" + $(this).data('gameid') + "/players";
        $.post(joinGameUrl)
            .done(function (data) {
                console.log(data);
                console.log("game joined");
                //gameViewUrl = "/web/game_2.html?gp=" + data.game;
                gameViewUrl = "/web/place-ship.html?gp=" + data.gpid;
                $('#gameJoinedSuccess').show("slow").delay(2000).hide("slow");
                setTimeout(
                    function () {
                        location.href = gameViewUrl;
                    }, 3000);
            })
            .fail(function (data) {
                console.log("game join failed");
                $('#errorSignup').text(data.responseJSON.error);
                $('#errorSignup').show("slow").delay(4000).hide("slow");

            })
            .always(function () {

            });
    });
}

function getPlayers(gamesData) {
    playersArray = [];
    var playersIds = [];

    for (var i = 0; i < gamesData.length; i++) {
        for (var j = 0; j < gamesData[i].gamePlayer.length; j++) {
            if (!playersIds.includes(gamesData[i].gamePlayer[j].player.id)) {
                playersIds.push(gamesData[i].gamePlayer[j].player.id);
                var playerScoreData = {
                    "id": gamesData[i].gamePlayer[j].player.id,
                    "email": gamesData[i].gamePlayer[j].player.email,
                    "scores": [],
                    "total": 0.0
                };
                playersArray.push(playerScoreData);
            }
        }
    }
    console.log(playersArray);
    console.log(data.player);
    return playersArray;
}

function addScoresToPlayersArray(playersArray) {
    for (var i = 0; i < gamesData.length; i++) {
        console.log(gamesData);
        for (var j = 0; j < gamesData[i].scores.length; j++) {

            var scorePlayerId = gamesData[i].scores[j].playerId;
            console.log(scorePlayerId);
            for (var k = 0; k < playersArray.length; k++) {

                if (playersArray[k].id == scorePlayerId) {
                    if (gamesData[i].scores[j].score != null) {
                        playersArray[k].scores.push(gamesData[i].scores[j].score);
                        playersArray[k].total += gamesData[i].scores[j].score;
                    }
                }
            }
        }
    }
}

function showScoreBoard(playersArray) {
    playersArray.sort(function (a, b) {
        return b.total - a.total;
    });

    var table = "#scoreBoard tbody";
    $(table).empty();

    console.log(playersArray);
    for (var m = 0; m < playersArray.length; m++) {
        var countWon = 0;
        var countLost = 0;
        var countTied = 0;

        if (playersArray[m].scores.length > 0) {

            for (var n = 0; n < playersArray[m].scores.length; n++) {
                if (playersArray[m].scores[n] == 0.0) {
                    countLost++;
                } else if (playersArray[m].scores[n] == 0.5) {
                    countTied++;
                } else if (playersArray[m].scores[n] == 1.0) {
                    countWon++;
                }
            }

            var row = $('<tr></tr>').appendTo(table);
            $('<td>' + playersArray[m].email + '</td>').appendTo(row);
            $("<td class='textCenter'>" + playersArray[m].total.toFixed(1) + '</td>').appendTo(row);
            $("<td class='textCenter'>" + countWon + '</td>').appendTo(row);
            $("<td class='textCenter'>" + countLost + '</td>').appendTo(row);
            $("<td class='textCenter'>" + countTied + '</td>').appendTo(row);
        }

    }


}








