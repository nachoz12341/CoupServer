package com.nachozgames.CoupServer;

import java.util.*;

public class CoupGame {
    public static final int STATE_WAIT_FOR_GAME         = 0;
    public static final int STATE_YOUR_TURN             = 1;
    public static final int STATE_OTHER_TURN            = 2;
    public static final int STATE_CLAIM_REQUEST         = 3;
    public static final int STATE_CLAIM_WAIT            = 4;
    public static final int STATE_CLAIM_BLOCK_REQUEST   = 5;
    public static final int STATE_CLAIM_BLOCK_WAIT      = 6;
    public static final int STATE_COUNTER_CLAIM_REQUEST = 7;
    public static final int STATE_COUNTER_CLAIM_WAIT    = 8;
    public static final int STATE_KILL_ROLE             = 9;
    public static final int STATE_KILL_ROLE_WAIT        = 10;

    Vector<Player> players;
    Vector<Card> deck;

    Boolean started;
    
    public CoupGame(){
        players = new Vector<Player>();
        deck = new Vector<Card>();
        started=false;
    }

    public void addPlayer(Player player){
        players.add(player);
        player.setState(STATE_WAIT_FOR_GAME);
    }

    public boolean removePlayer(String uuid){
        for (int i=0;i<players.size();i++)
            if(players.elementAt(i).getUUID().equals(uuid))  
            {
                players.removeElementAt(i);
                return true;
            }

        return false;
    }

    public Player getPlayer(String uuid){
        for (int i=0;i<players.size();i++)
            if(players.elementAt(i).getUUID().equals(uuid))  
                return players.elementAt(i);

        return null;
    }

    public boolean gameStarted(){
        return started;
    }

    public void gameStart(){
        createDeck();
        Collections.shuffle(deck);

        for (int i=0;i<players.size();i++)
        {
            Player p = players.elementAt(i);

            //First pass out two cards to each player
            for(int j=0;j<2;j++)
            {
                p.addCard(deck.firstElement());
                deck.remove(0);
            }

            //Next give 2 coins
            p.updateCoins(2);

            //Set state to other players turn
            p.setState(STATE_OTHER_TURN);
        }

        //Set state to your turn for only the first player
        players.elementAt(0).setState(STATE_YOUR_TURN);

        started=true;
    }

    private void createDeck(){
        //Add four of each card to the deck
        for(int i=0;i<4;i++){
            deck.add(new Card(Card.CARD_AMBASSADOR));
            deck.add(new Card(Card.CARD_ASSASSIN));
            deck.add(new Card(Card.CARD_CAPTAIN));
            deck.add(new Card(Card.CARD_CONTESSA));
            deck.add(new Card(Card.CARD_DUKE));
        }
    }
}
