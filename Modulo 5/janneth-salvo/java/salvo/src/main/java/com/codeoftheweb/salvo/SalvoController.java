package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.net.PasswordAuthentication;
import java.util.*;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ShipRepository shipRepository;

    @RequestMapping("/games")
    public Map<String,Object> getGames(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();

        if (authentication != null) {
            Player player = playerRepository.findByUserName(authentication.getName()).get();
            dto.put("player", player.makePlayerDTO());
        } else {
            dto.put("player", "Guest");
        }
        dto.put("games", gameRepository.findAll()
                .stream()
                .map(game -> makeGameDTO(game))
                .collect(Collectors.toList())
        );
        return dto;
    }

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Object> createGame(Authentication authentication) {

        if ( isGuest(authentication)) {
            return new ResponseEntity<>("Unathorized", HttpStatus.UNAUTHORIZED);
        }

        Player player = playerRepository.findByUserName(authentication.getName()).orElse(null);

        if (player ==  null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        Game game = gameRepository.save(new Game());
        GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(game,player));

        return new ResponseEntity<>(makeMap("gpid",gamePlayer.getId()),HttpStatus.CREATED);
    }

    @RequestMapping(path = "/games/{id}/players", method = RequestMethod.POST)
    public ResponseEntity<Object> joinGame(@PathVariable Long id, Authentication authentication) {
        System.out.println("Entro al metodo :"+id);
        if ( isGuest(authentication)) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        Player  player  =   playerRepository.findByUserName(authentication.getName()).orElse(null);
        Game game = gameRepository.findById(id).orElse(null);

        if (player ==  null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        if (game ==  null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        int gamePlayersCount = game.getGamePlayers().size();

        if (gamePlayersCount == 1) {
            GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(game, player));
            return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(makeMap("error", "Game is full!"), HttpStatus.FORBIDDEN);
        }
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    public Map<String,Object> makeGameDTO(Game game){
        Map<String,Object> dto=new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("created",game.getCreationDate());
        dto.put("gamePlayers",getAllGamePlayers(game.getGamePlayers()));
        return dto;
    }
    public List<Map<String,Object>> getAllGamePlayers(Set<GamePlayer> gamePlayers){
        return gamePlayers
                .stream()
                .map(gamePlayer -> makeGamePlayerDTO(gamePlayer))
                .collect(Collectors.toList());
    }
    public Map<String,Object> makeGamePlayerDTO(GamePlayer gamePlayer){
        Map<String,Object> dto=new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getId());
        dto.put("player", gamePlayer.getPlayer().makePlayerDTO());


        return dto;
    }


    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String username, @RequestParam String password) {

        if (username.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (playerRepository.findByUserName(username).orElse(null) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        playerRepository.save(new Player(username, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    /*@RequestMapping(path = "/games/players/{gpid}/ships",  method = RequestMethod.POST)
    public ResponseEntity<Map>  addShip(@PathVariable long gpid, @RequestBody Set<Ship> ships, Authentication authentication){
        if(isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error","NO esta autorizado"), HttpStatus.UNAUTHORIZED);
        }
        Player  player  = playerRepository.findByUserName(authentication.getName()).orElse(null);
        GamePlayer  gamePlayer  = gamePlayerRepository.getOne(gpid);
        if(player ==  null){
            return new ResponseEntity<>(makeMap("error","NO esta autorizado"), HttpStatus.UNAUTHORIZED);
        }
        if(gamePlayer == null){
            return new ResponseEntity<>(makeMap("error","NO esta autorizado"), HttpStatus.UNAUTHORIZED);
        }
        if(gamePlayer.getPlayer().getId() !=  player.getId()){
            return new ResponseEntity<>(makeMap("error","Los players no coinciden"), HttpStatus.FORBIDDEN);
        }
        if(!gamePlayer.getShips().isEmpty()){
            return new ResponseEntity<>(makeMap("error","NO esta autorizado ya tengo ships"), HttpStatus.UNAUTHORIZED);
        }
        ships.forEach(ship -> {
            ship.setGamePlayer(gamePlayer);
            shipRepository.save(ship);
        });
        return new ResponseEntity<>(makeMap("OK","Ship created"), HttpStatus.CREATED);
    }
*/


    @RequestMapping("/game_view/{nn}")
    public ResponseEntity<Map<String, Object>> getGameViewByGamePlayerID(@PathVariable Long nn, Authentication  authentication) {

        if(isGuest(authentication)){
            return new  ResponseEntity<>(makeMap("error","Paso algo"),HttpStatus.UNAUTHORIZED);
        }

        Player  player  = playerRepository.findByUserName(authentication.getName()).orElse(null);
        GamePlayer gamePlayer = gamePlayerRepository.findById(nn).orElse(null);

        if(player ==  null){
            return new  ResponseEntity<>(makeMap("error","Paso algo"),HttpStatus.UNAUTHORIZED);
        }

        if(gamePlayer ==  null ){
            return new  ResponseEntity<>(makeMap("error","Paso algo"),HttpStatus.UNAUTHORIZED);
        }

        if(gamePlayer.getPlayer().getId() !=  player.getId()){
            return new  ResponseEntity<>(makeMap("error","Paso algo"),HttpStatus.CONFLICT);
        }



        Map<String, Object> dto=new LinkedHashMap<>();
        dto.put("id", gamePlayer.getGame().getId());
        dto.put("created", gamePlayer.getGame().getCreationDate());
        dto.put("gamePlayers",gamePlayer.getGame().getGamePlayers()
                .stream()
                .map(gamePlayer1 -> gamePlayer1.makeGamePlayerDTO())
                .collect(Collectors.toList()));
        dto.put("ships", gamePlayer.getShips()
                .stream()
                .map(ship -> ship.makeShipDTO())
                .collect(Collectors.toList()));
        dto.put("salvoes", gamePlayer.getSalvoes()
                .stream()
                .map(salvo -> salvo.makeSalvoDTO())
                .collect(Collectors.toList()));

        return new ResponseEntity<>(dto, HttpStatus.ACCEPTED);
    }


    @RequestMapping("/leaderBoard")
    public List<Map<String,Object>> makeLeaderBoard(){
        return playerRepository
                .findAll()
                .stream()
                .map(player -> playerLeaderBoardDTO(player))
                .collect(Collectors.toList());
    }
    private Map<String, Object> playerLeaderBoardDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", player.getId());
        dto.put("userName", player.getUserName());
        dto.put("score", getScoreList(player));
        return dto;
    }
    private Map<String, Object> getScoreList (Player player) {
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("total", player.getTotalscore());
        dto.put("won", player.getWins(player.getScores()));
        dto.put("tied", player.getDraws(player.getScores()));
        dto.put("lost", player.getLosses(player.getScores()));
        return dto;
    }

    public Map<String,Object> makeMap(String clave, Object value){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put(clave,value);
        return dto;
    }



}