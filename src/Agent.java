import org.uncommons.maths.random.GaussianGenerator;
import java.util.Arrays;
import java.util.Random;
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
    double lol = 0.0;
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
            if(isDangerous()) {
                if(isDangerous) {
                    trainingTruePositiv++;
                }else {
                    trainingFalsePositiv++;
                }
            }
            for(Agent a : agents) {
                if(a.isDangerous()) {
                    if(isDangerous) {
                        agentsRating[a.getAgentId()].addTruePositiv();
                    }else {
                        agentsRating[a.getAgentId()].addFalsePositiv();
                    }
                }
            }
    }

    public void isDangerousBasedOnWorld(Agent[] agents, boolean isDangerous) {
        double danger = 0.0;
        double save = 0.0;
        for(Agent a : agents) {
            //TODO was tun?
            if(a.isDangerous()) {
                danger += agentsRating[a.getAgentId()].getTrainingTruePositivRate() - agentsRating[a.getAgentId()].getTrainingFalsePositivRate();
            }else {
                save += agentsRating[a.getAgentId()].getTrainingTruePositivRate() - agentsRating[a.getAgentId()].getTrainingFalsePositivRate();
            }
        }
        if(danger > save) {
            if(isDangerous) {
                truePositiv++;
            }else {
                falsePositiv++;
            }
        }else {
            lol++;
        }
    }



    public boolean isDangerous() {
        return  randomNumberZ > threshold;
    }

    public double getTrainingTruePositivRate() {
        return trainingTruePositiv / (trainingTruePositiv + trainingFalsePositiv);
    }

    public double getTrainingFalsePositivRate() {
        return trainingFalsePositiv / (trainingTruePositiv + trainingFalsePositiv);
    }

    public double getTruePositivRate() {
        if(truePositiv == 0) return 0;
        return truePositiv / (truePositiv + falsePositiv);
    }

    public double getFalsePositivRate() {
        if(falsePositiv == 0) return 0;
        return falsePositiv / (truePositiv + falsePositiv);
    }

    public int getAgentId() {
        return  this.agentId;
    }
}
