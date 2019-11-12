crearTabla();
leaderboard();

function leaderboard() {

    $.get('/api/leaderBoard')
        .done(function (data) {
        console.log(data);

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

    console.log(data);
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
            alert("User not registered")
        });
}

function signUp() {
    event.preventDefault();

    $.post("/api/players", {
            username: $("#username").val(),
            password: $("#password").val()
        })
        .done(function () {
            console.log("data");
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


function createGame(){
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
}






