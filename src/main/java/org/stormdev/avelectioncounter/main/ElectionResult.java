package org.stormdev.avelectioncounter.main;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Edward on 26/02/2017.
 */
public class ElectionResult implements Cloneable {
    private String id = UUID.randomUUID().toString();
    private String winner = "";

    private List<String> log = new ArrayList<>();

    public ElectionResult(){

    }

    public String getID(){
        return this.id;
    }

    public ElectionResult(String winner){
        this.winner = winner;
    }

    public void outputResultInfo(PrintWriter pw){
        for(String l:log){
            pw.println(l);
        }
        pw.println("Winner: "+winner);
        pw.println("========================");
    }

    public void log(String str){
        log.add(str);
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    @Override
    public ElectionResult clone(){
        ElectionResult copy = new ElectionResult();
        copy.winner = winner;
        copy.log.addAll(log);
        return copy;
    }
}
