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

    @Autowired
    private SalvoRepository salvoRepository;

    @RequestMapping(value = "/games/players/{gpid}/salvoes",  method = RequestMethod.POST)
    public ResponseEntity<Map>  addSalvo(@PathVariable long gpid, @RequestBody Salvo  salvo, Authentication authentication){

        if(isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error","NO esta autorizado"), HttpStatus.UNAUTHORIZED);
        }

        Player player  = playerRepository.findByUserName(authentication.getName()).orElse(null);
        GamePlayer self  = gamePlayerRepository.getOne(gpid);

        if(player ==  null){
            return new ResponseEntity<>(makeMap("error","NO esta autorizado"), HttpStatus.UNAUTHORIZED);
        }

        if(self == null){
            return new ResponseEntity<>(makeMap("error","NO esta autorizado"), HttpStatus.UNAUTHORIZED);
        }

        if(self.getPlayer().getId() !=  player.getId()){
            return new ResponseEntity<>(makeMap("error","Los players no coinciden"), HttpStatus.FORBIDDEN);
        }

        GamePlayer  opponent  = self.getGame().getGamePlayers().stream()
                .filter(gamePlayer -> gamePlayer.getId()  !=  self.getId())
                .findFirst()
                .orElse(new GamePlayer());

        if(self.getSalvoes().size() <=  opponent.getSalvoes().size()){
            salvo.setTurno(self.getSalvoes().size()  + 1);
            salvo.setGamePlayer(self);
            salvoRepository.save(salvo);
        }

        return  new ResponseEntity<>(makeMap("OK","Salvo  created"), HttpStatus.CREATED);
    }

    //si es Guest
    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }
    //LeaderBoard
    @RequestMapping("/leaderBoard")
    public List<Map<String, Object>> makeLeaderBoard() {
        return playerRepository
                .findAll()
                .stream()
                .map(player -> playerLeaderBoardDTO(player))
                .collect(Collectors.toList());
    }

    public Map<String, Object> makeGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate().getTime());
        dto.put("gamePlayers", getAllGamePlayers(game.getGamePlayers()));
        dto.put("score", game.getScores()
                .stream()
                .map(scores -> scores.makeScoreDTO())
                .collect(Collectors.toList()));
        return dto;
    }
    public List<Map<String, Object>> getAllGamePlayers(Set<GamePlayer> gamePlayers) {
        return gamePlayers
                .stream()
                .map(gamePlayer -> makeGamePlayerDTO(gamePlayer))
                .collect(Collectors.toList());
    }


    public Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("gpid", gamePlayer.getId());
        dto.put("player", gamePlayer.getPlayer().makePlayerDTO());
        return dto;
    }

    @RequestMapping("/game_view/{nn}")
    public ResponseEntity<Map<String, Object>> getGameViewByGamePlayerID(@PathVariable long nn, Authentication authentication) {

        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "algopasó"), HttpStatus.UNAUTHORIZED);
        }

        Player player = playerRepository.findByUserName(authentication.getName()).orElse(null);
        GamePlayer gamePlayer = gamePlayerRepository.findById(nn).orElse(null);

        if (player == null) {
            return new ResponseEntity<>(makeMap("error", "pasó algo"), HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayer == null) {
            return new ResponseEntity<>(makeMap("error", "pasó algo"), HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayer.getPlayer().getId() != player.getId()) {
            return new ResponseEntity<>(makeMap("error", "pasó algo"), HttpStatus.CONFLICT);
        }


        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", gamePlayer.getGame().getId());
        dto.put("created", gamePlayer.getGame().getCreationDate());
        dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers().
                stream().
                map(gamePlayer1 -> gamePlayer1.makeGamePlayerDTO()).
                collect(Collectors.toList()));
        dto.put("ships", gamePlayer.getShips()
                .stream()
                .map(ship -> ship.makeShipDTO())
                .collect(Collectors.toList()));
        dto.put("salvoEs", gamePlayer.getGame().getGamePlayers()
                .stream()
                .flatMap(gamePlayer1 -> gamePlayer1.getSalvoes()
                        .stream()
                        .map(salvo -> salvo.makeSalvoDTO()))
                .collect(Collectors.toList()));

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    private Map<String, Object> playerLeaderBoardDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", player.getId());
        dto.put("userName", player.getUserName());
        dto.put("score", getScoreList(player));
        return dto;
    }

    private Map<String, Object> getScoreList(Player player) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("total", player.getTotalscore());
        dto.put("won", player.getWins(player.getScores()));
        dto.put("tied", player.getDraws(player.getScores()));
        dto.put("lost", player.getLosses(player.getScores()));
        return dto;
    }

    public Map<String, Object> makeMap(String clave, Object value) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put(clave, value);
        return dto;
    }

    ///task 2 o 3 ultimo metodp de unidad 5
    /*@RequestMapping(path = "/game/{gameID}/players",method = RequestMethod.POST)
    public RequestEntity<Map<String,Object>>*/
    //unir un player a un juego
    /*@RequestMapping(path = "/game/{gameID}/player", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long gameID, Authentication authentication) {

        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "You can´t join a Game if You're Not Logged In!"), HttpStatus.UNAUTHORIZED);
        }
        Player player = playerRepository.findByUserName(authentication.getName()).orElse(null);
        Game gameToJoin = gameRepository.getOne(gameID);

        if (gameToJoin == null) {
            return new ResponseEntity<>(makeMap("error", "No such game"), HttpStatus.FORBIDDEN);
        }

        if (player == null) {
            return new ResponseEntity<>(makeMap("error", "No such game"), HttpStatus.FORBIDDEN);
        }

        int gamePlayerCount = gameToJoin.getGamePlayers().size();

        if (gamePlayerCount == 1) {
            GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(gameToJoin, player));
            return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(makeMap("error", "Game is full"), HttpStatus.FORBIDDEN);
        }
    }
*/
//players
   /* @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String username, @RequestParam String password) {

        if (username.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data,dato perdido", HttpStatus.FORBIDDEN);
        }
        if (playerRepository.findByUserName(username).orElse(null) != null) {
            return new ResponseEntity<>("Name already in use, nombre en uso", HttpStatus.FORBIDDEN);
        }
        playerRepository.save(new Player(username, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }*/
