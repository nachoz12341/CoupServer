package com.nachozgames.CoupServer;

public class Card {
    public static final int CARD_AMBASSADOR = 0;
    public static final int CARD_ASSASSIN = 1;
    public static final int CARD_CAPTAIN = 2;
    public static final int CARD_CONTESSA = 3;
    public static final int CARD_DUKE = 4;
    
    int card_type;
    boolean alive;

    public Card(int card_type){
        this.card_type=card_type;
        this.alive=true;
    }

    public void setAlive(boolean alive){
        this.alive=alive;
    }

    public boolean isAlive(){
        return this.alive;
    }
}
