/*
 * SuggestionMessage.java
 * Created on 5-apr-2006
 *
 * author: Jacopo Penazzi
 */
package gamedata;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Jacopo
 * 
 * 
 */
public class SuggestionMessage implements Serializable{
    
    private String senderIP;
    private ArrayList requestedCards;
    private boolean showYourCards;    
    private boolean exception;
    private Card returnCard;
    
    public SuggestionMessage(String senderIP,ArrayList requestedCards){
        this.senderIP = senderIP;
        this.requestedCards = requestedCards;
        this.showYourCards = true;
        this.exception = false;
        this.returnCard = null;
    }

    public boolean isException() {
        return exception;
    }

    public void setException(boolean exception) {
        this.exception = exception;
    }

    public ArrayList getRequestedCards() {
        return requestedCards;
    }

    public void setRequestedCards(ArrayList requestedCards) {
        this.requestedCards = requestedCards;
    }

    public Card getReturnCard() {
        return returnCard;
    }

    public void setReturnCard(Card returnCard) {
        this.returnCard = returnCard;
    }

    public String getSenderIP() {
        return senderIP;
    }

    public void setSenderIP(String senderIP) {
        this.senderIP = senderIP;
    }

    public boolean isShowYourCards() {
        return showYourCards;
    }

    public void setShowYourCards(boolean showYourCards) {
        this.showYourCards = showYourCards;
    }

    
    
    
    
}
