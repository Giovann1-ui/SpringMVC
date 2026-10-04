package app.models;

public class User {
    private String nom;
    private String email;

    public User(String nom, String email) {
        this.nom = nom;
        this.email = email;
    }

    public String getNom() { return nom; }
    public String getEmail() { return email; }
}