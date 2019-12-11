

$(function () {
  loadData();
});

function getParameterByName(name) {
  var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
  return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}

function loadData() {
  $.get('/api/game_view/' + getParameterByName('gp'))
    .done(function (data) {
      var playerInfo;
      if(data.gamePlayers.length >= 2){
          if (data.gamePlayers[0].id == getParameterByName('gp')){
            playerInfo = [data.gamePlayers[0].player, data.gamePlayers[1].player];
          }
          else{
            playerInfo = [data.gamePlayers[1].player, data.gamePlayers[0].player];
           }
          $('#playerInfo').text(playerInfo[0].email + '(you) vs ' + playerInfo[1].email);
      }else{
        playerInfo = [data.gamePlayers[0].player]
        $('#playerInfo').text(playerInfo[0].email + '(you) vs ' + "Waiting for Opponent");
      }
        data.ships.forEach(function (shipPiece) {
          shipPiece.shipLocations.forEach(function (shipLocation) {
            var hited = isHit(shipLocation,data.salvoes,playerInfo[0].id);
          if(hited > 0){
            $('#B_' + shipLocation).addClass('ship-piece-hited')
            $('#B_' + shipLocation).text(hited);}
          else
            $('#B_' + shipLocation).addClass('ship-piece');
        });
      });
      data.salvoes.forEach(function (salvo) {
        if (playerInfo[0].id === salvo.player) {
          salvo.salvoLocations.forEach(function (location) {
            $('#S_' + location).addClass('salvo');
          });
        } else {
          salvo.salvoLocations.forEach(function (location) {
            $('#_' + location).addClass('salvo');
          });
        }
      });
    })
    .fail(function (jqXHR, textStatus) {
      alert('Failed: ' + textStatus);
    });
}

function isHit(shipLocation,salvoes,playerId) {
  var hit = 0;
  salvoes.forEach(function (salvo) {
    if(salvo.player != playerId)
      salvo.salvoLocations.forEach(function (location) {
        if(shipLocation === location)
          hit = salvo.turn;
      });
  });
  return hit;
}