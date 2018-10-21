import java.util.concurrent.ThreadLocalRandom;

class Person {
    private boolean viewA = false;
    private int restResponsiveness = 0;

    Person() {
    }

    boolean meets(Person person) {
        final int responsiveness = 5;
        if(person.getViewA()) {
            if (restResponsiveness > 0) {
                this.viewA = true;
            } else {
                this.restResponsiveness = responsiveness;
            }
        }else {
            --restResponsiveness;
        }
        return this.viewA;
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

    void willBePatientZero() {
        this.viewA = true;
    }

    private boolean getViewA() {
        return this.viewA;
    }
}