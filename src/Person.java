/**
 * The class Person represents a person and his viewpoint to belief in A.
 */
class Person {
    private boolean viewA = false;
    private int restResponsiveness = 0;

    /**
     * Constructor for a new Person
     */
    Person() {
    }

    /**
     * The calling person meets the person in the parameter.
     * If the param person beliefs in view A, than than the calling person starts to belief in view A as well, as long
     * as he met another person with view A in the last 5 Days.
     * @param person The Person the calling person is meeting.
     */
    void meets(Person person) {
        final int responsiveness = 5;
        if(!this.viewA && person.hasViewA()) {
            if (restResponsiveness > 0 ) {
                manifestViewA();
            } else {
                this.restResponsiveness = responsiveness;
            }
        }else {
            --restResponsiveness;
        }
    }

    /**
     * Gets the Viewpoint from the person. Used as a Getter
     * @return Returns true if the person beliefs in point A and vise versa
     */
    boolean hasViewA() {
        return this.viewA;
    }

    /**
     * Manifests the view A in the person. Used as a setter
     */
    void manifestViewA() {
        this.viewA = true;
    }
}