package com.nachozgames.CoupServer;

import java.util.*; 

public class Player {
    int coins;
    int state;
    String uuid;
    String name;
    Vector<Card> cards; 

    public Player(String uuid){
        this.coins = 0;
        this.uuid = uuid;
        this.name = "";
        this.state = 0;
        cards = new Vector<Card>();
    }

    public void setName(String name){
        this.name = name;
    }
     
    public String getName(){
        return this.name;
    }

    public String getUUID(){
        return this.uuid;
    }

    public void setState(int state){
        this.state = state;
    }

    public int getState(){
        return this.state;
    }

    public int getCoins(){
        return this.coins;
    }

    public void updateCoins(int newCoins){
        this.coins+=newCoins;
    }

    public void addCard(Card card){
        this.cards.add(card);
    }

    public Vector<Card> getCards(){
        return cards;
    }

    public void removeCard(int i){
        this.cards.removeElementAt(i);
    }

    public boolean isAlive(){
        for (int i=0;i<cards.size();i++)
            if(cards.elementAt(i).isAlive())  
                return true;
        
        return false;
    }
}
