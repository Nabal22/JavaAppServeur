import encodage.Decode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try {
            // Créer un socket pour se connecter au serveur EmpruntServeur
            Socket socket = new Socket("localhost", 1001);

            // Créer un scanner pour lire les entrées utilisateur
            Scanner scanner = new Scanner(System.in);

            // Créer un PrintWriter pour envoyer les données au serveur
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Créer un buffer reader pour lire les données envoyées par le serveur
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Boucle pour permettre à l'utilisateur d'envoyer plusieurs messages
            while (true) {
                // Lire la réponse du serveur => choisir numéro abonne
                String reponse = in.readLine();
                System.out.println("Réponse du serveur : " + Decode.decoder(reponse));

                //envoyer au serveur => numéro abonne
                System.out.println("Entrez votre message : ");
                String message = scanner.next();
                out.println(message);

                // Lire la réponse du serveur => liste des DVDs
                reponse = in.readLine();
                System.out.println("Réponse du serveur : " + Decode.decoder(reponse));

                //envoyer au serveur => numéro du DVD
                System.out.println("Entrez votre message : ");
                message = scanner.next();
                out.println(message);

                // Lire la réponse du serveur => réservation effectuée
                reponse = in.readLine();
                System.out.println("Réponse du serveur : " + Decode.decoder(reponse));



                // Si l'utilisateur entre "exit", sortir de la boucle
                if (message.equals("exit")) {
                    break;
                }
            }

            // Fermer les ressources
            scanner.close();
            out.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
