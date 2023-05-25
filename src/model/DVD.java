package model;

import java.util.Date;
import java.util.GregorianCalendar;

public class DVD extends Document {
    protected Boolean estPourAdulte;

    public DVD(int numero, String titre, Etat etat, boolean estPourAdulte) {
        super(numero, titre, etat);
        this.estPourAdulte = estPourAdulte;
    }

    public DVD(int numero, String titre, Etat etat, Abonne abonne, boolean estPourAdulte) {
        super(numero, titre, etat, abonne);
        this.estPourAdulte = estPourAdulte;
    }

    public  DVD(int numero, String titre, Etat etat, Abonne abonne, GregorianCalendar dateReservation, boolean estPourAdulte) {
        super(numero, titre, etat, abonne, dateReservation);
        this.estPourAdulte = estPourAdulte;
    }

    public Boolean getEstPourAdulte() {
        return estPourAdulte;
    }

    @Override
    public boolean estAutorise(Abonne ab) {
        if (this.estPourAdulte) {
            return ab.getAge() >= 18;
        } else {
            return true;
        }
    }

    @Override
    public int numero() {
        return this.idDocument;
    }
    @Override
    public String toString() {
        String documentString = super.toString();
        String estPourAdulteString = this.estPourAdulte ? "oui" : "non";

        return "DVD{" + super.toString() + "estPourAdulte : "+ estPourAdulteString+"}";
    }



}