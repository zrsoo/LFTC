import model.FiniteAutomata;

import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {
        FiniteAutomata fa = new FiniteAutomata("FA.in");

        int cmd;
        Scanner scanner = new Scanner(System.in);

        while (true)
        {
            printMenu();

            cmd = scanner.nextInt();
            scanner.nextLine();

            switch (cmd) {
                case 1 -> fa.printStates();
                case 2 -> fa.printAlphabet();
                case 3 -> fa.printTransitions();
                case 4 -> fa.printInitialState();
                case 5 -> fa.printFinalState();
                case 6 -> {
                    System.out.println("Insert sequence:");
                    String sequence = scanner.nextLine();
                    if (fa.checkSequence(sequence))
                        System.out.println("Sequence is CORRECT.");
                }
                case 7 -> System.exit(0);
            }
        }
    }

    private static void printMenu()
    {
        System.out.println("""
                
                COMMAND LIST:
                1 - Display set of states
                2 - Display alphabet
                3 - Display transitions
                4 - Display initial state
                5 - Display final state
                6 - Check sequence
                7 - EXIT
                
                COMMAND:""");
    }
}
