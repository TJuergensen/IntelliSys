import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoubleToIntFunction;
import java.util.stream.DoubleStream;

//TODO komentare hinzufügen
public class Main {

    public static void main(String[] args) throws IOException {
        final int startPersonCountWithViewA = 3;
        final int maxDaysToChangeView = 10000;
        final int peopleCount = 50 ;

        //Genaue und noch zeitlich ok bei mir Sample Size: Independent = 10 millionen, Dependent = 1 millionen
        final long sampleSize = 100;
        DataContainer dataContainer = new DataContainer();
        DoubleToIntFunction dependentFunc,independentFunc;
        dependentFunc = e -> dependentOpinion(startPersonCountWithViewA, maxDaysToChangeView, peopleCount, dataContainer);
        independentFunc = e -> independentOpinion(maxDaysToChangeView, peopleCount, dataContainer);

        Xchart xchart = new Xchart();
        runSimulation(dependentFunc,sampleSize);
        xchart.simpleChart(dataContainer, "Dependent");
        dataContainer.clear();
        runSimulation(independentFunc,sampleSize);
        xchart.simpleChart(dataContainer, "Independent");
    }

    private static void runSimulation(DoubleToIntFunction func,long sampleSize) {
        long start = System.nanoTime();
        OptionalDouble aveDays = DoubleStream.iterate(0, integer -> integer + 1)
                .limit(sampleSize)
                .parallel()
                .mapToInt(func)
                .average();
        long end = System.nanoTime();

        System.out.println("Durchschnittliche Tage bis alle die Ansicht A haben :" + aveDays.getAsDouble() +
                "\nBei " + sampleSize + " durchläufen."+
                "\nDauer des Tests: " + ((end-start) / 1000000000) + " Sekunden" );
    }

    //TODO Independent und dependent weiter zusammen fassen und Kommentare hinzufügen.

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
            if(passedDays >= maxDaysToChangeView) {
                System.err.println("Max days reached to change view. Change probability or increase the max Day limit");
                return passedDays;
            }
            people = new ArrayList<>(usedPeople);
            usedPeople.clear();
            peopleWithViewA = countPeopleWithViewA(people);
        }
        dataContainer.addInt(passedDays);
        return passedDays;
    }

    private static int independentOpinion(int maxDaysToChangeView, int peopleCount, DataContainer dataContainer) {
        List<Person> people = generatePeople(peopleCount);
        int peopleWithViewA = countPeopleWithViewA(people);
        int passedDays = 0;
        while((peopleWithViewA < peopleCount)) {
            for(Person p : people) {
                if(getViewASpontaneous()) p.manifestViewA();
            }
            passedDays++;
            if(passedDays >= maxDaysToChangeView) {
                System.err.println("Max days reached to change view");
                return passedDays;
            }
            peopleWithViewA = countPeopleWithViewA(people);
        }
        dataContainer.addInt(passedDays);
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
        final double encounterProbability = 0.085;
        return generateRandomProbability(encounterProbability);
    }

    /**
     * Decides if a person starts to belief in view A.
     * @return Returns true if he starts to belief in view A, and false if not.
     */
    private static  boolean getViewASpontaneous () {
        //2,23% scheint ein guter wert zu sein
        final double changeViewProbability = 0.0223;
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