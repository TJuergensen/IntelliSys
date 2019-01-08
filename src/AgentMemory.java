public class AgentMemory {
    private double trainingTruePositive = 0.0;
    private double trainingFalsePositive = 0.0;

    public AgentMemory() {

    }

    public void addTruePositive() {
        trainingTruePositive++;
    }
    public void addFalsePositive() {
        trainingFalsePositive++;
    }

    public double getTrainingTruePositiveRate() {
       return trainingTruePositive / (Main.getRealSituationCount());
    }

    public double getTrainingFalsePositiveRate() {
        return trainingFalsePositive / (Main.getTrainingSituationCount() );
    }
}
