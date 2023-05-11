import serveur.ServeurBTTP;
import services.ServiceReservation;

public class Appli {

    public static void main(String[] args) {


        new Thread(new ServeurBTTP(ServiceReservation.class, 1001)).start();
    }
}
