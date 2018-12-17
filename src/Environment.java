import java.util.ArrayList;

public class Environment {

    private ArrayList<Agent> agents;
    private int harmlessTrue;
    private int harmlessFalse;
    private int dangerousTrue;
    private int dangerousFalse;

    public Environment(int agentNumber)
    {
        agents = new ArrayList<>();
        harmlessFalse = harmlessTrue = dangerousTrue = dangerousFalse = 0;

        createAgents(agentNumber);
    }

    private void createAgents(int number)
    {
        for(int i = 0; i<number;i++)
            agents.add(new Agent(i));
    }

    public void createPotentialThreat(int threatLevel)
    {
        for(Agent a : agents){
            a.eventHappens((double) threatLevel);
            countReaction(a, threatLevel);
        }
    }

    private void countReaction(Agent agent, int threatLevel)
    {
        switch (threatLevel) {
            case 4: //harmless
                if(agent.situationIsThreatening()){
                    dangerousFalse++;
                } else {harmlessTrue++;}
                break;
            case 7: //dangerous
                if(agent.situationIsThreatening()){
                    dangerousTrue++;
                } else { harmlessFalse++;}
                break;
        }
    }

    public void showResults(int agentCount, int harmlessSituations, int dangerousSituations)
    {
        System.out.println("Total numbers of reactions per Agent:\n" +
                "Harmless  Situations correclty seen as harmless: " + harmlessTrue +"\n"+
                "Harmless  Situations falsely seen as dangerous: " + dangerousFalse + "\n"+
                ((double)harmlessTrue/agentCount)/((double)harmlessSituations)*100+" % of Harmless Situations were correctly seen as harmless\n\n"+
                "Dangerous Situations correctly seen as dangerous: " + dangerousTrue +"\n"+
                "Dangerous Situations falsely seen as harmless: " + harmlessFalse +"\n"+
                ((double)dangerousTrue/agentCount)/((double)dangerousSituations)*100+" % of Dangerous Situations were correctly seen as dangerous");
    }
}
