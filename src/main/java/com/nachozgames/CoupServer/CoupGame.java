package com.nachozgames.CoupServer;

import java.util.*;

import ch.qos.logback.core.joran.conditional.ElseAction;

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

    public static final int ACTION_INCOME           = 0;
    public static final int ACTION_FOREIGN_AID      = 1;
    public static final int ACTION_COUP             = 2;
    public static final int ACTION_AMBASSADOR       = 3;
    public static final int ACTION_ASSASSIN         = 4;
    public static final int ACTION_CAPTAIN          = 5;
    public static final int ACTION_DUKE             = 6;
    public static final int ACTION_BLOCK_CONTESSA   = 7;
    public static final int ACTION_BLOCK_CAPTAIN    = 8;
    public static final int ACTION_BLOCK_AMBASSADOR = 9;
    public static final int ACTION_BLOCK_DUKE       = 10;
    public static final int ACTION_RESPONSE_KILL    = 11;

    Vector<Player> players;
    Vector<Card> deck;

    String activePlayerUUID;

    Boolean started;
    
    public CoupGame(){
        players = new Vector<Player>();
        deck = new Vector<Card>();
        started=false;
        activePlayerUUID="";
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
        activePlayerUUID=players.elementAt(0).getUUID();

        started=true;
    }

    public void gameUpdate(Player p, Map<String, String> updateMap){
        int gameAction = Integer.parseInt(updateMap.get("action"));
        
        switch(gameAction){
            case ACTION_INCOME:
                p.updateCoins(1);
                nextTurn();
                System.out.println("Player "+p.getName()+" collected 1 coin from income");
            break;

            case ACTION_COUP:
                if(p.getCoins()>=7)
                {
                    p.setState(STATE_KILL_ROLE_WAIT);
                    p.updateCoins(-7);

                    String targetId=updateMap.get("target");
                    Player targetPlayer=getPlayer(targetId);

                    targetPlayer.setState(STATE_KILL_ROLE);
                    System.out.println("Player "+p.getName()+" couped "+targetPlayer.getName()+" for 7 coins");
                }
                else
                    System.out.println("Player "+p.getName()+" does not have enough coins to coup");
            break;

            case ACTION_RESPONSE_KILL:
                int targetCard = Integer.parseInt(updateMap.get("card"));
                Vector<Card> cards = p.getCards();
                cards.elementAt(targetCard).setAlive(false);

                nextTurn();
                System.out.println("Player "+p.getName()+" chose a card to kill");
            break;
        }
    }

    private void nextTurn(){
        boolean nextPlayer=false;
        boolean setActive=false;

        //Iterate through list set all players state to other turn
        for (int i=0;i<players.size();i++)
        {
            Player p = players.elementAt(i);
            p.setState(STATE_OTHER_TURN);

            //Change active player
            if(nextPlayer && !setActive)
            {
                p.setState(STATE_YOUR_TURN);
                activePlayerUUID=p.getUUID();
                setActive=true;
            }

            //When we find the current player set flag to change active player
            if(p.getUUID()==activePlayerUUID)
                nextPlayer=true;
        }

        //If we still haven't set active then the next player has to be the first player
        if(!setActive){
            players.elementAt(0).setState(STATE_YOUR_TURN);
            activePlayerUUID=players.elementAt(0).getUUID();
        }
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