//ships para game
  /*  @RequestMapping(path = "/games/players/{gpid}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map> addShip(@PathVariable long gpid, @RequestBody Set<Ship> ships, Authentication authentication) {
        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "NO esta autorizado"), HttpStatus.UNAUTHORIZED);
        }
        Player player = playerRepository.findByUserName(authentication.getName()).orElse(null);
        GamePlayer gamePlayer = gamePlayerRepository.getOne(gpid);
        if (player == null) {
            return new ResponseEntity<>(makeMap("error", "NO esta autorizado"), HttpStatus.UNAUTHORIZED);
        }
        if (gamePlayer == null) {
            return new ResponseEntity<>(makeMap("error", "NO esta autorizado"), HttpStatus.UNAUTHORIZED);
        }
        if (gamePlayer.getPlayer().getId() != player.getId()) {
            return new ResponseEntity<>(makeMap("error", "Los players no coinciden"), HttpStatus.FORBIDDEN);
        }
        if (!gamePlayer.getShips().isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "NO esta autorizado ya tengo ships"), HttpStatus.UNAUTHORIZED);
        }
        ships.forEach(ship -> {
            ship.setGamePlayer(gamePlayer);
            shipRepository.save(ship);
        });
        return new ResponseEntity<>(makeMap("OK", "Ship created"), HttpStatus.CREATED);
    }
*/
    //Crear Un Juego
    /*@RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>>createGame(Authentication authentication){
        if (isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error","no player logged in"),HttpStatus.FORBIDDEN);
        }
        Game game=gameRepository.save(new Game(new Date()));
        Player player =playerRepository.findByUserName(authentication.getName()).orElse(null);
        GamePlayer gamePlayer=gamePlayerRepository.save(new GamePlayer(game, player));
        return new ResponseEntity<>(makeMap("gpId",gamePlayer.getId()),HttpStatus.CREATED);
    }*/
    //Juegos
    /*@RequestMapping("/games")
    public Map<String, Object> getGames(Authentication authentication) {
        Map<String, Object> dto = new HashMap<>();
        if (isGuest(authentication)) {
            dto.put("player", "guest");
        } else {
            Player player = playerRepository.findByUserName(authentication.getName()).get();
            dto.put("player", player.makePlayerDTO());
        }
        dto.put("games", gameRepository.findAll()
                .stream()
                .map(Game -> makeGameDTO(Game))
                .collect(Collectors.toList()));
        return dto;
    }*/
}