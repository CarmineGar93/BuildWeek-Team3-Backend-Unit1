package CarmineGargiulo;

import java.util.Scanner;

public class MenuInterattivo {
    private final Scanner scanner = new Scanner(System.in);
    public void menuAmministratore(){
        while (true) {
            System.out.println("Che cosa vuoi fare ?");
            System.out.println("1 per controllo biglietti/abbonamenti venduti");
            System.out.println("2 per controllo biglietti obliterati");
            System.out.println("3 per lista mezzi in manutezione e in servizio");
            System.out.println("4 per tratte scoperte");
            System.out.println("5 per mettere un veicolo in servizio o in manutenzione");
            System.out.println("0 per uscire");
            int scelta;
            while (true){
                scelta = verifyInput();
                if (scelta < 0 || scelta > 5) System.out.println("Devi inserire un numero tra zero e 5");
                else break;
            }
            switch (scelta){
                case 0 -> {
                    System.out.println("Logout da amministratore");
                    break;
                }
            }
        }


    }

    private int verifyInput(){
        int number;
        while(true){
            String input = scanner.nextLine();
            try {
                number = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e){
                System.out.println("Devi inserire un numero");
            }
        }
        return number;
    }
}
