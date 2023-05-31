package services;

import encodage.Encode;
import exceptions.*;
import model.Abonne;
import model.IDocument;
import timerTask.AnnulationReservation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class ServiceReservation extends ServiceCommun {

    public ServiceReservation(Socket s) {
        super(s);
    }

    @Override
    public void run() {
        try {
            BufferedReader sIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter sOut = new PrintWriter(s.getOutputStream(), true);

            // Demande au client de saisir son numéro d'abonné
            sOut.println(Encode.encoder("Veuillez saisir votre numéro abonné."));
            int numAbo = Integer.parseInt(sIn.readLine());

            // Vérifie si le numéro d'abonné est valide, sinon demande de réessayer
            while (!this.isAbonne(numAbo)) {
                sOut.println(Encode.encoder("Votre numéro abonné n'est pas reconnu. Veuillez réessayer."));
                numAbo = Integer.parseInt(sIn.readLine());
            }

            // Demande au client de saisir le numéro du document à réserver
            sOut.println(Encode.encoder("Veuillez saisir le numéro d'un document à réserver. \n" + super.listeDesDocuments()));
            int numDocumentChoisi = Integer.parseInt(sIn.readLine());

            try {
                // Tente de réserver le document
                this.réserverDocument(numAbo, numDocumentChoisi);
                sOut.println("Réservation effectuée.");
            } catch (SQLException e ) {
                sOut.println("sql : " + e.getMessage());
            } catch (RestrictionException e) {
                sOut.println(e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Réserve un document pour un abonné donné.
     * @param numAbo le numéro de l'abonné.
     * @param numDocument le numéro du document à réserver.
     * @throws SQLException en cas d'erreur SQL lors de la réservation du document.
     * @throws RestrictionException si le document est déjà réservé ou déjà emprunté.
     */
    private void réserverDocument(int numAbo, int numDocument) throws SQLException, RestrictionException {
        Abonne ab = this.getAbonne(numAbo);
        IDocument d = this.getDocument(numDocument);

        // Synchronisation pour éviter les problèmes d'accès concurrent au document
        synchronized (d) {
            if(d.reservePar() == null && d.empruntePar() == null) {
                // Le document est disponible, on peut le réserver
                Timer timer = new Timer();
                d.reservation(ab);
                super.dbConnect.reserverDocumentBD(ab, d);
                timer.schedule(new AnnulationReservation(d, super.dbConnect), TimeUnit.HOURS.toMillis(2));
            } else if (d.reservePar() != null) {
                throw new RestrictionException("Le document est déjà réservé.");
            } else if (d.empruntePar() != null) {
                throw new RestrictionException("Le document est déjà emprunté.");
            }
        }
    }
}
