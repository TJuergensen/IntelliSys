import org.uncommons.maths.random.GaussianGenerator;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Agent {
    //Agent information about himself
    private int agentId;
    private double threshold;   //Schwellwert
    private double standardDeviation;
    private double randomNumberZ;
    private AgentMemory[] agentsRating;
    private double trainingTruePositiv = 0.0;
    private double trainingFalsePositiv = 0.0;
    private double truePositiv = 0.0;
    private double falsePositiv = 0.0;
    double justeverypossiblenegative = 0.0;
    List<Integer> quorumTestTP = new ArrayList<>();
    List<Integer> quorumTestFP = new ArrayList<>();
    double quorumTP = 0.3;
    double quorumFP = 0.2;
    int minAgents = 7;
    //Agent information about the World

    public Agent(int agentId, int agentCount) {
        this.agentId = agentId;
        this.agentsRating = new AgentMemory[agentCount];
        Arrays.fill(agentsRating, new AgentMemory());
        standardDeviation = ThreadLocalRandom.current().nextDouble(3, 5);
        threshold = ThreadLocalRandom.current().nextDouble(5.5, 8.5);
    }

    public void newSituation(double situation) {
        GaussianGenerator gen = new GaussianGenerator(situation, standardDeviation, new Random());
        randomNumberZ = gen.nextValue();
    }

    public void observeWorld(Agent[] agents, boolean isDangerous) {
        int tpCount = 0;
        int fpCount = 0;
            if(isDangerous()) {
                if(isDangerous) {
                    trainingTruePositiv++;
                }else {
                    trainingFalsePositiv++;
                }
            }
            for(Agent a : agents) {
                if(a.isDangerous()) {
                    if(this != a) {
                        if (isDangerous) {
                            tpCount++;
                            agentsRating[a.getAgentId()].addTruePositiv();
                        } else {
                            fpCount++;
                            agentsRating[a.getAgentId()].addFalsePositiv();
                        }
                    }
                }
            }
            if(isDangerous) {
                quorumTestTP.add(tpCount);
            }else {
                quorumTestFP.add(fpCount);
            }
    }

    public void isDangerousBasedOnWorld(Agent[] agents, boolean isDangerous) { //for real situazions.
        /*double danger = 0.0;
        double save = 0.0;
        for(Agent a : agents) {
            if(this != a) {
                if (a.isDangerous()) {
                    danger += (agentsRating[a.getAgentId()].getTrainingTruePositivRate() - agentsRating[a.getAgentId()].getTrainingFalsePositivRate()) * (agentsRating[a.getAgentId()].getTrainingTruePositivRate() - agentsRating[a.getAgentId()].getTrainingFalsePositivRate());
                } else {
                    save += (agentsRating[a.getAgentId()].getTrainingTruePositivRate() - agentsRating[a.getAgentId()].getTrainingFalsePositivRate()) * (agentsRating[a.getAgentId()].getTrainingTruePositivRate() - agentsRating[a.getAgentId()].getTrainingFalsePositivRate());
                }
            }
        }
        if(danger > save) {
            if(isDangerous) {
                truePositiv++;
            }else {
                falsePositiv++;
            }
        }else {
            justeverypossiblenegative++;
        }*/

        List<Integer> randomAgents = new ArrayList<>();
        for(int i = 0; i < agents.length; i++){
            randomAgents.add(i);
        }
        boolean beliefISDangerous = false;
        Collections.shuffle(randomAgents);
        int beliefIsDangerousCount = 0;
        int beliefISSaveCount = 0;
        double minAgentsDummy = minAgents;
        double minAgentsDummy2 = minAgents;
        Agent a;
        while(randomAgents.size() != 0) {
            for (int i = 0; i < minAgentsDummy; i++) {
                a = agents[randomAgents.remove(0)];
                if (a.isDangerous()) {
                    beliefIsDangerousCount++;
                } else {
                    beliefISSaveCount++;
                }
            }
            if(minAgentsDummy == 1) {
                minAgentsDummy2++;
            }
            if ((beliefIsDangerousCount / minAgentsDummy2) > quorumTP) {
                beliefISDangerous = true;
                break;
            }
            if ((beliefISSaveCount / minAgentsDummy2) > quorumFP) {
                beliefISDangerous = false;
                break;
            }
            minAgentsDummy = 1;
        }
        //System.out.println("tp:" + beliefIsDangerousCount / minAgentsDummy2);
        //System.out.println("fp:" + beliefISSaveCount / minAgentsDummy2);
        if(randomAgents.size() == 0) {
            justeverypossiblenegative += 2000;
            beliefISDangerous = isDangerous();
        }

        if(beliefISDangerous) {
            if(isDangerous) {
                truePositiv++;
            }else {
                falsePositiv++;
            }
        }else {
            justeverypossiblenegative++;
        }
    }



    public boolean isDangerous() {
        return  randomNumberZ > threshold;
    }

    public double getTrainingTruePositivRate() {
        return trainingTruePositiv / (Main.trainingSituationCount );
       //return trainingTruePositiv / (trainingTruePositiv + trainingFalsePositiv);
    }

    public double getTrainingFalsePositivRate() {
        return trainingFalsePositiv / (Main.trainingSituationCount);
        //return trainingFalsePositiv / (trainingTruePositiv + trainingFalsePositiv);
    }

    public double getTruePositivRate() {
        //if(truePositiv == 0) return 0;
       return truePositiv / (Main.realSituationCount );
        //return truePositiv / (truePositiv + falsePositiv);
    }

    public double getFalsePositivRate() {
       // if(falsePositiv == 0) return 0;
        return falsePositiv / (Main.realSituationCount );
        //return falsePositiv / (truePositiv + falsePositiv);
    }

    public int getAgentId() {
        return  this.agentId;
    }
}
