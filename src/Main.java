import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoubleToIntFunction;
import java.util.stream.DoubleStream;


/**
 * Implementation of the given task https://lernraum.th-luebeck.de/mod/assign/view.php?id=95408
 */
public class Main {

    //Variables that change the result
    private final static int startPersonCountWithViewA = 3;
    private final static int maxDaysToChangeView = 1000;
    private final static int peopleCount = 50 ;
    private final static int sampleSize = 10000;
    //8,5% scheint ein guter wert zu sein
    private final static double encounterProbability = 0.085;
    //2,23% scheint ein guter wert zu sein
    private final static double changeViewProbability = 0.0223;

    /**
     * Start of the simulation
     * @param args Params, that aren't in use right now.
     * @throws IOException Throws IOException, while saving a chart as SVG
     */
    public static void main(String[] args){

        DataContainer dataContainer = new DataContainer(maxDaysToChangeView);
        DoubleToIntFunction dependentFunc,independentFunc;
        dependentFunc = e -> dependentOpinion(startPersonCountWithViewA, maxDaysToChangeView, peopleCount, dataContainer);
        independentFunc = e -> independentOpinion(maxDaysToChangeView, peopleCount, dataContainer);

        Xchart xchart = new Xchart();
        //Run and analyse dependent simulation
        runSimulation(dependentFunc,sampleSize, "Dependent");
        xchart.simpleChart(dataContainer, "Dependent", sampleSize, peopleCount);
        dataContainer.clear();
        //Run and analyse independent simulation
        runSimulation(independentFunc,sampleSize, "Independent");
        xchart.simpleChart(dataContainer, "Independent", sampleSize, peopleCount);
    }

    /**
     * runs the simulation. Uses Optional Doubles to enable multithreading. Calculates average of each simulation
     * @param func function to be used; can be dependend or independend
     * @param sampleSize number of samples used
     */
    private static void runSimulation(DoubleToIntFunction func,long sampleSize, String name) {
        long start = System.nanoTime();
        OptionalDouble aveDays = DoubleStream.iterate(0, integer -> integer + 1)
                .limit(sampleSize)
                .parallel()
                .mapToInt(func)
                .average();
        long end = System.nanoTime();

        System.out.println(name + "\nDurchschnittliche Tage bis alle die Ansicht A haben :" + aveDays.getAsDouble() +
                "\nBei " + sampleSize + " durchläufen."+
                "\nDauer des Tests: " + ((end-start) / 1000000000) + " Sekunden" );
    }

    /**
     * Simulates the dependent view change.
     * @param startPersonCountWithViewA The number of People with view A on day 0
     * @param maxDaysToChangeView Maximum number of days to run simulation
     * @param peopleCount total ammount of participating people
     * @param dataContainer Data structure holding calculated information
     * @return Number of People with view A on last day (after simulation ends)
     */
    private static int dependentOpinion(int startPersonCountWithViewA, int maxDaysToChangeView, int peopleCount,
                                        DataContainer dataContainer) {
        List<Person> people = generatePeople(peopleCount);
        List<Person> usedPeople = new ArrayList<>();
        Person dummy;
        for(int i = 0; i < startPersonCountWithViewA; i++) {
            dummy = people.remove(0);
            dummy.manifestViewA();
            people.add(peopleCount-1, dummy);
        }

        Collections.shuffle(people, new Random());
        Person per1, per2;
        int peopleWithViewA = countPeopleWithViewA(people);
        int passedDays = 0;

        //As long as not everyone has view A and at least one person has view A.
        while ((peopleWithViewA < peopleCount) && (peopleWithViewA > 0)) {
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
            //Check if it takes to long to change view
            if(passedDays >= maxDaysToChangeView) {
                System.err.println("Max days reached to change view. Change probability or increase the max Day limit");
                return passedDays;
            }
            people = new ArrayList<>(usedPeople);
            usedPeople.clear();
            peopleWithViewA = countPeopleWithViewA(people);
            dataContainer.addViewCountOnDay(passedDays-1, peopleWithViewA);
        }
        /*
         Fill the rest of the array, with the peopleWithViewA.
         Is needed for the chart creation
        */
        for(int a = passedDays; a < maxDaysToChangeView;a++) {
            dataContainer.addViewCountOnDay(a, peopleWithViewA);
        }
        return passedDays;
    }


    /**
     * Simulates the independent view change.
     * @param maxDaysToChangeView  The number of People with view A on day 0
     * @param peopleCount total ammount of participating people
     * @param dataContainer Data structure holding calculated information
     * @return Number of People with view A on last day (after simulation ends)
     */
    private static int independentOpinion(int maxDaysToChangeView, int peopleCount, DataContainer dataContainer) {
        List<Person> people = generatePeople(peopleCount);
        int peopleWithViewA = countPeopleWithViewA(people);
        int passedDays = 0;

        //As long as not everyone has view A
        while((peopleWithViewA < peopleCount)) {
            //Try to get view A
            for(Person p : people) {
                if(getViewASpontaneous()) p.manifestViewA();
            }
            passedDays++;
            //Check if it takes to long to change view
            if(passedDays >= maxDaysToChangeView) {
                System.err.println( "Max days reached to change view\n"+
                                    "Please increment maxDaysToChangeView");
                return passedDays;
            }
            peopleWithViewA = countPeopleWithViewA(people);
            dataContainer.addViewCountOnDay(passedDays-1,peopleWithViewA);
        }

        /*
         Fill the rest of the array, with the peopleWithViewA.
         Is needed for the chart creation
        */
        for(int i = passedDays; i < maxDaysToChangeView; i++) {
            dataContainer.addViewCountOnDay(i, peopleWithViewA);
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
        return generateRandomProbability(encounterProbability);
    }

    /**
     * Decides if a person starts to belief in view A.
     * @return Returns true if he starts to belief in view A, and false if not.
     */
    private static  boolean getViewASpontaneous () {
        return generateRandomProbability(changeViewProbability);
    }

    /**
     * Is used to generate Random Probabilities.
     * @param probability The chance that an event occurs. Has a range from 0 to 1.
     * @return Returns true if the event will happen, false if not.
     */
    private static boolean generateRandomProbability(double probability) {
        double min = 0;
        double max = 1;
        double randomNum = ThreadLocalRandom.current().nextDouble(min, max);

        return randomNum <= probability;
    }

    /**
     * Counts the people with view A, in a given list.
     * @param people The List, in which the people with view A will be counted.
     * @return Returns the number of people, that belief in view A.
     */
    private static int countPeopleWithViewA(List<Person> people) {
        return (int) people.stream()
                            .filter(person -> person.hasViewA())
                            .count();
    }
}