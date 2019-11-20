crearTabla();
leaderboard();
newGame();
function leaderboard() {
    $.get('/api/leaderBoard')
        .done(function (data) {
            data.sort((a, b) => {
                return b.score.puntajeTotal - a.score.puntajeTotal
            });
            scoreTable(data);
        });
}
function crearTabla(){
    $.get("/api/games")
        .done(function(data){
            data.games.sort();
            gameTabla(data);
         } );
}
function gameTabla(data){
    let tgameFormateada = addTableGameHTML(data);
    let tGames = document.getElementById("gamesInfo");
    tGames.innerHTML = tgameFormateada;
}
/*function addTableGameHTML(data){
    var Gtabla = '<thead class="thead-dark"><tr><th> Game ID</th><th>Fecha</th><th>Player1</th><th>Player2</th><th>State</th> ';
    Gtabla += "<tbody>";
    data.games.forEach(function(game){
    Gtabla += "<tr>";
    Gtabla += "<td>" + game.id + "</td>";
    Gtabla += "<td>" +   new Date(game.created).toLocaleString()+ "</td>";
    Gtabla += "<td>" + game.gamePlayers[0].player.email + "</td>";
    Gtabla += "<td>" + (game.gamePlayers.length == 1 ? "      ":game.gamePlayers[1].player.email) + "</td>";
    if(game.gamePlayers.length == 1 && game.gamePlayers[0].player.id!=data.player.id){
    Gtabla += "<td class='textCenter' ><button class='joinGameButton' data.gameid=' " + game.id +" ' >unirse</button ></td>";
    };
    return Gtabla;
})
}*/
function addTableGameHTML(data){
var playerLogueado = data.player.email;

    var Gtabla = '<thead class="thead-dark"><tr><th> Game ID</th><th>Fecha</th><th>Player1</th><th>Player2</th><th>State</th> ';
    Gtabla += "<tbody>";
    data.games.forEach(function(game){
    Gtabla += "<tr>";
    Gtabla += "<td>" + game.id + "</td>";
    Gtabla += "<td>" +   new Date(game.created).toLocaleString()+ "</td>";
    Gtabla += "<td>" + game.gamePlayers[0].player.email + "</td>";
    Gtabla += "<td>" + (game.gamePlayers.length == 1 ? "      ":game.gamePlayers[1].player.email) + "</td>";

    if(data.player =="guest"){
    Gtabla += "<td>hola guest</td>";
    }

    else if(data.player.email !="guest"){
        if(game.gamePlayers.length == 1 && game.gamePlayers[0].player.id!=data.player.id){
        Gtabla += "<td class='textCenter' ><button onclick='unir()' data.gameid=' " + game.id +" ' >unirse</button ></td>";
        }
        if(game.gamePlayers.length == 1 && game.gamePlayers[0].player.id == data.player.id){
        Gtabla += "<td class='textCenter' ><button onclick='entrar()' data.gameid=' " + game.id +" ' >ingresar</button ></td>";
        }
        if(game.gamePlayers.length == 2 && game.gamePlayers[0].player.id == data.player.id){
        console.log("if dos jugadores ")
        Gtabla += "<td class='textCenter' ><button onclick='entrar()' data.gameid=' " + game.id +" ' >ingresar</button ></td>";
        }

    }


    });
    return Gtabla;
    $('.joinGameButton').click(function (e) {
            e.preventDefault();
            var joinGameUrl = "/api/game/" + $(this).data('gameid') + "/players";
            $.post(joinGameUrl)
                .done(function (data) {
                    console.log(data);
                    console.log("game joined");
                    gameViewUrl = "/web/game_2.html?gp=" + data.game;
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

$('.joinGameButton').click(function (e) {
        e.preventDefault();
        var joinGameUrl = "/api/game/" + $(this).data('gameid') + "/players";
        $.post(joinGameUrl)
            .done(function (data) {
                console.log(data);
                console.log("game joined");
                gameViewUrl = "/web/game_2.html?gp=" + data.game;
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
Contraer





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
function newGame() {
 event.preventDefault();
    url = '/api/games';
    $.post(url)
        .done(function (data) {
            console.log(data);
            return location.href = "/web/game.html?gp=" + data.gpId;
            //var gameViewUrl ="/web/game.html?gp="+ data.gpId;
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
            alert("Usuario existente")
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
function entrar (gPId) {

   console.log("Estas dando click");
   alert("¡¡Regresaste!!");

gameViewUrl = "/web/game.html?gp=" + gPId;

 setTimeout(
  function () {
location.href = gameViewUrl;
   }, 1000);

 }
 function unir() {
    let botonUnir = document.getElementsByName("botonUnir")
    botonUnir.forEach(a => a.addEventListener("click", function () {
      alert("¡¡Gracias por sumarte!!");

     let gameId = this.getAttribute("data-gameId");

  console.log(gameId);


      $.post("/api/game/" +  gameId + "/player/")

        .done(function (data) {
          console.log(data);

          return location.href = "/web/game.html?gp=" +  data.gpid;

        }).fail(error => console.log(error))
    }))

  }

///////////////////

