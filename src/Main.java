/**
 * Implementation of "Entscheidungsfindung in einer Gruppe"
 */
public class Main {
    private static final int numberOfAgents = 20;
    private static final int trainingSituationCount = 2000; //how many simulations the agents should train
    private static final int realSituationCount = 1000;     //how many real situations should the agents face after training
    private static final double dangerousSituation = 7.0;
    private static final double saveSituation = 4.0;

    /**
     * Starts the Program
     * @param args Not used!
     */
    public static void main(String[] args){
        Agent[] agents = createAgents();
        simulate(agents);
        prettyPrint(agents);
    }

    /**
     * Simulates all training situations first. After that the real situations will be simulated.
     * @param agents The agents the simulation will performed on
     */
    private static void simulate(Agent[] agents) {

        //Train with dangerous situations
        for(int finishedTrainingSimulations = 0; finishedTrainingSimulations < trainingSituationCount; finishedTrainingSimulations++) {
            //give avery agent the new situation
            for (Agent a : agents) {
                a.newSituation(dangerousSituation);
            }
            //tell each agent to observe the world
            for (Agent a : agents) {
                a.observeWorld(agents, true);
            }
        }
        //train with save situations
        for(int finishedTrainingSimulations = 0; finishedTrainingSimulations < trainingSituationCount; finishedTrainingSimulations++) {
            //give avery agent the new situation
            for (Agent a : agents) {
                a.newSituation(saveSituation);
            }
            //tell each agent to observe the world
            for (Agent a : agents) {
                a.observeWorld(agents, false);
            }
        }

        //After training test the agents on the real situations
        for(int finishedRealSimulations = 0; finishedRealSimulations < realSituationCount; finishedRealSimulations++) {
            //new Situation
            for (Agent a : agents) {
                a.newSituation(dangerousSituation);
            }
            //Make decision based on all agents
            for (Agent a : agents) {
                a.isDangerousBasedOnWorld(agents, true);
            }
        }
        for(int finishedRealSimulations = 0; finishedRealSimulations < realSituationCount; finishedRealSimulations++) {
            //new Situation
            for (Agent a : agents) {
                a.newSituation(saveSituation);
            }
            //Make decision based on all agents
            for (Agent a : agents) {
                a.isDangerousBasedOnWorld(agents, false);
            }
        }
    }

    /**
     * Prints the Information, that is gathered by the simulation onto the console
     * @param agents The Agents that hold the information from the simulation
     */
    private static void prettyPrint(Agent[] agents) {
        double avgTruePositive = 0;
        double avgFalsePositive = 0;
        double avgTrainingTruePositive = 0;
        double avgTrainingFalsePositive = 0;
        for(Agent a : agents) {
            avgTruePositive += a.getTruePositiveRate();
            avgFalsePositive += a.getFalsePositiveRate();
            avgTrainingTruePositive += a.getTrainingTruePositiveRate();
            avgTrainingFalsePositive += a.getTrainingFalsePositiveRate();
        }
        System.out.println(
                "The avg Rates for all agents while there ignored the other agents:" +
                "\nTrue-Positive: " + avgTrainingTruePositive / agents.length +
                "\nFalse-Positive: " + avgTrainingFalsePositive / agents.length +
                "\n\nThe avg Rates for all agents when they take the other agents take into account:" +
                "\nTrue-Positive: " + avgTruePositive / agents.length +
                "\nFalse-Positive: " + avgFalsePositive / agents.length);
    }

    /**
     * Creates all agents for the simulation
     * @return Returns an array with all agents
     */
    private static Agent[] createAgents() {
        Agent[] ret = new Agent[numberOfAgents];

        for(int i = 0; i < numberOfAgents; i++) {
            ret[i] = new Agent(i, numberOfAgents);
        }

        return ret;
    }

    /**
     * Returns the number of training situations that should be simulated
     * @return Returns the number of training situations that should be simulated
     */
    static int getTrainingSituationCount() {
        return trainingSituationCount;
    }

    /**
     * Returns the number of real situations that should be simulated
     * @return Returns the number of real situations that should be simulated
     */
    static int getRealSituationCount() {
        return realSituationCount;
    }
}
