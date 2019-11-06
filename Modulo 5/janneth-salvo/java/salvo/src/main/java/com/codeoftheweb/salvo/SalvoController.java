package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {
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

    public Map<String,Object> makeGameDTO(Game game){
        Map<String,Object> dto=new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("created",game.getCreationDate().getTime());
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
      //dto.put("scores3", gamePlayer.getPlayer().getScores());

        return dto;
    }

    /*@RequestMapping("/game_view/{nn}")
    public ResponseEntity<Map<String,Object> getGameViewByGamePlayerID(@PathVariable Long nn, Autentication autentication) >
        if(isGuest(authentication)){
            return new ResponseEntity<>(makeMap("ERRO PASO ALGO"))
    }*/





    @RequestMapping("/game_view/{nn}")
    public Map<String, Object> getGameViewByGamePlayerId(@PathVariable Long nn){
        GamePlayer gamePlayer=gamePlayerRepository.findById(nn).get();

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
        return dto;

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

    ///task 2 o 3 ultimo metodp de unidad 5
    /*@RequestMapping(path = "/game/{gameID}/players",method = RequestMethod.POST)
    public RequestEntity<Map<String,Object>>*/





}
