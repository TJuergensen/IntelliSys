public class AgentMemory {
    private double trainingTruePositiv = 0.0;
    private double trainingFalsePositiv = 0.0;

    public AgentMemory() {

    }

    public void addTruePositiv() {
        trainingTruePositiv++;
    }
    public void addFalsePositiv() {
        trainingFalsePositiv++;
    }

    public double getTrainingTruePositivRate() {
        return trainingTruePositiv / (trainingTruePositiv + trainingFalsePositiv);
    }

    public double getTrainingFalsePositivRate() {
        return trainingFalsePositiv / (trainingTruePositiv + trainingFalsePositiv);
    }
}
