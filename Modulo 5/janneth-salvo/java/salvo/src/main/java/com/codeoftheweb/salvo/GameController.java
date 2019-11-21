package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @RequestMapping("/games")
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
                .map(Game -> Game.makeGameDTO(Game))
                .collect(Collectors.toList()));
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


    private boolean isGuest(Authentication authentication){
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}