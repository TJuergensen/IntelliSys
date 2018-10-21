import java.util.concurrent.ThreadLocalRandom;

class Person {
    private boolean viewA = false;
    private int restResponsiveness = 0;

    Person() {
    }

    void meets(Person person) {
        final int responsiveness = 5;
        if(!this.viewA && person.getViewA()) {
            if (restResponsiveness > 0 ) {
                infectPerson();
            } else {
                this.restResponsiveness = responsiveness;
            }
        }else {
            --restResponsiveness;
        }
    }

    boolean hasViewA() {
        return this.viewA;
    }

    void indepententChange(int chance) {
        int min = 1;
        int max = 100;
        int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);

        if(randomNum <= chance) this.viewA = true;
    }

    void infectPerson() {
        this.viewA = true;
        //System.out.println("Person wird infiziert");
    }

    private boolean getViewA() {
        return this.viewA;
    }
}