package com.codeoftheweb.salvo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name ="native", strategy = "native")
    private long id;
    private Date joinDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;
    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Ship> ships;
    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Salvo> salvoes;


    public GamePlayer() {
    }

    public GamePlayer(Date joinDate, Game game, Player player) {
        this.joinDate = joinDate;
        this.game = game;
        this.player = player;
    }
    public long getId() {
        return id;
    }
    public Date getJoinDate() {
        return joinDate;
    }
    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
    @JsonIgnore
    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }
    @JsonIgnore
    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    @JsonIgnore
    public Set<Ship> getShips() {
        return ships;
    }
    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }
    @JsonIgnore
    public Set<Salvo> getSalvoes() { return salvoes;  }
    public void setSalvoes(Set<Salvo> salvoes) { this.salvoes = salvoes;  }

    public Object makeGamePlayerDTO() {
        Map<String,Object> dto=new LinkedHashMap<>();
        dto.put("id",this.getId());
        dto.put("player",this.getPlayer().makePlayerDTO());
        return dto;

    }


}


