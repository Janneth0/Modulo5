package com.codeoftheweb.salvo.modelos;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private Date creationDate;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<Score> scores;

    public Game() {
    }
    public Game(Date creationDate) {
        this.creationDate = creationDate;
    }
//get && set
    public long getId() {
        return id;
    }
    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }
    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }
    public Set<Score> getScores() {
        return scores;
    }
    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }


    public Map<String,Object> makeGameDTO(Game game){
        Map<String,Object> dto=new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("created",this.getCreationDate());
        dto.put("gamePlayers",this.getGamePlayersList());
        dto.put("scores",this.getScoresList());
        return dto;
    }

    public List<Object> getScoresList() {
        return this.getScores()
                .stream()
                .map(score -> score.makeScoreDTO())
                .collect(Collectors.toList());

    }
    public List<Object> getGamePlayersList(){
        return this.getGamePlayers()
                .stream()
                //.sorted(gamePlayer -> gamePlayer.getId())
                .sorted(Comparator.comparing(GamePlayer::getId))
                .map(GamePlayer -> GamePlayer.makeGamePlayerDTO())
                .collect(Collectors.toList());
    }
    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayers.add(gamePlayer);
    }








    /*
    public void addGamePlayer(GamePlayer gamePlayer) {
        //gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }
    public void setDate(Date creationDate){
        this.date = creationDate;
    }

    */
}
