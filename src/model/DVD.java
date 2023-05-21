package model;

import exceptions.documentNonEmpruntéException;
import exceptions.documentNonLibreException;

import java.util.Date;

public class DVD extends Document {
    Boolean estPourAdulte;

    public DVD(int numero, String titre, Etat etat, boolean estPourAdulte) {
        super(numero, titre, etat);
        this.estPourAdulte = estPourAdulte;
    }

    public DVD(int numero, String titre, Etat etat, Abonne abonne, boolean estPourAdulte) {
        super(numero, titre, etat, abonne);
        this.estPourAdulte = estPourAdulte;
    }

    public  DVD(int numero, String titre, Etat etat, Abonne abonne, Date dateReservation, boolean estPourAdulte) {
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
        String estPourAdulteString = this.estPourAdulte ? "oui" : "non";
        String etatString = this.etat.equals(Etat.EMPRUNTE) ? "emprunté" : this.etat.equals(Etat.LIBRE) ? "libre" : "réservé";
        String abonneString = this.abonne == null ? "aucun" : this.abonne.toString();

        return "DVD{" +
                "idDocument=" + idDocument +
                ", titre='" + titre + '\'' +
                ", estPourAdulte=" + estPourAdulteString +
                ", etat=" + etatString +
                ", abonne=" + abonneString +
                ", dateReservation=" + dateReservation +
                '}';
    }

}