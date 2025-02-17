import java.util.Scanner;

public class MUDController {
    private Player player;
    private boolean running;
    private Scanner scanner;

    public MUDController(Player player) {
        this.player = player;
        this.running = true;
        this.scanner = new Scanner(System.in);
    }

    public void runGameLoop() {
        System.out.println("Welcome to the MUD Game! Type 'help' for a list of commands.");
        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim().toLowerCase();
            handleInput(input);
        }
        System.out.println("Thanks for playing!");
    }

    private void handleInput(String input) {
        if (input.isEmpty()) return;

        String[] parts = input.split(" ", 2);
        String command = parts[0];
        String argument = parts.length > 1 ? parts[1] : "";

        switch (command) {
            case "look":
                lookAround();
                break;
            case "move":
                move(argument);
                break;
            case "pick":
                if (argument.startsWith("up ")) {
                    pickUp(argument.substring(3));
                } else {
                    System.out.println("Invalid command. Did you mean 'pick up <item>'?");
                }
                break;
            case "inventory":
                checkInventory();
                break;
            case "help":
                showHelp();
                break;
            case "quit":
            case "exit":
                running = false;
                break;
            default:
                System.out.println("Unknown command.");
        }
    }

    private void lookAround() {
        Room currentRoom = player.getCurrentRoom();
        System.out.println(currentRoom.describe());
    }

    private void move(String direction) {
        if (direction.isEmpty()) {
            System.out.println("Move where? Use: move <forward|back|left|right>");
            return;
        }

        Room nextRoom = player.getCurrentRoom().getConnectedRoom(direction);
        if (nextRoom != null) {
            player.setCurrentRoom(nextRoom);
            System.out.println("You move " + direction + ".");
            lookAround();
        } else {
            System.out.println("You can't go that way!");
        }
    }

    private void pickUp(String itemName) {
        if (itemName.isEmpty()) {
            System.out.println("Pick up what?");
            return;
        }

        Room currentRoom = player.getCurrentRoom();
        Item item = currentRoom.getItem(itemName);
        if (item != null) {
            player.addItem(item);
            currentRoom.removeItem(item);
            System.out.println("You pick up the " + itemName + ".");
        } else {
            System.out.println("No item named '" + itemName + "' here!");
        }
    }

    private void checkInventory() {
        player.showInventory();
    }

    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("look - Describe the current room");
        System.out.println("move <forward|back|left|right> - Move in a direction");
        System.out.println("pick up <item> - Pick up an item");
        System.out.println("inventory - Show your items");
        System.out.println("help - Show this menu");
        System.out.println("quit/exit - Leave the game");
    }

    public static void main(String[] args) {
        Room startRoom = new Room("A small stone chamber", "A dimly lit, ancient room with dust everywhere.");
        Room hallway = new Room("A narrow hallway", "A dark corridor with flickering torches.");

        startRoom.setConnectedRoom("forward", hallway);
        hallway.setConnectedRoom("back", startRoom);

        Item sword = new Item("sword");
        startRoom.addItem(sword);

        Player player = new Player(startRoom);
        MUDController controller = new MUDController(player);
        controller.runGameLoop();
    }
}

