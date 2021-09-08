package banking;

import banking.ui.UserInterface;

public class Main {
    public static void main(String[] args) {
        UserInterface userInterface = new UserInterface();
        userInterface.interact(args[1]);
    }
}
