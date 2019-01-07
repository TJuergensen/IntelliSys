public class Main {
    static int numberOfAgents = 20;
    static int trainingSituationCount = 100;
    static int realSituationCount = 10;
    private static final double dangerousSituation = 7.0;
    private static final double saveSituation = 4.0;
    public static void main(String[] args){
        Agent[] agents = createAgents();
        simulate(agents);
    }

    private static void simulate(Agent[] agents) {

        //Train with dangerous situations
        for(int trainingSimulationNR = 0; trainingSimulationNR < trainingSituationCount; trainingSimulationNR++) {
            //new save situation
            for (Agent a : agents) {
                a.newSituation(dangerousSituation);
            }
            //observe other agents
            for (Agent a : agents) {
                a.observeWorld(agents, true);
            }
        }
        //train with save situations
        for(int trainingSimulationNR = 0; trainingSimulationNR < trainingSituationCount; trainingSimulationNR++) {
            //new save situation
            for (Agent a : agents) {
                a.newSituation(saveSituation);
            }
            //observe other agents
            for (Agent a : agents) {
                a.observeWorld(agents, false);
            }
        }

        //Real
        for(int realSimulationNR = 0; realSimulationNR < realSituationCount; realSimulationNR++) {
            //new Situation
            for (Agent a : agents) {
                a.newSituation(dangerousSituation);
            }
            //Make decision based on all agents
            for (Agent a : agents) {
                a.isDangerousBasedOnWorld(agents, true);
            }
        }
        for(int realSimulationNR = 0; realSimulationNR < realSituationCount; realSimulationNR++) {
            //new Situation
            for (Agent a : agents) {
                a.newSituation(saveSituation);
            }
            //Make decision based on all agents
            for (Agent a : agents) {
                a.isDangerousBasedOnWorld(agents, false);
            }
        }

        double avgTruePositiv = 0;
        double avgFalsePositiv = 0;
        double avgTrainingTruePositiv = 0;
        double avgTrainingFalsePositiv = 0;
        double avgLol = 0;
        for(Agent a : agents) {
            avgTruePositiv += a.getTruePositivRate();
            System.out.println(avgTruePositiv);
            avgFalsePositiv += a.getFalsePositivRate();
            avgTrainingTruePositiv += a.getTrainingTruePositivRate();
            avgTrainingFalsePositiv += a.getTrainingFalsePositivRate();
            avgLol += a.lol;
            System.out.println( "Agend" + a.getAgentId() +
                                "\n     Training True-PositivRate: " + a.getTrainingTruePositivRate() +
                                "\n     Training False-PositivRate: " + a.getTrainingFalsePositivRate()+
                                "\n     True-PositivRate: " + a.getTruePositivRate() +
                                "\n     False-PositivRate: " + a.getFalsePositivRate()+
                                "\n     lol: " + a.lol);
        }
        System.out.println("Avg Training True-Positiv: " + avgTrainingTruePositiv / agents.length +
                        "\nAvg Training False-Positiv: " + avgTrainingFalsePositiv / agents.length +
                        "\nAvg True-Positiv: " + avgTruePositiv / agents.length +
                        "\nAvg False-Positiv: " + avgFalsePositiv / agents.length +
                        "\nAvg lol: " + avgLol / agents.length);
    }

    private static Agent[] createAgents() {
        Agent[] ret = new Agent[numberOfAgents];

        for(int i = 0; i < numberOfAgents; i++) {
            ret[i] = new Agent(i, numberOfAgents);
        }

        return ret;
    }
}
