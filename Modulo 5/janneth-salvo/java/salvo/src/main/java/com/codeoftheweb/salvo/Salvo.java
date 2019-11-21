package com.codeoftheweb.salvo;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashMap;
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
  @Column(name = "location")
  private Set<String> locations;

  //constructor

  public Salvo() {
  }

  public Salvo( GamePlayer gamePlayer, int turno, Set<String> locations) {
    this.gamePlayer = gamePlayer;
    this.turno = turno;
    this.locations = locations;
  }
  public Object makeSalvoDTO() {
    Map<String,Object> dto=new LinkedHashMap<>();
    dto.put("turno",getTurno());
    dto.put("player",this.getGamePlayer().getPlayer().getId());
    dto.put("salvoLocations",getLocations());
    return dto;
  }
  //get and set

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
  public Set<String> getLocations() {
    return locations;
  }
  public void setLocations(Set<String> locations) {
    this.locations = locations;
  }
}
