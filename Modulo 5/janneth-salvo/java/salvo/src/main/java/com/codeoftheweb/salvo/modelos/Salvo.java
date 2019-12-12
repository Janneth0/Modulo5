package com.codeoftheweb.salvo.modelos;


import com.codeoftheweb.salvo.modelos.GamePlayer;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Entity
public class Salvo {

  //declarar variables
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name ="native", strategy = "native")
  private long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "gamePlayer_id")
  private GamePlayer gamePlayer;

  private int turno;

  @ElementCollection
  @Column(name="salvoLocation")
  private Set<String> salvoLocations = new LinkedHashSet<>();


  //constructor

  public Salvo() {
  }

  public Salvo( GamePlayer gamePlayer, int turno, Set<String> locations) {
    this.gamePlayer = gamePlayer;
    this.turno = turno;
    this.salvoLocations = salvoLocations;
  }
//------------GET & SET --------------------------------------//
  public long getId() {
    return id;
  }
  public GamePlayer getGamePlayer() {
    return gamePlayer;
  }
  public void setGamePlayer(GamePlayer gamePlayer) {
    this.gamePlayer = gamePlayer;
  }
  public int getTurno() {
    return turno;
  }
  public void setTurno(int turno) {
    this.turno = turno;
  }
  public Set<String> getSalvoLocations() {
    return salvoLocations;
  }
  public void setSalvoLocations(Set<String> salvoLocations) {
    this.salvoLocations = salvoLocations;
  }



  public Object makeSalvoDTO() {
    Map<String,Object> dto=new LinkedHashMap<>();
    dto.put("turno",getTurno());
    dto.put("player",this.getGamePlayer().getPlayer().getId());
    dto.put("salvoLocations",getSalvoLocations());
    return dto;
  }



}
