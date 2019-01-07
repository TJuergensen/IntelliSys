import org.uncommons.maths.random.GaussianGenerator;

import java.util.ArrayList;
import java.util.Random;

public class Agent {

    private double stdDeviation;
    private double threshold;
    private double eventRisk;
    private int id;
    private boolean isThreatening;

    private double minSigma = 3.0;
    private double maxSigma = 4.9;
    private double minThreshold = 5.5;
    private double maxThreshold = 8.4;

    private static ArrayList<Agent> agentList = new ArrayList<>();



    public Agent(int id) {
        calculateVariables();
        this.id=id;
        agentList.add(this);

    }

    private void RUN(){
        //System.out.println("Oh deer. Run, guys! RUN!");
        isThreatening=true;
    }

    private void stayCalm(){
        //System.out.println("Meh...nothing to see here. BORING.");
        isThreatening = false;
    }

    public void eventHappens(double threatLevel)
    {
        GaussianGenerator ge = new GaussianGenerator(threatLevel, stdDeviation, new Random());
        eventRisk = ge.nextValue();

        reactOnEnvironment();
    }

    public boolean reactOnEnvironment()
    {
        if(eventRisk <= threshold)
        {
            RUN();
        } else { stayCalm();}

        return isThreatening;
    }

    private void calculateVariables()
    {
        Random rdm = new Random();
        threshold = minThreshold + (maxThreshold - minThreshold) * rdm.nextDouble();
        stdDeviation = minSigma + (maxSigma - minSigma) * rdm.nextDouble();
    }

    public double showRisk() {
        return eventRisk;
    }

    public boolean situationIsThreatening()
    {
        return isThreatening;
    }

    public int getID(){
        return this.id;
    }

}
