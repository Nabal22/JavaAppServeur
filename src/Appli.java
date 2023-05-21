import BD.ConnectionBD;
import model.Abonne;
import model.IDocument;
import serveur.ServeurBTTP;
import services.ServiceEmprunt;
import services.ServiceReservation;
import services.ServiceRetour;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class Appli {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
        ConnectionBD dbConnect = new ConnectionBD();
        dbConnect.connectToBD();
        ArrayList<Abonne> abonnes = dbConnect.getAbonnesFromDB();
        ArrayList<IDocument> dvds = dbConnect.getDocumentsFROMDB(abonnes);


        ServiceReservation.setAbonnes(abonnes);
        ServiceReservation.setDvds(dvds);
        ServiceRetour.setAbonnes(abonnes);
        ServiceRetour.setDvds(dvds);
        ServiceEmprunt.setAbonnes(abonnes);
        ServiceEmprunt.setDocuments((dvds));

        new Thread(new ServeurBTTP(ServiceReservation.class, 1001)).start();
        new Thread(new ServeurBTTP(ServiceRetour.class, 1002)).start();
        new Thread(new ServeurBTTP(ServiceEmprunt.class, 1003)).start();
    }
}
