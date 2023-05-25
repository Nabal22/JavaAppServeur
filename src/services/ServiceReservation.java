package services;
import BD.ConnectionBD;
import encodage.Encode;
import exceptions.*;
import model.Abonne;
import model.IDocument;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceReservation extends ServiceCommun {



    public ServiceReservation(Socket s) {
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

            sOut.println(Encode.encoder("Veuillez saisir le numéro d'un document à réserver. \n" + super.listeDesDocuments()));
            int numDocumentChoisi = Integer.parseInt(sIn.readLine());



            try {
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



    private void réserverDocument(int numAbo, int numDocument) throws SQLException, RestrictionException{
            Abonne ab = this.getAbonne(numAbo);
            IDocument d = this.getDocument(numDocument);
            synchronized (d) {
                if(d.reservePar() == null && d.empruntePar() == null) {
                    d.reservation(ab);
                    super.dbConnect.reserverDocumentBD(ab, d);
                } else if (d.reservePar() != null) {
                    throw new RestrictionException("Le document est déjà réservé.");
                } else if (d.empruntePar() != null) {
                    throw new RestrictionException("Le document est déjà emprunté.");
                }
            }

    }


}
