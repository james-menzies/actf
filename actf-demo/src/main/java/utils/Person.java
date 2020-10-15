package utils;

public class Person implements Comparable<Person> {

    private String firstName;
    private String lastName;
    private String country;
    private String phone;
    private String email;


    public Person(String firstName, String lastName, String country, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.phone = phone;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCountry() {
        return country;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
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
