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
                // Lire l'entrée utilisateur
                System.out.println("Entrez votre message : ");
                String message = scanner.nextLine();

                // Envoyer le message au serveur
                out.println(message);

                // Lire la réponse du serveur
                String reponse = in.readLine();
                System.out.println("Réponse du serveur : " + reponse);

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
