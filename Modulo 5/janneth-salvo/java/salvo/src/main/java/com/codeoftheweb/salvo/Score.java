package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
public class Score {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name ="native", strategy = "native")
  private long id;

  private float scores;

  private Date finishDate;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="player_id")
  private Player player;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="game_id")
  private Game game;


  public Score() {
  }

//constructor
  public Score( float scores, Date finishDate, Player player, Game game){
    this.scores=scores;
    this.finishDate=finishDate;
    this.player=player;
    this.game=game;
  }

  public Map<String,Object> makeScoreDTO() {
    Map<String, Object> dto= new LinkedHashMap<>();
    dto.put("player",this.getPlayer().getId());
    dto.put("score",this.getScore());
    dto.put("finishDate",this.getFinishDate()
            .getTime());
    return dto;
  }

//get && set
  public long getId() {
    return id;
  }

  public float getScore() {
    return scores;
  }

  public void setScore(float scores) {
    this.scores = scores;
  }

  public Date getFinishDate() {
    return finishDate;
  }

  public void setFinishDate(Date finishDate) {
    this.finishDate = finishDate;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

}
