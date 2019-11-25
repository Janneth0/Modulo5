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

        })
        .fail(function( jqXHR, textStatus ) {
          alert( "Failed: " + textStatus );
        });
};