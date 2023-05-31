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
        // Crée une connexion à la base de données
        ConnectionBD dbConnect = new ConnectionBD();
        dbConnect.connectToBD();

        // Récupère la liste des abonnés et des DVD depuis la base de données
        ArrayList<Abonne> abonnes = dbConnect.getAbonnesFromDB();
        ArrayList<IDocument> dvds = dbConnect.getDocumentsFROMDB(abonnes);

        // Configure les services avec les abonnés, les documents et la connexion à la base de données
        ServiceReservation.setAbonnes(abonnes);
        ServiceReservation.setDocuments(dvds);
        ServiceReservation.setConnectionDB(dbConnect);
        ServiceRetour.setAbonnes(abonnes);
        ServiceRetour.setDocuments(dvds);
        ServiceRetour.setConnectionDB(dbConnect);
        ServiceEmprunt.setAbonnes(abonnes);
        ServiceEmprunt.setDocuments((dvds));
        ServiceRetour.setConnectionDB(dbConnect);

        // Lance les serveurs sur différents ports, en utilisant les classes de services correspondantes
        new Thread(new ServeurBTTP(ServiceReservation.class, 1001)).start();
        new Thread(new ServeurBTTP(ServiceRetour.class, 1002)).start();
        new Thread(new ServeurBTTP(ServiceEmprunt.class, 1003)).start();
    }
}
