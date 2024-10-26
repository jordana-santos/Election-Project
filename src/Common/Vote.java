package Common;

import java.io.Serializable;

public class Vote implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userCpf;
    private String chosenOption;

    //constructor
    public Vote(String userCpf, String chosenOption) {
        this.userCpf = userCpf;
        this.chosenOption = chosenOption;
    }

    //getters
    public String getUserCpf() { return userCpf; }
    public String getChosenOption() { return chosenOption; }

    @Override
    public String toString() {
        return "Vote{" +
                "userCpf='" + userCpf + '\'' +
                ", chosenOption='" + chosenOption + '\'' +
                '}';
    }
}
