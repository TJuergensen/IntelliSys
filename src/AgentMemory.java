/**
 * The Memory of the performance from the other agents.
 * Was used for finding the quorums and the first implementation of the situation based on the world.
 * !!!!
 * THIS IS NOT USED IN THE CURRENT VERSION OF THE IMPLEMENTATION and ONLY HER TO SHOW THE FIRST IMPLANTATION
 * !!!!
 */
public class AgentMemory {
    private double trainingTruePositive = 0.0;
    private double trainingFalsePositive = 0.0;

    /**
     * Initialise the agent memory
     */
    public AgentMemory() {

    }

    /**
     * Remember that the agent had a true positive in training
     */
    public void addTruePositive() {
        trainingTruePositive++;
    }

    /**
     * Remember that the agent had a false positive in training
     */
    public void addFalsePositive() {
        trainingFalsePositive++;
    }

    /**
     * Returns the True Positive rate from this agent training
     * @return Returns the True Positive rate from this agent training
     */
    public double getTrainingTruePositiveRate() {
       return trainingTruePositive / (Main.getRealSituationCount());
    }

    /**
     * Returns the False Positive rate from this agent training
     * @return Returns the False Positive rate from this agent training
     */
    public double getTrainingFalsePositiveRate() {
        return trainingFalsePositive / (Main.getTrainingSituationCount() );
    }
}
