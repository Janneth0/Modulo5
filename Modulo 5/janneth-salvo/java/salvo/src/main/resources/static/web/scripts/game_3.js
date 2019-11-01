
function leaderboard() {

    $.get('/api/leaderBoard')
        .done(function (data) {
        console.log(data);

            data.sort((a, b) => {
                return b.score.puntajeTotal - a.score.puntajeTotal
            });


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
            scoreTable(data);
        });
}
leaderboard();

function crearTabla(){
    $.get("/api/games")
        .done(function(data){
            data.sort();


            function gameTabla(data){
            console.log(data);
                let tgameFormateada = addTableGHTML(data);
                let tGames = document.getElementById("gamesInfo");
                tGames.innerHTML = tgameFormateada;
            }

            function addTableGHTML(data){

                var Gtabla = '<thead class="thead-dark"><tr><th> Game ID</th><th>Fecha</th><th>Player1</th><th>Player2</th><th>State</th> ';
                Gtabla += "<tbody>";
                data.forEach(function(players){
                Gtabla += "<tr>";
                Gtabla += "<td>" + players.id + "</td>";
                Gtabla += "<td>" +   new Date(players.created).toLocaleString()+ "</td>";
                Gtabla += "<td>" + players.gamePlayers[0].player.email + "</td>";
           Gtabla += "<td>" + (players.gamePlayers.length == 1 ? "      ":players.gamePlayers[1].player.email) + "</td>";
                Gtabla += "<td></td>";
                });
                return Gtabla;
            }

            gameTabla(data);
         } );

}
crearTabla();
