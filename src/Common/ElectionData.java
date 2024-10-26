package Common;

import java.io.Serializable;
import java.util.*;

public class ElectionData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String question;
    private List<String> options;

    //constructor
    public ElectionData(String question, List<String> options) {
        this.question = question;
        this.options = options;
    }

    //getters
    public String getQuestion() { return question; }
    public List<String> getOptions() { return options; }

    @Override
    public String toString() {
        return "ElectionData{" +
                "question='" + question + '\'' +
                ", options=" + options +
                '}';
    }
}
