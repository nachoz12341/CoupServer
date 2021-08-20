package com.nachozgames.CoupServer;

import java.util.*;

public class CoupGame {
    Vector<Player> players;

    public CoupGame(){
        players = new Vector<Player>();
    }

    public void addPlayer(Player player){
        players.add(player);
    }
}
