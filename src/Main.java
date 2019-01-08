public class Main {
    private static final int numberOfAgents = 20;
    private static final int trainingSituationCount = 2000; //how many simulations the agents should train
    private static final int realSituationCount = 1000;     //how many real situations should the agents face after training
    private static final double dangerousSituation = 7.0;
    private static final double saveSituation = 4.0;

    public static void main(String[] args){
        Agent[] agents = createAgents();
        simulate(agents);
    }

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
        System.out.println("Avg Training True-Positive rate: " + avgTrainingTruePositive / agents.length +
                        "\nAvg Training False-Positive rate: " + avgTrainingFalsePositive / agents.length +
                        "\nAvg Real True-Positive rate: " + avgTruePositive / agents.length +
                        "\nAvg Real False-Positive rate: " + avgFalsePositive / agents.length);
    }

    private static Agent[] createAgents() {
        Agent[] ret = new Agent[numberOfAgents];

        for(int i = 0; i < numberOfAgents; i++) {
            ret[i] = new Agent(i, numberOfAgents);
        }

        return ret;
    }

    static int getTrainingSituationCount() {
        return trainingSituationCount;
    }

    static int getRealSituationCount() {
        return realSituationCount;
    }
}
