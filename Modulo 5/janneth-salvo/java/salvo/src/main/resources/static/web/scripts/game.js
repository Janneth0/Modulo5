$(function() {
    loadData();
});

function getParameterByName(name) {
    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
};

function loadData(){
    $.get('/api/game_view/'+getParameterByName('gp'))
        .done(function(data) {
            let playerInfo;
            if(data.gamePlayers.length==2){
                 if(data.gamePlayers[0].id == getParameterByName("gp")){
                 playerInfo = [data.gamePlayers[0].player.email,data.gamePlayers[1].player.email];
                 console.log(playerInfo);
                 $("#playerInfo").text(playerInfo[0] + '(you) vs ' + playerInfo[1]);
                 }
                 else{
                 playerInfo = [data.gamePlayers[1].player.email,data.gamePlayers[0].player.email];
                 $('#playerInfo').text(playerInfo[0]+ '(you) vs ' + playerInfo[1]);
                 }
            }
            else if(data.gamePlayers.length==1){
            playerInfo = data.gamePlayers;
            $('#playerInfo').text(playerInfo[0].player.email + '(you) VS Falta jugador');
            }
            /*if(data.gamePlayers[0].id == getParameterByName('gp'))
                playerInfo = [data.gamePlayers[0].player.email,data.gamePlayers[1].player.email];
            else
                playerInfo = [data.gamePlayers[1].player.email,data.gamePlayers[0].player.email];

            $('#playerInfo').text(playerInfo[0] + '(you) vs ' + playerInfo[1]);

            data.ships.forEach(function(shipPiece){
                shipPiece.shipLocations.forEach(function(location){
                    $('#'+location).addClass('ship-piece');
                })
            });*/
             data.ships.forEach(function(shipPiece){
                            shipPiece.shipLocations.forEach(function(location){
                            //console.log(location);
                                $('#'+location).addClass('ship-piece');
                               // console.log(location.toLowerCase());
                            })
             });
             console.log(location);


      if (data.ships.length > 0) {
        console.log("dandfgsdf");

        console.log(data.ships);
        $("#agregaBarcos").hide();
      } else {
        $("#agregaBarcos").show();
      };

      let salvosP1 = data.salvoes.filter(jugador => (jugador.player === Number(getParameterByName('gp'))))
      let salvosP2 = data.salvoes.filter(jugador => (jugador.player !== Number(getParameterByName('gp'))))
      console.log(salvosP1)
      console.log(salvosP2)

      salvosP1.forEach(shoot => {

        shoot.salvoes.forEach(hit => {
          $('#shoots' + hit.toLowerCase()).addClass('hit-cell').append(shoot.turno);
          console.log(shoot.turno);



        })
      })
      salvosP2.forEach(shoot => {
        shoot.salvoes.forEach(hit => {
          $('#ships' + hit.toLowerCase()).addClass('hit-cell').append(shoot.turno);
        })
      })
    })
    .fail(function (jqXHR, textStatus) {
      alert("Failed: " + textStatus);
    });
};

//createGrid(11, $(".grid-salvoes"), 'salvoes');
//lines 45 comienza la parte de pinter los tiros en game.js

//obtener ubicacion del tiro apartir de la linea 93- game.js

//84----math invoca metodos matematicos

//salvogrind.js no importa
//