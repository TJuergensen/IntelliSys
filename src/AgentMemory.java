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
        //TODO berechnung nicht jedes mal neu machen
       return trainingTruePositiv / (Main.trainingSituationCount);
        //return trainingTruePositiv / (trainingTruePositiv + trainingFalsePositiv);
    }

    public double getTrainingFalsePositivRate() {
        return trainingFalsePositiv / (Main.trainingSituationCount );
        //return trainingFalsePositiv / (trainingTruePositiv + trainingFalsePositiv);
    }
}
