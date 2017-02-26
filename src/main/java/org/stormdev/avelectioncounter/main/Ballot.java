package org.stormdev.avelectioncounter.main;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 26/02/2017.
 */
public class Ballot implements Cloneable {
    private List<String> orderedChoices = new ArrayList<String>();

    private int currentChoice = 0;

    public Ballot(List<String> orderedChoices){
        this.orderedChoices = orderedChoices;
    }

    public void goToNextChoice(){
        this.currentChoice++;
    }

    public String getCurrentChoice(){
        if(currentChoice >= 0 && currentChoice < orderedChoices.size()){
            return orderedChoices.get(currentChoice);
        }
        return null;
    }

    public boolean hasCurrentChoice(){
        return getCurrentChoice() != null;
    }

    public void printBallotChoices(PrintWriter pw){
        StringBuilder res = new StringBuilder();
        for(String choice:orderedChoices){
            if(res.length() > 0){
                res.append(", ");
            }
            res.append(choice);
        }
        pw.println("Choices: "+res.toString());
    }

    @Override
    public Ballot clone(){
        Ballot copy = new Ballot(orderedChoices);
        copy.currentChoice = currentChoice;
        return copy;
    }
}
