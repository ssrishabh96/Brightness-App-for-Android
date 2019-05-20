package androbright.brightness.rishabh.androbright.model;

/**
 * Created by RISHABH on 6/12/2017.
 */

public class User {

    private String firstname, secondName;

    public User(String firstname, String secondName) {
        this.firstname = firstname;
        this.secondName = secondName;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }
}
