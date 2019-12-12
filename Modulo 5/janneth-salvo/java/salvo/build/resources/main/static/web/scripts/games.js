crearTabla();
leaderboard();
//newGame();
///////////////////////////////////////////////////////////////////////////////
function leaderboard() {
    $.get('/api/leaderBoard')
        .done(function (data) {
            data.sort((a, b) => {
                return b.score.puntajeTotal - a.score.puntajeTotal
            });
            scoreTable(data);
        });
}
/////////////////////////////////////////////////////////////////////////////////
function crearTabla(){
    $.get("/api/games")
        .done(function(data){
            data.games.sort();
            gameTabla(data);
         } );
}
////////////////////////////////////////////////////////////////////////////////
function gameTabla(data){
    let tgameFormateada = addTableGameHTML(data);
    let tGames = document.getElementById("gamesInfo");
    tGames.innerHTML = tgameFormateada;
}
//////////////////////////////////////////////////////////////////////////////////
function addTableGameHTML(data){
var playerLogueado = data.player.email;
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
         console.log(game.gamePlayers[0].gpid);
        Gtabla += "<td class='textCenter' ><button onclick='unir(" + game.id +")' data.gameid=' " + game.id +" ' >unirse</button ></td>";
        console.log(game.gamePlayers[0].gpid);

        }
        if(game.gamePlayers.length == 1 && game.gamePlayers[0].player.id == data.player.id){
        Gtabla += "<td class='textCenter' ><button onclick='entrar("+game.gamePlayers[0].gpid+")' data.gameid=' " + game.id +" ' >ingresar</button ></td>";
        console.log(game.gamePlayers[0].gpid);

        }
        if(game.gamePlayers.length == 2 ){
            if(game.gamePlayers[0].player.id == data.player.id){
              Gtabla += "<td class='textCenter' ><button class='entrar btn btn-danger font-weight-bold' onclick='entrar("+game.gamePlayers[0].gpid+")' data.gameid=' " + game.id +" ' >ingresar</button ></td>";
            console.log(game.gamePlayers[0].gpid);
            }
            if(game.gamePlayers[1].player.id == data.player.id){
              Gtabla += "<td class='textCenter' ><button class='entrar btn btn-danger font-weight-bold' onclick='entrar("+game.gamePlayers[1].gpid+")' data.gameid=' " + game.id +" ' >ingresar</button ></td>";
            console.log(game.gamePlayers[0].gpid);
            }
        }

    }
    });
    return Gtabla;
}
///////////////////////////////////////////////////////////////////////////////////
function scoreTable(data) {
     let tablaFormateada = addTableHTML(data);
     let tablaScore = document.getElementById("tablaLider");
     tablaScore.innerHTML = tablaFormateada;
}
///////////////////////////////////////////////////////////////////////////////////
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
//////////////////////////////////////////////////////////////////////////////////
function newGame() {
    event.preventDefault();
    url = '/api/games/';
    $.post(url)
        .done(function (data) {
            return location.href = "/web/place-ship.html?gp=" + data.gpid;
        })
}
/////////////////////////////////////////////////////////////////////////////
function logIn() {
    event.preventDefault();
    $.post("/api/login", {
            username: $("#username").val(),
            password: $("#password").val()
        })
        .done(function () {
            $('#loginSuccess').show("slow").delay(2000).hide("slow");
            crearTabla();
            $("#login-form").hide(),
                $("#logout-form").show(),
                $("#password").val("")
        })
        .fail(function () {
            console.log("Failed to LogIn");
               $('#loginFailed').show("slow").delay(2000).hide("slow");
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
        crearTabla();
            console.log("bye");
            $('#logoutSuccess').show("slow").delay(2000).hide("slow");
            crearTabla();
            $("#logout-form").hide();
            $("#login-form").show()
        })
        .fail(function () {
            console.log("Failed to LogOut")
        });
};
 //BOTON ENTRAR A JUEGO DEL QUE FORMO PARTE
 function entrar (gpid) {
     event.preventDefault();
   console.log("Estas dando click");
   alert("¡¡Regresaste!!");
   return location.href = "/web/game.html?gp=" + gpid;
 setTimeout(
  function () {
location.href = gameViewUrl;
   }, 1000);

 }
  function unir(data) {
          event.preventDefault();
      url = '/api/games/' + data + '/players';
      $.post(url)
          .done(function (data) {
              return location.href = "/web/place-ship.html?gp=" + data.gpid;
          })
  }

///////////////////
