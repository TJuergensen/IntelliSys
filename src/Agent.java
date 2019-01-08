import org.uncommons.maths.random.GaussianGenerator;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class Agent {
    private int agentId;
    private double threshold;
    private double standardDeviation;
    private double randomNumberZ;
    private AgentMemory[] agentsRating; //The memory of the performance of the other agents
    private double trainingTruePositive = 0.0; //counts the true positive events
    private double trainingFalsePositive = 0.0; //counts the false positive events
    private double truePositive = 0.0;  //count the true positive events in the real situations
    private double falsePositive = 0.0; //count the false positive events in the real situations
    private final double quorumTP = 0.3; //The quorum that needs to be reached by the true positive rate
    private final double quorumFP = 0.2; //The quorum that needs to be reached by the false positive rate
    private final int minAmountOfAgentIds = 7; //The min amount of agents that are used for evaluating the situation

    Agent(int agentId, int agentCount) {
        this.agentId = agentId;
        this.agentsRating = new AgentMemory[agentCount];
        Arrays.fill(agentsRating, new AgentMemory());
        standardDeviation = ThreadLocalRandom.current().nextDouble(3, 5);
        threshold = ThreadLocalRandom.current().nextDouble(5.5, 8.5);
    }

    void newSituation(double situation) {
        GaussianGenerator gen = new GaussianGenerator(situation, standardDeviation, new Random());
        randomNumberZ = gen.nextValue();
    }

    void observeWorld(Agent[] agents, boolean isDangerous) {
            if(isDangerous()) {
                if(isDangerous) {
                    trainingTruePositive++;
                }else {
                    trainingFalsePositive++;
                }
            }
            for(Agent a : agents) {
                if(a.isDangerous()) {
                    if(this != a) {
                        if (isDangerous) {
                            agentsRating[a.getAgentId()].addTruePositive();
                        } else {
                            agentsRating[a.getAgentId()].addFalsePositive();
                        }
                    }
                }
            }
    }

    void isDangerousBasedOnWorld(Agent[] agents, boolean isDangerous) { //for real situazions.
        /*
        //Ursprüngliche idee.
        double danger = 0.0;
        double save = 0.0;
        for(Agent a : agents) {
            if(this != a) {
                if (a.isDangerous()) {
                    danger += agentsRating[a.getAgentId()].getTrainingTruePositiveRate() - agentsRating[a.getAgentId()].getTrainingFalsePositiveRate();
                } else {
                    save += agentsRating[a.getAgentId()].getTrainingTruePositiveRate() - agentsRating[a.getAgentId()].getTrainingFalsePositiveRate();
                }
            }
        }
        if(danger > save) {
            if(isDangerous) {
                truePositive++;
            }else {
                falsePositive++;
            }
        }*/

        List<Integer> randomAgentsIDs = new ArrayList<>();
        //generate a list with all agents ids
        for(int i = 0; i < agents.length; i++){
            randomAgentsIDs.add(i);
        }
        //randomize the list
        Collections.shuffle(randomAgentsIDs);

        boolean beliefItIsDangerous = false;
        boolean quorumReached = false;
        int agentsThatBelieveItsDangerousCount = 0;
        int agentsThatBelieveItsSaveCount = 0;
        double amountOfAgentsIds = minAmountOfAgentIds; //The number of agents that are needed for making a decision
        double nrOfAgentsAlreadyWatched = minAmountOfAgentIds; //The number of agents that are already used for decision making
        Agent dummyAgent;

        //as long as there are agents left and the quorum isn't reached
        while(randomAgentsIDs.size() != 0 && !quorumReached) {
            for (int i = 0; i < amountOfAgentsIds; i++) {
                dummyAgent = agents[randomAgentsIDs.remove(0)];
                if (dummyAgent.isDangerous()) {
                    agentsThatBelieveItsDangerousCount++;
                } else {
                    agentsThatBelieveItsSaveCount++;
                }
            }
            if(amountOfAgentsIds == 1) {
                nrOfAgentsAlreadyWatched++;
            }
            //Check if the quorum is reached for the true positives
            //else check if the quorum is reached for the false positives
            if ((agentsThatBelieveItsDangerousCount / nrOfAgentsAlreadyWatched) > quorumTP) {
                beliefItIsDangerous = true;
                quorumReached = true;
            }else if ((agentsThatBelieveItsSaveCount / nrOfAgentsAlreadyWatched) > quorumFP) {
                beliefItIsDangerous = false;
                quorumReached = true;
            }
            amountOfAgentsIds = 1; //After the first comparision with n agent add another
        }

        //If the quorum isent reached, the agent decides based of its own view.
        if(!quorumReached) {
            beliefItIsDangerous = isDangerous();
        }

        //Count the true and false positive decisions
        if(beliefItIsDangerous) {
            if(isDangerous) {
                truePositive++;
            }else {
                falsePositive++;
            }
        }
    }



    private boolean isDangerous() {
        return  randomNumberZ > threshold;
    }

    double getTrainingTruePositiveRate() {
        return trainingTruePositive / Main.getTrainingSituationCount();
    }

    double getTrainingFalsePositiveRate() {
        return trainingFalsePositive / (Main.getTrainingSituationCount());
    }

    double getTruePositiveRate() {
       return truePositive / Main.getRealSituationCount();
    }

    double getFalsePositiveRate() {
        return falsePositive / Main.getRealSituationCount();
    }

    private int getAgentId() {
        return  this.agentId;
    }
}
