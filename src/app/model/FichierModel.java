package app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "fichier")
public class FichierModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "extension")
    private String extension;

    @Lob
    @Column(name = "contenu")
    private byte[] contenu;

    public FichierModel() {
    }

    public FichierModel(String nom, String extension, byte[] contenu) {
        this.nom = nom;
        this.extension = extension;
        this.contenu = contenu;
    }

    public FichierModel(Long id, String nom, String extension) {
        this.id = id;
        this.nom = nom;
        this.extension = extension;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getExtension() {
        return extension;
    }

    public byte[] getContenu() {
        return contenu;
    }
}