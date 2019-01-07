public class Main {

    public static void main(String[] args) {

        int agentCount = 20;
        int harmlessSituations = 100000;
        int dangerousSituations = 100000;


        Environment env = new Environment(agentCount);

        for(int i=0; i<harmlessSituations; i++)
            env.createPotentialThreat(4);

        //for(int i=0; i<dangerousSituations; i++)
        //    env.createPotentialThreat(7);

        env.showResults(agentCount, harmlessSituations, 0);

    }

}
