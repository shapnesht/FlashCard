package flashcards;

import java.io.*;
import java.util.*;

public class App {
    public static Scanner scanner = new Scanner(System.in);
    Map<String, String> map;
    Map<String, Integer> countMistakes;
    List<String> list;

    App() {
        map = new HashMap<>();
        countMistakes = new HashMap<>();
        list = new ArrayList<>();
    }

    public void printMessage(String message) {
        System.out.println(message);
        list.add(message);
    }

    public String getStringValue() {
        String data = scanner.nextLine();
        list.add(data);
        return data;
    }

    public int getIntValue() {
        int data = scanner.nextInt();
        list.add(String.valueOf(data));
        return data;
    }

    public void addCard() {
        printMessage("The card:");
        String key = getStringValue();
        if (map.containsKey(key)) {
            printMessage("The card \"" + key + "\" already exists.");
        } else {
            printMessage("The definition of the card:");
            String value = getStringValue();
            if (map.containsValue(value)) {
                printMessage("The definition \"" + value + "\" already exists.");
            } else {
                map.put(key, value);
                printMessage("The pair (\"" + key + "\":\"" + value + "\") has been added.");
            }
        }
    }

    public void removeCard() {
        printMessage("Which card?");
        String key = getStringValue();
        if (map.containsKey(key)) {
            printMessage("The card has been removed.");
            map.remove(key);
        } else {
            printMessage("Can't remove \"" + key + "\": there is no such card.");
        }
        countMistakes.remove(key);
    }

    private String getStringKey(String value) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private String getIntegerKey(int value) {
        for (Map.Entry<String, Integer> entry : countMistakes.entrySet()) {
            if (entry.getValue() == value) {
                return entry.getKey();
            }
        }
        return null;
    }

    private ArrayList<String> getAllIntegerKey(int value) {
        ArrayList<String> tempList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : countMistakes.entrySet()) {
            if (entry.getValue() == value) {
                tempList.add(entry.getKey());
            }
        }
        return tempList;
    }

    public void askAboutCard() {
        printMessage("How many times to ask?");
        int n = getIntValue();
        getStringValue();
        int count = 0;

        while (count != n) {
            for (Map.Entry<String, String> element : map.entrySet()) {
                String key = element.getKey();
                String value = element.getValue();
                printMessage("Print the definition of \"" + key + "\":");
                String definition1 = getStringValue();
                if (value.equals(definition1)) {
                    printMessage("Correct!");
                } else {
                    countMistakes.put(key, countMistakes.getOrDefault(key, 0) + 1);
                    if (map.containsValue(definition1)) {
                        printMessage("Wrong. The right answer is \"" + element.getValue() + "\", but your definition is correct for \"" + getStringKey(definition1) + "\".");
                    } else {
                        printMessage("Wrong. The right answer is \"" + element.getValue() + "\".");
                    }
                }
                count++;
            }
        }
    }

    void exportData() throws IOException {
        printMessage("File name:");
        String fileAddress = getStringValue();
        File file = new File(fileAddress);
        boolean fileCreated = file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        for (Map.Entry<String, String> element : map.entrySet()) {
            fileWriter.write(element.getKey() + ":" + element.getValue() + ":" + countMistakes.getOrDefault(element.getKey(), 0) + "\n");
        }
        fileWriter.close();
        printMessage(map.size() + " cards have been saved.");
    }

    void importData() throws IOException {
        printMessage("File name:");
        int count = 0;
        String fileAddress = getStringValue();
        File file = new File(fileAddress);
        if (!file.exists()) {
            printMessage("File not found.");
            return;
        }
        Scanner scanner1 = new Scanner(file);
        while (scanner1.hasNextLine()) {
            String str = scanner1.nextLine().trim();
            map.put(str.split(":")[0], str.split(":")[1]);
            countMistakes.put(str.split(":")[0], Integer.valueOf(str.split(":")[2]));
            count++;
        }
        scanner1.close();
        printMessage(count + " cards have been loaded.");
    }

    void importDataFromFile(String fileName) throws IOException {
        Scanner scanner1 = new Scanner(new File(fileName));
        int count = 0;
        while (scanner1.hasNextLine()) {
            String str = scanner1.nextLine().trim();
            map.put(str.split(":")[0], str.split(":")[1]);
            countMistakes.put(str.split(":")[0], Integer.valueOf(str.split(":")[2]));
            count++;
        }
        scanner1.close();
        printMessage(count + " cards have been loaded.");
    }

    public void saveLog() throws IOException {
        printMessage("File name:");
        File file = new File(getStringValue());
        boolean isCreated = file.createNewFile();
        printMessage("The log has been saved.");
        PrintWriter fileWriter = new PrintWriter(file);
        fileWriter.write("\n");
        for (String str : list) {
            fileWriter.write(str);
            fileWriter.write("\n");
        }
        fileWriter.close();
    }

    public void hardestCard() {
        int maxValue = findMaxValue();
        int countOfMaxValue = findMaxValueCount(maxValue);
        if (maxValue <= 0) {
            printMessage("There are no cards with errors.");
            return;
        }
        if (countOfMaxValue == 1) {
            printMessage("The hardest card is \"" + getIntegerKey(maxValue) + "\". You have " + maxValue + " errors answering it.");
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            ArrayList<String> arrayList = getAllIntegerKey(maxValue);
            for (String str : arrayList) {
                stringBuilder.append("\"").append(str).append("\", ");
            }
            printMessage("The hardest cards are " + stringBuilder.substring(0, stringBuilder.lastIndexOf(",")) + ". You have " + countOfMaxValue + " errors answering them.");
        }
    }

    private int findMaxValueCount(int maxi) {
        int count = 0;
        for (Map.Entry<String, Integer> element : countMistakes.entrySet()) {
            if (element.getValue() == maxi) {
                count++;
            }
        }
        return count;
    }

    private int findMaxValue() {
        int maxi = -1;
        for (Map.Entry<String, Integer> element : countMistakes.entrySet()) {
            maxi = Math.max(maxi, element.getValue());
        }
        return maxi;
    }

    public void resetStats() {
        countMistakes.replaceAll((k, v) -> 0);
        printMessage("Card statistics have been reset.");
    }

    public void start() throws IOException {
        while (true) {
            String input = getCommand();
            switch (input) {
                case "add" -> addCard();
                case "remove" -> removeCard();
                case "import" -> importData();
                case "export" -> exportData();
                case "ask" -> askAboutCard();
                case "log" -> saveLog();
                case "hardest card" -> hardestCard();
                case "reset stats" -> resetStats();
            }
            if (input.equals("exit")) {
                System.out.println("Bye bye!");
                break;
            }
        }
    }

    private String getCommand() {
        printMessage("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
        return getStringValue();
    }

    public void exportDataToFile(String fileAddress) throws IOException {
        File file = new File(fileAddress);
        boolean fileCreated = file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        for (Map.Entry<String, String> element : map.entrySet()) {
            fileWriter.write(element.getKey() + ":" + element.getValue() + ":" + countMistakes.getOrDefault(element.getKey(), 0) + "\n");
        }
        fileWriter.close();
        printMessage(map.size() + " cards have been saved.");
    }
}
