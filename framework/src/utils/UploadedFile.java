package utils;

public class UploadedFile {
    private String nom;
    private String extension;
    private String contentType;
    private byte[] contenu;

    public UploadedFile(String nomFichier, String contentType, byte[] contenu) {
        int slash = Math.max(nomFichier.lastIndexOf('/'), nomFichier.lastIndexOf('\\'));
        String simple = nomFichier.substring(slash + 1);

        int point = simple.lastIndexOf('.');
        if (point > 0) {
            this.nom = simple.substring(0, point);
            this.extension = simple.substring(point + 1);
        } else {
            this.nom = simple;
            this.extension = "";
        }
        this.contentType = contentType;
        this.contenu = contenu;
    }

    public String getNom() {
        return nom;
    }

    public String getExtension() {
        return extension;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getContenu() {
        return contenu;
    }
}