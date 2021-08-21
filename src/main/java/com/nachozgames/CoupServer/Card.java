package com.nachozgames.CoupServer;

public class Card {
    public static final int CARD_AMBASSADOR = 0;
    public static final int CARD_ASSASSIN = 1;
    public static final int CARD_CAPTAIN = 2;
    public static final int CARD_CONTESSA = 3;
    public static final int CARD_DUKE = 4;
    
    private int cardType;
    private boolean alive;

    public Card(int cardType){
        this.cardType=cardType;
        this.alive=true;
    }

    public int getCardType(){
        return this.cardType;
    }

    public void setAlive(boolean alive){
        this.alive=alive;
    }

    public boolean isAlive(){
        return this.alive;
    }
}
