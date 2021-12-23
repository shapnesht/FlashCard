package flashcards;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        App app = new App();
        if (args.length == 2) {
            if (args[0].equals("-import")) {
                app.importDataFromFile(args[1]);
                app.start();
            } else if (args[0].equals("-export")) {
                app.start();
                app.exportDataToFile(args[1]);
            }
        } else if (args.length == 4){
            if (args[0].equals("-import")) {
                app.importDataFromFile(args[1]);
                app.start();
                app.exportDataToFile(args[3]);
            } else if (args[0].equals("-export")) {
                app.importDataFromFile(args[3]);
                app.start();
                app.exportDataToFile(args[1]);
            }
        } else {
            app.start();
        }
    }
}