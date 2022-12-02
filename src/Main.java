import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ArrayList<String> arrList = new ArrayList<>();
        String cmd = "";
        boolean run = true;
        boolean needsToBeSaved = false;
        String fileName = "";
        do {
            cmd = printMenu(in, arrList);
            switch (cmd) {
                case "A":
                    // prompt user for list item
                    addToList(in, arrList);
                    needsToBeSaved = true;
                    // make sure it's not empty string (si getnonzerolengthstring)
                    // add to list
                    break;
                case "C":
                    // clears items from list
                    clearList(arrList);
                    needsToBeSaved = true;
                    break;
                case "D":
                    // prompt user for the number of the item to delete
                    // translate the number to an index by subtracting 1
                    // remove item from the list
                    deleteFromList(in, arrList);
                    needsToBeSaved = true;
                    break;
                case "O":
                    // opens file
                    fileName = openListFile(in, arrList, needsToBeSaved);
                    break;
                case "S":
                    // saves file
                    saveCurrentFile(arrList, fileName);
                    needsToBeSaved = false;
                    break;
                case "V":
                    // prints out the array
                    displayList(arrList);
                    break;
                case "Q":
                    if (SafeInput.getYNConfirm(in, "Are you sure")) {
                        if (needsToBeSaved) {
                            saveCurrentFile(arrList, fileName);
                        }
                        run = false;
                    } else {
                        System.out.println(" ");
                    }
                    break;
            }
        } while (run);
    }

    private static String printMenu(Scanner in, ArrayList arrList) {

        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        if (arrList.isEmpty()) {

            System.out.println("List is empty");

        } else {

            for (int i = 0; i < arrList.size(); i++) {
                System.out.printf("%3d%35s\n", i + 1 , arrList.get(i));

            }
        }

        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        return SafeInput.getRegExString(in, "A: Add C: Clear D: Delete O: Open S: Save V: View Q: Quit", "[AaCcDdOoSsVvQq]").toUpperCase();
    }

    public static void addToList(Scanner in, ArrayList arrList) {
        String itemToAdd = SafeInput.getNonZeroLenString(in, "What would you like to add to the array list");
        arrList.add(itemToAdd);
    }

    public static void clearList(ArrayList arrList) {
        arrList.clear();
    }

    public static void deleteFromList(Scanner in, ArrayList arrList) {
        int itemToDelete = SafeInput.getRangedInt(in, "Enter item to delete: ", 1, arrList.size());
        arrList.remove(itemToDelete - 1);
        System.out.println(itemToDelete + " removed");
    }

    public static void displayList(ArrayList arrList) {
        for (int i = 0; i < arrList.size(); i++) {
            System.out.println(arrList.get(i));
        }
    }

    private static String openListFile(Scanner in, ArrayList arrList, boolean needsToBeSaved) {
        if (needsToBeSaved) {
            String prompt = "You will loose your current list, are you sure? ";
            boolean burnListYN = SafeInput.getYNConfirm(in, prompt);
            if (!burnListYN) {
                return "";
            }
        }
        clearList(arrList);
        Scanner inFile;
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        chooser.setFileFilter(filter);
        String line;

        Path target = new File(System.getProperty("user.dir")).toPath();
        target = target.resolve("src");
        chooser.setCurrentDirectory(target.toFile());

        try {
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                target = chooser.getSelectedFile().toPath();
                inFile = new Scanner(target);
                System.out.println("Opening File: " + target.getFileName());
                while (inFile.hasNextLine()) {
                    line = inFile.nextLine();
                    arrList.add(line);
                }
                inFile.close();
            } else { // user did not select a file
                System.out.println("You must select a file, try again");
            }
        } catch (IOException e) {
            System.out.println("IOException Error");
        }
        return target.toFile().toString();
    }

    public static void saveCurrentFile(ArrayList arrList, String fileName) {
        PrintWriter outFile;
        Path target = new File(System.getProperty("user.dir")).toPath();
        if (fileName.equals("")) {
            target = target.resolve("list.txt");
        } else {
            target = target.resolve(fileName);
        }

        try {
            outFile = new PrintWriter(target.toString());
            for (int i = 0; i < arrList.size(); i++) {
                outFile.println(arrList.get(i));
            }
            outFile.close();
            System.out.printf("Saved \"%s\"\n", target.getFileName());
        } catch (IOException e) {
            System.out.println("IOException Error");
        }
    }

}
