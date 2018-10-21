import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoubleToIntFunction;
import java.util.stream.DoubleStream;

public class Main {

    public static void main(String[] args) {
        final boolean whichFunction = true; //true == dependent, false == independent
        final int patientsZero = 3;
        final int maxInfectionDays = 10000;
        final int peopleCount = 50 ;

        //Genaue und noch zeitlich ok bei mir Sample Size: Independent = 10 millionen, Dependent = 1 millionen
        final long sampleSize = 1000000;

        DoubleToIntFunction func;
        if(whichFunction) {
            func = e -> dependentOpinion(patientsZero, maxInfectionDays, peopleCount);
        }else {
            func = e -> independentOpinion(maxInfectionDays, peopleCount);
        }

        long startIn = System.nanoTime();
        OptionalDouble aveDaysIn = DoubleStream.iterate(0, integer -> integer + 1)
                .limit(sampleSize)
                .parallel()
                .mapToInt(func)
                .average();
        long endIn = System.nanoTime();

        if(whichFunction) {
            System.out.println("DependentOpinion test:");
        }else {
            System.out.println("IndependentOpinion test:");
        }
        System.out.println("Durchschnittliche Tage bis alle infeziert wurden :" + aveDaysIn.getAsDouble() +
                            "\nBei " + sampleSize + " durchläufen."+
                            "\nDauer des Tests: " + ((endIn-startIn) / 1000000000) + " Sekunden" );
    }

    private static int dependentOpinion(int patientsZero, int maxInfectionDays, int peopleCount) {
        List<Person> people = generatePeople(peopleCount);
        List<Person> usedPeople = new ArrayList<>();
        Person dummy;
        for(int i = 0; i < patientsZero; i++) {
            dummy = people.remove(0);
            dummy.infectPerson();
            people.add(peopleCount-1, dummy);
        }

        Collections.shuffle(people, new Random());
        Person per1, per2;
        int infectedPeople = countInfected(people);
        //System.out.println("Am Anfang gibt es " + infectedPeople + " Personen die infiziert sind");
        int passedDays = 0;
        while ((infectedPeople < peopleCount) && (infectedPeople > 0)) {
            Collections.shuffle(people, new Random());
            while (people.size() > 0) {
                per1 = people.remove(0);
                per2 = people.remove(0);
                if(shouldTheyMeetToday()) {
                    per1.meets(per2);
                    per2.meets(per1);
                }
                usedPeople.add(per1);
                usedPeople.add(per2);
            }
            passedDays++;
            if(passedDays >= maxInfectionDays) {
                System.out.println("Max infection days reached");
                return passedDays;
            }
            people = new ArrayList<>(usedPeople);
            usedPeople.clear();
            infectedPeople = countInfected(people);
            //System.out.println("Es wurden bisher " + infectedPeople + " Personen infiziert.");
        }
        return passedDays;
    }

    private static int independentOpinion(int maxInfectionDays, int peopleCount) {
        List<Person> people = generatePeople(peopleCount);
        int infectedPeople = countInfected(people);
        //System.out.println("Am Anfang gibt es " + infectedPeople + " Personen die infiziert sind");
        int passedDays = 0;
        while((infectedPeople < peopleCount)) {
            for(Person p : people) {
                if(getsSpontaneousInfected()) p.infectPerson();
            }
            passedDays++;
            if(passedDays >= maxInfectionDays) {
                System.out.println("Max infection days reached");
                return passedDays;
            }
            infectedPeople = countInfected(people);
            //System.out.println("Es wurden bisher " + infectedPeople + " Personen infiziert.");
        }

        return passedDays;
    }

    /**
     * Generates a List of  People.
     * @param peopleCount The amount of people, that should be generated.
     * @return Returns a list with the generated people.
     */
    private static List<Person> generatePeople(int peopleCount) {
        List<Person> people = new ArrayList<>();
        Person dummy;
        for(int i = 0; i < peopleCount; i++) {
            dummy = new Person();
            people.add(dummy);
        }
        return people;
    }

    /**
     * Decides if two people should meet today, through pseudo random numbers.
     * @return Returns True if they should meet and false if not.
     */
    private static boolean shouldTheyMeetToday() {
        //8,5% scheint ein guter wert zu sein
        double encounterProbability = 0.085;
        return generateRandomProbability(encounterProbability);
    }

    private static  boolean getsSpontaneousInfected () {
        //2,23% scheint ein guter wert zu sein
        double infectionProbability = 0.0223;
        return generateRandomProbability(infectionProbability);
    }

    private static boolean generateRandomProbability(double probability) {
        double min = 0;
        double max = 1;
        double randomNum = ThreadLocalRandom.current().nextDouble(min, max);

        return randomNum <= probability;
    }

    /**
     * Counts the infected people in a given list.
     * @param people The List, in which the infected people will be counted.
     * @return Returns the number of infected people
     */
    private static int countInfected(List<Person> people) {
        int count = 0;
        for(Person p : people) {
            if(p.hasViewA())count++;
        }
        return count;
    }
}