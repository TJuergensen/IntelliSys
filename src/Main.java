import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) {
        int patientsZero = 3;

        dependentOpinion(patientsZero);
    }

    private static void dependentOpinion(int patientsZero) {
        int peopleCount = 50 ;
        List<Person> people = generatePeople(peopleCount);
        List<Person> usedPeople = new ArrayList<>();
        Person dummy;
        for(int i = 0; i < patientsZero; i++) {
            dummy = people.remove(0);
            dummy.willBePatientZero();
            people.add(peopleCount-1, dummy);
        }

        Collections.shuffle(people, new Random());
        Person per1, per2;
        int infectedPeople = 0;
        System.out.println("Am Anfang gibt es " + countInfected(people) + " die infiziert sind");
        //TODO abbruch bedingung für den fall das es keine infizierten mehr gibt einbauen
        while (infectedPeople < peopleCount) {
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
            people = new ArrayList<>(usedPeople);
            usedPeople.clear();
            infectedPeople = countInfected(people);
            System.out.println("Es wurden bisher " + countInfected(people) + " Leute infiziert.");
        }
    }

    private static List<Person> generatePeople(int peopleCount) {
        List<Person> people = new ArrayList<>();
        Person dummy;
        for(int i = 0; i < peopleCount; i++) {
            dummy = new Person();
            people.add(dummy);
        }
        return people;
    }

    private static boolean shouldTheyMeetToday() {
        double encounterProbability = 70.0;
        double min = 0;
        double max = 100;
        double randomNum = ThreadLocalRandom.current().nextDouble(min, max);

        return randomNum <= encounterProbability;
    }

    private static int countInfected(List<Person> people) {
        int count = 0;
        for(Person p : people) {
            if(p.hasViewA())count++;
        }
        return count;
    }
}