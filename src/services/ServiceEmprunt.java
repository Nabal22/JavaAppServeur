package services;

import BD.ConnectionBD;
import encodage.Encode;
import exceptions.RestrictionException;
import model.Abonne;
import model.IDocument;

import java.awt.geom.RectangularShape;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

public class ServiceEmprunt extends ServiceCommun {

    public ServiceEmprunt(Socket s) {
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

            // Demande au client de saisir le numéro du document à emprunter
            sOut.println(Encode.encoder("Veuillez saisir le numéro d'un document à emprunter. \n" + super.listeDesDocuments()));
            int numDvdChoisi = Integer.parseInt(sIn.readLine());

            try {
                // Tente d'emprunter le document
                this.emprunterDocument(numAbo, numDvdChoisi);
                sOut.println(Encode.encoder("Vous avez emprunté ce document."));
            } catch (RestrictionException e) {
                sOut.println(e.getMessage());
            } catch (SQLException e) {
                sOut.println(Encode.encoder("Erreur SQL."+e.getMessage()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RestrictionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Emprunte un document pour un abonné donné.
     * @param numAbo le numéro de l'abonné.
     * @param numDocument le numéro du document à emprunter.
     * @throws SQLException en cas d'erreur SQL lors de l'emprunt du document.
     * @throws IOException en cas d'erreur d'entrée/sortie lors de l'emprunt du document.
     */
    private void emprunterDocument(int numAbo, int numDocument) throws  SQLException, IOException {
        Abonne ab = this.getAbonne(numAbo);
        IDocument d = super.getDocument(numDocument);

        // Synchronisation pour éviter les problèmes d'accès concurrent au document
        synchronized (d) {
            if (d.empruntePar() == null &&
                    (d.reservePar() == null || d.reservePar().getNumeroAdhérent() == ab.getNumeroAdhérent())) {
                // Le document est disponible, on peut l'emprunter
                d.emprunt(ab);
                super.dbConnect.emprunterDocument(d, ab);
                System.out.println("Document emprunté.");
            } else {
                if (d.empruntePar() != null) {
                    throw new RestrictionException("Document déjà emprunté");
                } else if (d.reservePar() != null || d.reservePar().getNumeroAdhérent() != ab.getNumeroAdhérent()) {
                    throw new RestrictionException("Document déjà réservé");
                }
            }
        }
    }
}
