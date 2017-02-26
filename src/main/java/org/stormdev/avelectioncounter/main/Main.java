package org.stormdev.avelectioncounter.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by Edward on 26/02/2017.
 */
public class Main {
    public static void main(String[] args){
        String filename;
        if(args.length > 0){
            filename = args[0];
        }
        else {
            filename = "input.csv";
        }

        File output = new File("output.txt");
        if(!output.exists()){
            try {
                output.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PrintWriter log;

        try {
            log = new PrintWriter(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        List<String> inputLines = new ArrayList<String>();

        try {
            inputLines.addAll(Files.readAllLines(Paths.get(filename)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Ballot> ballots = new ArrayList<>();

        //Start at the second line since the first line is column titles
        for(int i=1;i<inputLines.size();i++){
            String line = inputLines.get(i);
            String[] row = line.split(Pattern.quote(",")); //Input is from a CSV so each cell is split by a comma
            //First 2 cells in the row are timestamp and email
            List<String> ballotChoices = new ArrayList<>();
            for(int j=2;j<row.length;j++){
                ballotChoices.add(row[j]);
            }
            ballots.add(new Ballot(ballotChoices));
        }

        log.println("Loaded "+ballots.size()+" ballots!");

        log.println("========================");
        for(int i=0;i<ballots.size();i++){
            Ballot ballot = ballots.get(i);
            log.println("Ballot "+(i+1)+":");
            ballot.printBallotChoices(log);
            log.println("========================");
        }

        log.println("Calculating AV election result, if there is a tie between least popular candidates will calculate result of both cases:");

        Election election = new Election(ballots);

        List<ElectionResult> results = election.getElectionResult();

        if(results.size() < 1){
            log.println("No election result!");
        }
        else if(results.size() == 1){
            results.get(0).outputResultInfo(log);
        }
        else {
            log.println(results.size()+" results calculated becuase of different possible round-tie-breakings!");
            Map<String, Integer> resultWins = new HashMap<String, Integer>();
            for(ElectionResult result:results){
                String candidate = result.getWinner();
                Integer winsO = resultWins.get(candidate);
                int wins = winsO == null ? 0 : winsO;
                wins++;
                resultWins.put(candidate, wins);
            }

            for(Map.Entry<String, Integer> candidateScore:resultWins.entrySet()){
                String candidate = candidateScore.getKey();
                double wins = candidateScore.getValue();
                double percentWon = (wins*100) / ((double)results.size());
                double percentWonRounded = Math.round(percentWon*100)/100.0d;
                log.println(candidate+" won "+percentWonRounded+"% of the results");
            }
        }

        log.flush();

    }
}
