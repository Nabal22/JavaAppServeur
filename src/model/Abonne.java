package model;

public class Abonne {
    int numeroAdhérent;
    String nom;
    String dateNaissance;

    public Abonne(int numero, String nom, String dateNaissance) {
        this.numeroAdhérent = numero;
        this.nom = nom;
        this.dateNaissance = dateNaissance;
    }

    public int getNumeroAdhérent() {
        return numeroAdhérent;
    }

    public String getNom() {
        return nom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }


}
