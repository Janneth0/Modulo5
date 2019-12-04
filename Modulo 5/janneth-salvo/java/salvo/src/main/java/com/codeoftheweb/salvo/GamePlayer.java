package com.codeoftheweb.salvo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

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

    public GamePlayer(Game game, Player player) {
        this.joinDate = new Date();
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

    // funcion del game_view
    public Map<String, Object> gameViewDTO() {
        //se determina el oponente y se lo guarda en una variable auxiliar
        GamePlayer opponent = GetOpponent().orElse(null);
        Map<String, Object> dto = new LinkedHashMap<>();

        dto.put("id", this.getGame().getId());
        dto.put("creationDate", this.getGame().getCreationDate());
        dto.put("gamePlayers", this.getGame().getGamePlayers());
        dto.put("ships", this.getShips());
        dto.put("salvoes",   this.getGame().getGamePlayers()
                .stream()
                .flatMap(gp -> gp.getSalvoes()
                        .stream()
                        .sorted(Comparator.comparing(Salvo::getTurno))
                        .map(salvo -> salvo.makeSalvoDTO())
                )
                .collect(Collectors.toList())
        );
        dto.put("hits", Hitsdto(this,opponent));
        return dto;
    }
    private Map<String,Object> Hitsdto(GamePlayer self,GamePlayer opponent){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("self", getHits(self,opponent));
        dto.put("opponent", getHits(opponent,self));
        return dto;
    }




    //***************************************DETERMINAR EL OPONENTE*****************************************************
    public Optional<GamePlayer> GetOpponent(){
        return this.getGame().getGamePlayers()
                .stream()
                .filter(opponent -> this.getId() != opponent.getId())
                .findFirst();
    }

    //**********************************CALCULAR A CUANTOS BARCOS DEL OPONENTE ACERTE***********************************
    private List<Map> getHits(GamePlayer self,
                              GamePlayer opponent){
        List<Map> dto = new ArrayList<>();

        int carrierDamage = 0;
        int destroyerDamage = 0;
        int patrolboatDamage = 0;
        int submarineDamage = 0;
        int battleshipDamage = 0;
        List<String> carrierLocations = new ArrayList<>();
        List<String> destroyerLocations = new ArrayList<>();
        List<String> submarineLocations = new ArrayList<>();
        List<String> patrolboatLocations = new ArrayList<>();
        List<String> battleshipLocations = new ArrayList<>();

        for (Ship ship: self.getShips()) {
            switch (ship.getType()){
                case "carrier":
                    carrierLocations = ship.getShipLocations();
                    break ;
                case "battleship" :
                    battleshipLocations = ship.getShipLocations();
                    break;
                case "destroyer":
                    destroyerLocations = ship.getShipLocations();
                    break;
                case "submarine":
                    submarineLocations = ship.getShipLocations();
                    break;
                case "patrol_boat":
                    patrolboatLocations = ship.getShipLocations();
                    break;
            }
        }
        for (Salvo salvo : opponent.getSalvoes()) {
            Integer carrierHitsInTurn = 0;
            Integer battleshipHitsInTurn = 0;
            Integer submarineHitsInTurn = 0;
            Integer destroyerHitsInTurn = 0;
            Integer patrolboatHitsInTurn = 0;
            Integer missedShots = salvo.getSalvoLocations().size();
            Map<String, Object> hitsMapPerTurn = new LinkedHashMap<>();
            Map<String, Object> damagesPerTurn = new LinkedHashMap<>();
            List<String> salvoLocationsList = new ArrayList<>();
            List<String> hitCellsList = new ArrayList<>();
            salvoLocationsList.addAll(salvo.getSalvoLocations());
            for (String salvoShot : salvoLocationsList) {
                if (carrierLocations.contains(salvoShot)) {
                    carrierDamage++;
                    carrierHitsInTurn++;
                    hitCellsList.add(salvoShot);
                    missedShots--;
                }
                if (battleshipLocations.contains(salvoShot)) {
                    battleshipDamage++;
                    battleshipHitsInTurn++;
                    hitCellsList.add(salvoShot);
                    missedShots--;
                }
                if (submarineLocations.contains(salvoShot)) {
                    submarineDamage++;
                    submarineHitsInTurn++;
                    hitCellsList.add(salvoShot);
                    missedShots--;
                }
                if (destroyerLocations.contains(salvoShot)) {
                    destroyerDamage++;
                    destroyerHitsInTurn++;
                    hitCellsList.add(salvoShot);
                    missedShots--;
                }
                if (patrolboatLocations.contains(salvoShot)) {
                    patrolboatDamage++;
                    patrolboatHitsInTurn++;
                    hitCellsList.add(salvoShot);
                    missedShots--;
                }
            }


            damagesPerTurn.put("carrierHits", carrierHitsInTurn);
            damagesPerTurn.put("battleshipHits", battleshipHitsInTurn);
            damagesPerTurn.put("submarineHits", submarineHitsInTurn);
            damagesPerTurn.put("destroyerHits", destroyerHitsInTurn);
            damagesPerTurn.put("patrolboatHits", patrolboatHitsInTurn);
            damagesPerTurn.put("carrier", carrierDamage);
            damagesPerTurn.put("battleship", battleshipDamage);
            damagesPerTurn.put("submarine", submarineDamage);
            damagesPerTurn.put("destroyer", destroyerDamage);
            damagesPerTurn.put("patrolboat", patrolboatDamage);
            hitsMapPerTurn.put("turn", salvo.getTurno());
            hitsMapPerTurn.put("hitLocations", hitCellsList);
            hitsMapPerTurn.put("damages", damagesPerTurn);
            hitsMapPerTurn.put("missed", missedShots);
            dto.add(hitsMapPerTurn);
        }

        return dto;
    }




}


