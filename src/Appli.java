import model.Abonne;
import model.Document;
import serveur.ServeurBTTP;
import services.ServiceReservation;

import java.sql.SQLException;
import java.util.ArrayList;

public class Appli {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        ConnectionBD dbConnect = new ConnectionBD();
        dbConnect.connectToBD();
        ArrayList<Abonne> abonnes = dbConnect.getAbonnesFromDB();
        ArrayList<Document> dvds = dbConnect.getDvdsFROMDB(abonnes);




        new Thread(new ServeurBTTP(ServiceReservation.class, 1001)).start();
    }
}
