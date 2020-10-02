package co.domi.semana6.model;

public class User {
    String usuario;
    String password;
    String description;


    public User(String usuario, String password, String description) {
        this.usuario = usuario;
        this.password = password;
        this.description = description;
    }

    //Necesario para Gson
    public User() {
    }

    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
