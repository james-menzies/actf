package utils;

public class Person implements Comparable<Person> {

    private String firstName;
    private String lastName;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return String.format( "%S, %s", lastName, firstName);
    }

    @Override
    public int compareTo(Person o) {

        int lastComp =
                lastName.compareTo(o.lastName);

        return lastComp != 0 ? lastComp : firstName.compareTo(o.firstName);
    }
}
