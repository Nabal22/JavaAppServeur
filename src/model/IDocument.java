package model;

import exceptions.*;

public interface IDocument {
    int numero();

    // return null si pas emprunté ou pas réservé
    Abonne empruntePar() ; // Abonné qui a emprunté ce document
    Abonne reservePar() ; // Abonné qui a réservé ce document

    // precondition ni réservé ni emprunté
    void reservation(Abonne ab) throws RestrictionException;

    // precondition libre ou réservé par l’abonné qui vient emprunter
    void emprunt(Abonne ab) throws RestrictionException;

    // retour d’un document ou annulation d‘une réservation
    void retour();
}
