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

            sOut.println(Encode.encoder("Veuillez saisir votre numéro abonné."));
            int numAbo = Integer.parseInt(sIn.readLine());
             while (!this.isAbonne(numAbo)) {
                 sOut.println(Encode.encoder("Votre numéro abonné n'est pas reconnu. Veuillez réessayer."));
                 numAbo = Integer.parseInt(sIn.readLine());
             }

            sOut.println(Encode.encoder("Veuillez saisir le numéro d'un document à emprunter. \n" + super.listeDesDocuments()));
            int numDvdChoisi = Integer.parseInt(sIn.readLine());

            try {
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

    private void emprunterDocument(int numAbo, int numDocument) throws abonneNonTrouveException, documentNonTrouveException, SQLException, documentNonLibreException, IOException, documentDejaEmprunteException, documentDejaReserveException, InterruptedException, documentPourAdulteException {
            Abonne ab = this.getAbonne(numAbo);
            IDocument d = super.getDocument(numDocument);
            synchronized (d) {
                if (d.empruntePar() == null &&
                        (d.reservePar() == null || d.reservePar().getNumeroAdhérent() == ab.getNumeroAdhérent())) {
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
