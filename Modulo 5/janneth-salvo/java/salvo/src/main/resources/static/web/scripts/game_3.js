crearTabla();
leaderboard();
//newGame();

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
    //var Gtabla = '<thead class="thead-dark"><tr><th> Game ID</th><th>Fecha</th><th>Player1</th><th>Player2</th><th>State</th> ';
    var Gtabla = "<tbody>";
    data.games.forEach(function(game){
    Gtabla += "<tr>";
    Gtabla += "<td>" + game.id + "</td>";
    Gtabla += "<td>" +   new Date(game.created).toLocaleString()+ "</td>";
    Gtabla += "<td>" + game.gamePlayers[0].player.email + "</td>";
    Gtabla += "<td>" + (game.gamePlayers.length == 1 ? "      ":game.gamePlayers[1].player.email) + "</td>";

    if(data.player =="Guest"){
    Gtabla += "<td>hola guest</td>";
    }
    else if(data.player.email !="guest"){
        if(game.gamePlayers.length == 1 && game.gamePlayers[0].player.id!=data.player.id){
        Gtabla += "<td class='textCenter' ><button onclick='unir(" + game.id +")' data.gameid=' " + game.id +" ' >unirse</button ></td>";
        }
        if(game.gamePlayers.length == 1 && game.gamePlayers[0].player.id == data.player.id){
        Gtabla += "<td class='textCenter' ><button onclick='entrar("+game.gamePlayers[0].id+")' data.gameid=' " + game.id +" ' >ingresar</button ></td>";
        }
        if(game.gamePlayers.length == 2 ){
            if(game.gamePlayers[0].player.id == data.player.id){
              Gtabla += "<td class='textCenter' ><button class='entrar btn btn-danger font-weight-bold' onclick='entrar("+game.gamePlayers[0].id+")' data.gameid=' " + game.id +" ' >ingresar</button ></td>";
            }
            if(game.gamePlayers[1].player.id == data.player.id){
              Gtabla += "<td class='textCenter' ><button class='entrar btn btn-danger font-weight-bold' onclick='entrar("+game.gamePlayers[1].id+")' data.gameid=' " + game.id +" ' >ingresar</button ></td>";
            }
        }
    }
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
function newGame() {
    event.preventDefault();
    url = '/api/games/';
    $.post(url)
        .done(function (data) {
            return location.href = "/web/game.html?gp=" + data.gpid;
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
 //BOTON ENTRAR A JUEGO DEL QUE FORMO PARTE
 function entrar (gpid) {
   console.log("Estas dando click");
   alert("¡¡Regresaste!!");
   return location.href = "/web/game.html?gp=" + gpid;
 setTimeout(
  function () {
location.href = gameViewUrl;
   }, 1000);

 }
//FIN BOTON ENTRAR A JUEGO
 /*function unir() {
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
  }*/
  function unir(data) {
          event.preventDefault();
      url = '/api/games/' + data + '/players';
      $.post(url)
          .done(function (data) {
              return location.href = "/web/game.html?gp=" + data.gpid;
          })
  }

///////////////////
