crearTabla();
leaderboard();
updateView();

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
            gamesData = data.games;
            updateView();
        }).catch(function (error) {
            // do something getting JSON fails
        });
}


function leaderboard() {

    $.get('/api/leaderBoard')
        .done(function (data) {
        console.log("leaderboard"+data);

            data.sort((a, b) => {
                return b.score.puntajeTotal - a.score.puntajeTotal
            });

            scoreTable(data);
        });
}

function updateView() {
        showGamesTable(gamesData);
        addScoresToPlayersArray(getPlayers(gamesData));
        showScoreBoard(playersArray);
        if (data.player == "Guest") {
            $('#currentPlayer').text(data.player);
            $('#logout-form').hide("slow");
            $('#login-form').show("slow");
            $("#createGameForm").hide();

        } else {

            $('#currentPlayer').text(data.player.email);
            $('#login-form').hide("slow");
            $('#logout-form').show("slow");

        }
}

function showGamesTable(data) {
        // let mytable = $('<table></table>').attr({id: "gamesTable", class: ""});
        var table = "#gamesList tbody";
        var gpid;
        $(table).empty();
        for (var i = 0; i < data.length; i++) {

            var isLoggedPlayer = false;
            var joinButtonHtml = null;

            var DateCreated = new Date(gamesData[i].created);
            DateCreated = DateCreated.getMonth() + 1 + "/" + DateCreated.getDate() + " " + DateCreated.getHours() + ":" + DateCreated.getMinutes();
            var row = $('<tr></tr>').prependTo(table);
            $('<td class="textCenter">' + gamesData[i].id + '</td>').appendTo(row);
            $('<td>' + DateCreated + '</td>').appendTo(row);


            for (var j = 0; j < gamesData[i].gamePlayers.length; j++) {


                if (gamesData[i].gamePlayers.length == 2) {
                    $('<td>' + gamesData[i].gamePlayers[j].player.email + '</td>').appendTo(row);
                }
                if (gamesData[i].gamePlayers.length == 1 && (data.player == "Guest" || data.player.id == gamesData[i].gamePlayers[j].player.id)) {
                    $('<td>' + gamesData[i].gamePlayers[0].player.email + '</td><td class="yellow500">WAITING FOR PLAYER</td>').appendTo(row);
                }
                if (gamesData[i].gamePlayers.length == 1 && data.player.id != null && data.player.id != gamesData[i].gamePlayers[j].player.id) {
                    $('<td>' + gamesData[i].gamePlayers[0].player.email + '</td><td class="yellow500">WAITING FOR PLAYER</td>').appendTo(row);
                    joinButtonHtml = '<td class="textCenter"><button class="joinGameButton btn btn-info" data-gameid=' + '"' + gamesData[i].id + '"' + '>JOIN GAME</button></td>';

                }
                if (gamesData[i].gamePlayers[j].player.id == data.player.id) {
                    gpid = gamesData[i].gamePlayers[j].id;
                    isLoggedPlayer = true;
                }
            }

            if (isLoggedPlayer === true) {
                var gameUrl = "/web/game.html?gp=" + gpid;
                $('<td class="textCenter"><a href=' + '"' + gameUrl + '"' + 'class="btn btn-warning" role="button">ENTER GAME</a></td>').appendTo(row);
            } else if (joinButtonHtml !== null){
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
                gameViewUrl = "/web/game.html?gp=" + data.gpid;
                $('#gameJoinedSuccess').show("slow").delay(2000).hide("slow");
                setTimeout(
                   function()
                  {
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



function crearTabla(){

    $.get("/api/games")
        .done(function(data){
            data.games.sort();
            console.log("crear tabla"+data);
            gameTabla(data);
         } );
}

function gameTabla(data){

    console.log("gametabla"+data);
    let tgameFormateada = addTableGameHTML(data);
    let tGames = document.getElementById("gamesInfo");
    tGames.innerHTML = tgameFormateada;
}

function addTableGameHTML(data){

    var Gtabla = '<thead class="thead-dark"><tr><th> Game ID</th><th>Fecha</th><th>Player1</th><th>Player2</th><th>State</th> ';
    Gtabla += "<tbody>";
    data.games.forEach(function(game){
    Gtabla += "<tr>";
    Gtabla += "<td>" + game.id + "</td>";
    Gtabla += "<td>" +   new Date(game.created).toLocaleString()+ "</td>";
    Gtabla += "<td>" + game.gamePlayers[0].player.email + "</td>";
    Gtabla += "<td>" + (game.gamePlayers.length == 1 ? "      ":game.gamePlayers[1].player.email) + "</td>";
    Gtabla += "<td></td>";
    });
    return Gtabla;
}
function scoreTable(data) {
     let tablaFormateada = addTableHTML(data);
     let tablaScore = document.getElementById("tablaLider");
     tablaScore.innerHTML = tablaFormateada;
}
function addTableHTML(data) {
    var tabla = '<thead  class="thead-dark" ><tr><th>Full Name</th><th>Total</th><th>Won</th><th>Lost</th><th>Tied</th></tr></thead>';
    tabla += "<tbody>";
    data.forEach(function (jugador) {
        tabla += '<tr>';
        tabla += '<td>' + jugador.userName + '</td>';
        tabla += '<td>' + jugador.score.total + '</td>';
        tabla += '<td>' + jugador.score.won + '</td>';
        tabla += '<td>' + jugador.score.tied + '</td>';
        tabla += '<td>' + jugador.score.lost + '</td>';
        tabla += '</tr>';
        tabla += '</tbody>';
    });
    return tabla;
}
function newGame(data) {
 event.preventDefault();
    url = '/api/games';
    $.post(url)
        .done(function () {
            //return location.href = "/web/game.html?gp=" + data.gpId;
            var gameViewUrl ="/web/game.html?gp="+ data.gpId;
        })
}


function logIn() {
    event.preventDefault();

    $.post("/api/login", {
            username: $("#username").val(),
            password: $("#password").val()
        })
        .done(function () {
            crearTabla()
            $("#login-form").hide(),
                $("#logout-form").show(),
                $("#password").val("")
        })
        .fail(function () {
            console.log("Failed to LogIn");
            //alert("User not registered")
            alert("Usuario no Registrado")
        });
}

function signUp() {
    event.preventDefault();

    $.post("/api/players", {
            username: $("#username").val(),
            password: $("#password").val()
        })
        .done(function () {


            logIn();
            $("#login-form").hide(),
                $("#logout-form").show(),
                $("#password").val("")
        })
        .fail(function () {
            console.log("Failed to LogIn");
            alert("User not registered")
        });
};

function logout() {
    event.preventDefault();

    $.post("/api/logout")
        .done(function () {
            console.log("bye");
            $("#logout-form").hide();
            $("#login-form").show()
        })
        .fail(function () {
            console.log("Failed to LogOut")
        });
};







/*function newGame(){
  console.log("creando juego");
    $.post("/api/games")
        .done(function(data){
            console.log(data);
            console.log("juego creado");
          var gameViewUrl ="/web/game.html?gp="+ data.gpId;
            //$('gameCreatedSuccess').show("slow").delay(2000).hide("slow").delay(2000);
            setTimeout(function(){ location.href=gameViewUrl; },2000);
        })
        .fail(function(data){
            console.log("game creation failed");
        });
}*/






