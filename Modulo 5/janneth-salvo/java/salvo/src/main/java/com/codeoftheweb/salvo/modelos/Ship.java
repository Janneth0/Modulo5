package com.codeoftheweb.salvo.modelos;

import com.codeoftheweb.salvo.modelos.GamePlayer;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class Ship {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private long id;

  private String type;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "gamePlayer_id")
  private GamePlayer gamePlayer;
  @ElementCollection
  @Column(name="shipLocation")
  private List<String> shipLocations = new ArrayList<>();


  public Ship() {
  }
  public Ship(String type, GamePlayer gamePlayer, List<String> shipLocations) {
    this.type = type;
    this.gamePlayer = gamePlayer;
    this.shipLocations = shipLocations;
  }
  //--------------GET && SET-------------------------------------//
  public long getId() {
    return id;
  }
  public String getType() {
    return type;
  }
  public GamePlayer getGamePlayer() {
    return gamePlayer;
  }
  public void setType(String type) {
    this.type = type;
  }
  public void setGamePlayer(GamePlayer gamePlayer) {
    this.gamePlayer = gamePlayer;
  }
  public List<String> getShipLocations() {
    return shipLocations;
  }
  public void setShipLocations(List<String> shipLocations) {
    this.shipLocations = shipLocations;
  }


  public Object makeShipDTO() {
    Map<String,Object> dto=new LinkedHashMap<String, Object>();
    dto.put("type",this.getType());
    dto.put("shipLocations",this.getShipLocations());
    return dto;
  }

}