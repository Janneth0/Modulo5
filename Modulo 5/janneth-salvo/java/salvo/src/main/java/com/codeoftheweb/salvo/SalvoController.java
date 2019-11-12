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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    /*@RequestMapping("/game_view/{nn}")
    public ResponseEntity<Map<String,Object> getGameViewByGamePlayerID(@PathVariable Long nn, Autentication autentication) >
        if(isGuest(authentication)){
            return new ResponseEntity<>(makeMap("ERRO PASO ALGO"))
    }*/


    @RequestMapping(value = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String username, @RequestParam String password) {

        if (username.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Error", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByUserName(username).orElse(null) !=  null) {
            return new ResponseEntity<>("Nombre en uso", HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(username,passwordEncoder.encode(password)));
        return new ResponseEntity<>(makeMap("OK","Player registrado"),HttpStatus.CREATED);
    }



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
        return  new ResponseEntity<>(dto,HttpStatus.OK);



    }
/*
    @RequestMapping(path = "/game/{gameID}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long gameID, Authentication authentication) {
        if (isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error", "You can't join a Game if You're Not Logged In!"), HttpStatus.UNAUTHORIZED);
        }

        Player  player  = playerRepository.findByUserName(authentication.getName()).orElse(null);
        Game gameToJoin = gameRepository.getOne(gameID);

        // assert (gameToJoin != null);

        if (gameRepository.getOne(gameID) == null) {
            return new ResponseEntity<>(makeMap("error", "No such game."), HttpStatus.FORBIDDEN);
        }

        if(player ==  null){
            return new ResponseEntity<>(makeMap("error", "No such game."), HttpStatus.FORBIDDEN);
        }

        long gamePlayersCount = gameToJoin.getGamePlayers().size();

        if (gamePlayersCount == 1) {
            GamePlayer gameplayer = gamePlayerRepository.save(new GamePlayer(gameToJoin, player));
            return new ResponseEntity<>(makeMap("gpid", gameplayer.getId()), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(makeMap("error", "Game is full!"), HttpStatus.FORBIDDEN);
        }

    }
*/

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

    ///task 2 o 3 ultimo metodp de unidad 5
    /*@RequestMapping(path = "/game/{gameID}/players",method = RequestMethod.POST)
    public RequestEntity<Map<String,Object>>*/





}
