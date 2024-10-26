package Common;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String cpf;
    private String name;

    //constructor
    public User(String cpf, String name) {
        this.cpf = cpf;
        this.name = name;
    }

    //getters
    public String getCpf() { return cpf; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return "User{" +
                "cpf='" + cpf + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
