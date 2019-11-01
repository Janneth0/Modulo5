var misCabeceras = new Headers();

var miInit = { method: 'GET',
               headers: misCabeceras,
               mode: 'cors',
               cache: 'default' };

fetch('/api/games',miInit)
.then(function(response) {
    return  response.json();

})
.then(function(data) {
    console.log(data)
   var htmlOL = document.getElementById("games");
  data.map(element => {
      var item = document.createElement("li");
      item.appendChild(document.createTextNode(element.id +"    "+ new Date(element.created).toLocaleString()  +" "+   element.gamePlayers[0].player.email +" VS "+    element.gamePlayers[1].player.email));
     document.body.insertBefore(item, htmlOL);
  });
});