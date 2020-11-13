/*
 * Deck.java
 * Created on 9-mar-2006
 *
 * author: Jacopo Penazzi
 */
package util;

import gamedata.Card;
import interfaces.Configuration;

import java.util.ArrayList;

/**
 * @author Jacopo
 * 
 * 
 */
public class Deck {

    private ArrayList remainingCards;
    
    
    public Deck(){
        remainingCards = Deck.getAllDeckCards();
    }
    
    
    public int getSize(){
        return remainingCards.size();
    }
    
    public ArrayList getRemainingCards(){
        return remainingCards;
    }
    
    public static ArrayList getAllDeckCards(){
        ArrayList cards = new ArrayList();
        
        // carte delle stanze
        cards.add(new Card(Configuration.BALL));
        cards.add(new Card(Configuration.HALL));
        cards.add(new Card(Configuration.STUDY));
        cards.add(new Card(Configuration.CONSERVATORY));
        cards.add(new Card(Configuration.LIBRARY));
        cards.add(new Card(Configuration.KITCHEN));
        cards.add(new Card(Configuration.BILLIARD));
        cards.add(new Card(Configuration.DINING));
        cards.add(new Card(Configuration.LOUNGE));
        // carte delle armi
        cards.add(new Card(Configuration.DAGGER));
        cards.add(new Card(Configuration.GUN));
        cards.add(new Card(Configuration.ROPE));
        cards.add(new Card(Configuration.SPANNER));
        cards.add(new Card(Configuration.CANDLESTICK));
        cards.add(new Card(Configuration.LEADPIPE));
        // carte dei personaggi
        cards.add(new Card(Configuration.CHARACTER_NAME_1));
        cards.add(new Card(Configuration.CHARACTER_NAME_2));
        cards.add(new Card(Configuration.CHARACTER_NAME_3));
        cards.add(new Card(Configuration.CHARACTER_NAME_4));
        cards.add(new Card(Configuration.CHARACTER_NAME_5));
        cards.add(new Card(Configuration.CHARACTER_NAME_6));
                
        return cards;
    }
    
    
  /*
    public static ArrayList getShuffledDeck(){
        return null;
    }
  */
    
    public Card pickRandomCard(ArrayList deck){
        int size = deck.size();
        Card c = (Card)deck.get((int)((Math.random()*size)+1));
        remainingCards.remove(c);
        return c;
    }
    
    
    public ArrayList getCharacterCards(){
        ArrayList cards = new ArrayList();
        cards.add(new Card(Configuration.CHARACTER_NAME_1));
        cards.add(new Card(Configuration.CHARACTER_NAME_2));
        cards.add(new Card(Configuration.CHARACTER_NAME_3));
        cards.add(new Card(Configuration.CHARACTER_NAME_4));
        cards.add(new Card(Configuration.CHARACTER_NAME_5));
        cards.add(new Card(Configuration.CHARACTER_NAME_6));
        return cards;
    }
    
    public ArrayList getWeaponCards(){
        ArrayList cards = new ArrayList();
        cards.add(new Card(Configuration.DAGGER));
        cards.add(new Card(Configuration.GUN));
        cards.add(new Card(Configuration.ROPE));
        cards.add(new Card(Configuration.SPANNER));
        cards.add(new Card(Configuration.CANDLESTICK));
        cards.add(new Card(Configuration.LEADPIPE));
        return cards;
    }
    
    public ArrayList getRoomCards(){
        ArrayList cards = new ArrayList();
        cards.add(new Card(Configuration.BALL));
        cards.add(new Card(Configuration.HALL));
        cards.add(new Card(Configuration.STUDY));
        cards.add(new Card(Configuration.CONSERVATORY));
        cards.add(new Card(Configuration.LIBRARY));
        cards.add(new Card(Configuration.KITCHEN));
        cards.add(new Card(Configuration.BILLIARD));
        cards.add(new Card(Configuration.DINING));
        cards.add(new Card(Configuration.LOUNGE));
        return cards;
    }
}
