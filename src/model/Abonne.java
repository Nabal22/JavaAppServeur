package model;

import java.util.Date;

public class Abonne {
    protected int numeroAdhérent; // Numéro d'adhérent de l'abonné
    protected String nom; // Nom de l'abonné
    protected Date dateNaissance; // Date de naissance de l'abonné

    /**
     * Constructeur de la classe Abonne.
     * @param numero le numéro d'adhérent de l'abonné
     * @param nom le nom de l'abonné
     * @param dateNaissance la date de naissance de l'abonné
     */
    public Abonne(int numero, String nom, Date dateNaissance) {
        this.numeroAdhérent = numero;
        this.nom = nom;
        this.dateNaissance = dateNaissance;
    }

    /**
     * Retourne le numéro d'adhérent de l'abonné.
     * @return le numéro d'adhérent
     */
    public int getNumeroAdhérent() {
        return numeroAdhérent;
    }

    /**
     * Calcule et retourne l'âge de l'abonné.
     * @return l'âge de l'abonné
     */
    public int getAge() {
        int currentYear = new Date().getYear(); // Année actuelle
        int birthYear = this.dateNaissance.getYear(); // Année de naissance de l'abonné
        return currentYear - birthYear;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de l'objet Abonne.
     * @return la représentation de l'abonné sous forme de chaîne de caractères
     */
    public String toString() {
        return "Abonne{" +
                "numeroAdhérent=" + numeroAdhérent +
                ", nom='" + nom + '\'' +
                ", dateNaissance='" + dateNaissance + '\'' +
                '}';
    }
}
