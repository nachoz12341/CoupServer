package com.nachozgames.CoupServer;

import java.util.*; 

public class Player {
    int coins;
    String uuid;
    String name;
    Vector<Card> cards; 

    public Player(String uuid){
        this.coins = 2;
        this.uuid = uuid;
        this.name = "";
        cards = new Vector<Card>();
    }

    public void setName(String name){
        this.name = name;
    }
     
    public String getName(){
        return this.name;
    }

    public void UpdateCoins(int newCoins){
        this.coins=newCoins;
    }

    public void AddCard(Card card){
        this.cards.add(card);
    }

    public Vector<Card> GetCards(){
        return cards;
    }

    public void RemoveCard(int i){
        this.cards.removeElementAt(i);
    }

    public boolean isAlive(){
        for (int i=0;i<cards.size();i++)
            if(cards.elementAt(i).isAlive())  
                return true;
        
        return false;
    }
}
