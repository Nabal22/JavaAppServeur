package model;

import java.util.Date;
import java.util.GregorianCalendar;

public class DVD extends Document {

    protected Boolean estPourAdulte; // Indique si le DVD est destiné à un public adulte

    /**
     * Constructeur de la classe DVD.
     * @param numero le numéro du DVD.
     * @param titre le titre du DVD.
     * @param etat l'état du DVD.
     * @param estPourAdulte indique si le DVD est destiné à un public adulte.
     */
    public DVD(int numero, String titre, Etat etat, boolean estPourAdulte) {
        super(numero, titre, etat);
        this.estPourAdulte = estPourAdulte;
    }

    /**
     * Constructeur de la classe DVD avec abonné.
     * @param numero le numéro du DVD.
     * @param titre le titre du DVD.
     * @param etat l'état du DVD.
     * @param abonne l'abonné qui a emprunté ou réservé le DVD.
     * @param estPourAdulte indique si le DVD est destiné à un public adulte.
     */
    public DVD(int numero, String titre, Etat etat, Abonne abonne, boolean estPourAdulte) {
        super(numero, titre, etat, abonne);
        this.estPourAdulte = estPourAdulte;
    }

    /**
     * Constructeur de la classe DVD avec abonné et date de réservation.
     * @param numero le numéro du DVD.
     * @param titre le titre du DVD.
     * @param etat l'état du DVD.
     * @param abonne l'abonné qui a emprunté ou réservé le DVD.
     * @param dateReservation la date de réservation du DVD.
     * @param estPourAdulte indique si le DVD est destiné à un public adulte.
     */
    public DVD(int numero, String titre, Etat etat, Abonne abonne, GregorianCalendar dateReservation, boolean estPourAdulte) {
        super(numero, titre, etat, abonne, dateReservation);
        this.estPourAdulte = estPourAdulte;
    }

    /**
     * Méthode permettant de vérifier si un abonné est autorisé à emprunter le DVD.
     * @param ab l'abonné à vérifier.
     * @return true si l'abonné est autorisé, false sinon.
     */
    @Override
    public boolean estAutorise(Abonne ab) {
        // Vérifie si l'abonné a l'âge requis pour emprunter le DVD
        if (this.estPourAdulte) {
            return ab.getAge() >= 18;
        } else {
            return true;
        }
    }

    /**
     * Retourne le numéro du DVD.
     * @return le numéro du DVD.
     */
    @Override
    public int numero() {
        return this.idDocument;
    }

    /**
     * Renvoie une représentation sous forme de chaîne de caractères de l'objet DVD.
     * @return la représentation sous forme de chaîne de caractères.
     */
    @Override
    public String toString() {
        String documentString = super.toString();
        String estPourAdulteString = this.estPourAdulte ? "oui" : "non";

        return "DVD{" + super.toString() + "estPourAdulte : " + estPourAdulteString + "}";
    }
}