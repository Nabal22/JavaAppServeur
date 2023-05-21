package model;

import exceptions.documentNonEmpruntéException;
import exceptions.documentNonLibreException;

import java.util.Date;

public abstract class Document implements IDocument {

    int idDocument;
    String titre;

    Enum etat;//reservé ou emprunté ou libre

    Abonne abonne;

    Date dateReservation;

    public Document(int idDocument, String titre, Etat etat) {
        this.idDocument = idDocument;
        this.titre = titre;
        this.etat = etat;
    }

    public Document(int idDocument, String titre, Etat etat, Abonne abonne) {
        this.idDocument = idDocument;
        this.titre = titre;
        this.etat = etat;
        this.abonne = abonne;
    }

    public Document(int idDocument, String titre, Etat etat, Abonne abonne, Date dateReservation) {
        this.idDocument = idDocument;
        this.titre = titre;
        this.etat = etat;
        this.abonne = abonne;
        this.dateReservation = dateReservation;
    }

    public int getIdDocument() {
        return idDocument;
    }

    // return null si pas emprunté ou pas réservé
    public Abonne empruntePar(){
        if(this.etat.equals(Etat.EMPRUNTE)) {
            return this.abonne;
        }
        else {
            return null;  // Abonné qui a emprunté ce document
        }
    }

    public Abonne reservePar() {
        if (this.etat.equals(Etat.RESERVE)) {
            return this.abonne;
        } else {
            return null;
        }
    } ; // Abonné qui a réservé ce document


    // precondition ni réservé ni emprunté
    public void reservation(Abonne ab) throws documentNonLibreException {
        if (this.etat.equals(Etat.LIBRE) && this.estAutorise(ab)){
            this.etat = Etat.RESERVE;
            this.abonne = ab;
            this.dateReservation = new Date();
        } else {
            throw new documentNonLibreException();
        }
    }


    // precondition libre ou réservé par l’abonné qui vient emprunter
    public void emprunt(Abonne ab) throws documentNonLibreException {
        if ((this.etat.equals(Etat.RESERVE) && this.abonne == ab) ||
                (this.etat.equals(Etat.LIBRE) && this.abonne == null)
                && this.estAutorise(ab)) {
            this.etat = Etat.EMPRUNTE;
            this.abonne = ab;
        } else {
            throw new documentNonLibreException();
        }
    }


    // retour d’un document ou annulation d‘une réservation
    public void retour() throws documentNonEmpruntéException {
        if(this.etat.equals(Etat.EMPRUNTE)) {
            this.etat = Etat.LIBRE;
            this.abonne = null;
        } else {
            throw new documentNonEmpruntéException();
        }

    };

    public abstract boolean estAutorise(Abonne ab);

    public String toString() {
        return this.titre + " " + "(" + this.etat + ")";
    }

    public Abonne getAbonne() {
        return this.abonne;
    }

    public Date getDateReservation() {
        return this.dateReservation;
    }
}
