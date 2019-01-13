import org.uncommons.maths.random.GaussianGenerator;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * An agent for "Entscheidungsfindung in einer Gruppe"
 */
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
    private int minAmountOfAgentIds; //The min amount of agents that are used for evaluating the situation
    private final double minAmountOfAgentIdsInPercent = 35; //The min amount of agents that are used for evaluating the situation in percent
    /**
     * Initialise the agent.
     * Creates the standardDeviation and the threshold as required in the task-position
     * @param agentId The ID that will be associated with the new agent
     * @param agentCount The amount of all agents that will be created
     */
    Agent(int agentId, int agentCount) {
        minAmountOfAgentIds = (int)((agentCount / 100.0) * minAmountOfAgentIdsInPercent);
        this.agentId = agentId;
            this.agentsRating = new AgentMemory[agentCount];
        Arrays.fill(agentsRating, new AgentMemory());
        standardDeviation = ThreadLocalRandom.current().nextDouble(3, 5);
        threshold = ThreadLocalRandom.current().nextDouble(5.5, 8.5);
    }

    /**
     * Simulates a new Situation for the agent.
     * @param situation The Value of the Situation, should be 4 or 7
     */
    void newSituation(double situation) {
        GaussianGenerator gen = new GaussianGenerator(situation, standardDeviation, new Random());
        randomNumberZ = gen.nextValue();
    }

    /**
     * When called, the agent observes the world around him.
     * He checks on how the other agents perform in the world.
     * SHOULD ONLY BE USED WHILE TRAINING THE AGENTS
     * @param agents The Agents he will observe
     * @param isDangerous The Information on the dangerousness of the situation
     */
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

    /**
     * When called the agent decides if the world is dangerous based on the world around him.
     * For the Details of the implementation, see the report of this project.
     * SHOULD ONLY BE CALLED AFTER THE TRAINING OF THE AGENTS IN FINISHED
     * @param agents The agents that are in the world around the agent
     * @param isDangerous The Information on the dangerousness of the situation
     */
    void isDangerousBasedOnWorld(Agent[] agents, boolean isDangerous) { //for real situazions.
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


        /*
        The first Implementation we used. For more Information see the report of this project
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
    }

    /**
     * The agent decides based on his sensors if the world is dangerous
     * @return Returns the decision if the world is dangerous or not
     */
    private boolean isDangerous() {
        return  randomNumberZ > threshold;
    }

    /**
     * Returns the True Positive rate from the training of this agent
     * @return Returns the True Positive rate from the training of this agent
     */
    double getTrainingTruePositiveRate() {
        return trainingTruePositive / Main.getTrainingSituationCount();
    }

    /**
     * Returns the False Positive rate from the training of this agent
     * @return Returns the False Positive rate from the training of this agent
     */
    double getTrainingFalsePositiveRate() {
        return trainingFalsePositive / (Main.getTrainingSituationCount());
    }

    /**
     * Returns the True Positive rate from the real world of this agent
     * @return Returns the True Positive rate from the real world of this agent
     */
    double getTruePositiveRate() {
       return truePositive / Main.getRealSituationCount();
    }

    /**
     * Returns the False Positive rate from the real world of this agent
     * @return Returns the False Positive rate from the real world of this agent
     */
    double getFalsePositiveRate() {
        return falsePositive / Main.getRealSituationCount();
    }

    /**
     * Returns the agent ID
     * @return Returns the agent ID
     */
    private int getAgentId() {
        return  this.agentId;
    }
}
