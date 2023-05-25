package model;

import java.util.Date;

public class Abonne {
    protected int numeroAdhérent;
    protected String nom;
    protected Date dateNaissance;

    public Abonne(int numero, String nom, Date dateNaissance) {
        this.numeroAdhérent = numero;
        this.nom = nom;
        this.dateNaissance = dateNaissance;
    }

    public int getNumeroAdhérent() {
        return numeroAdhérent;
    }

    public int getAge() {
        return new Date().getYear() - this.dateNaissance.getYear();
    }

    public String toString() {
        return "Abonne{" +
                "numeroAdhérent=" + numeroAdhérent +
                ", nom='" + nom + '\'' +
                ", dateNaissance='" + dateNaissance + '\'' +
                '}';
    }
}
