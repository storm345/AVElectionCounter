package org.stormdev.avelectioncounter.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Edward on 26/02/2017.
 */
public class Election {
    private List<Ballot> votes = new ArrayList<Ballot>();

    public Election(List<Ballot> votes){
        this.votes = votes;
    }

    public List<ElectionResult> getElectionResult(){
        List<ElectionResult> resultList = new ArrayList<>();
        calculateElectionResult(resultList, new ElectionResult(), votes, 1);
        return resultList;
    }

    public void calculateElectionResult(List<ElectionResult> resultList, ElectionResult currentResult, List<Ballot> ballots, int round){
        if(ballots.size() < 1){ //If there are no ballots, we have no result
            return;
        }

        //map of candidate and how many votes they have in this 'round'
        Map<String, Integer> votes = new HashMap<String, Integer>();
        //Generate the map of each candidate and how many votes they received
        for(Ballot ballot:ballots){
            if(ballot.hasCurrentChoice()){ //if they have a choice in this 'round'
                String candidate = ballot.getCurrentChoice();
                Integer voteCount = votes.get(candidate);
                int votesForCandidate = voteCount == null ? 0 : voteCount;
                votesForCandidate ++; //Add 1 to this candidates votes
                votes.put(candidate, votesForCandidate);
            }
        }

        if(votes.size() < 2){ //1 or 0 candidates left so we have a winner!
            if(votes.size() < 1){ //No candidates left so no winner
                return;
            }

            //The winner is the candidate still remaining
            String winner = votes.keySet().toArray(new String[]{})[0];
            currentResult.setWinner(winner);
            resultList.add(currentResult);
            return;
        }

        int leastVotes = Integer.MAX_VALUE;
        List<String> candidatesWithLeast = new ArrayList<String>();
        for(Map.Entry<String, Integer> candidateVotes:new HashMap<String, Integer>(votes).entrySet()){
            int votesForThisCandidate = candidateVotes.getValue();
            if(votesForThisCandidate == leastVotes){ //if candidate has the worse votes
                candidatesWithLeast.add(candidateVotes.getKey()); //Then add candidate to the list of the worst for this round
            }
            else if(votesForThisCandidate < leastVotes){ //If candidate has less votes than is the current 'worst'
                leastVotes = candidateVotes.getValue(); //Update the number of votes that is the least
                candidatesWithLeast.clear();
                candidatesWithLeast.add(candidateVotes.getKey()); //Add candidate to list of worst for the round
            }
        }

        //Candidates with least now is the candidates with the least votes
        if(candidatesWithLeast.size() < 1){
            throw new RuntimeException("Error occured, this shouldn't be possible!");
        }

        for(String badCandidate:candidatesWithLeast){
            ElectionResult toCarry = currentResult;
            if(candidatesWithLeast.size() != 1){ //if we are breaking a tie
                toCarry = toCarry.clone();
                toCarry.log("Tie at round "+round+" was overcome by eliminating "+badCandidate);
            }

            List<Ballot> ballotsForNextRound = new ArrayList<>();
            for(Ballot ballot:ballots){ //For each ballot
                ballot = ballot.clone();
                //Move onto the next fav. choice if they are currently supporting the eliminated candidate
                while(ballot.hasCurrentChoice() && ballot.getCurrentChoice().equals(badCandidate)){
                    ballot.goToNextChoice();
                }
                ballotsForNextRound.add(ballot);
            }

            calculateElectionResult(resultList, toCarry, ballotsForNextRound, round++);
        }
    }
}
