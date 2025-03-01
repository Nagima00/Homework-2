import java.util.*;

public class MUDController {
    private Player player;
    private boolean running;

    public MUDController(Player player) {
        this.player = player;
        this.running = true;
    }

    public void runGameLoop() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the MUD game! Type 'help' for commands.");

        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            handleInput(input);
        }

        System.out.println("Thanks for playing!");
    }

    private void handleInput(String input) {
        if (input.isEmpty()) return;

        String[] parts = input.split(" ", 2);
        String command = parts[0].toLowerCase();
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
                    System.out.println("Invalid command. Use 'pick up <item>'.");
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
        System.out.println("Room: " + currentRoom.getName());
        System.out.println(currentRoom.getDescription());
        System.out.println("Items here: " + currentRoom.getItems());
    }

    private void move(String direction) {
        if (direction.isEmpty()) {
            System.out.println("Specify a direction: forward, back, left, or right.");
            return;
        }

        Room nextRoom = player.getCurrentRoom().getConnectedRoom(direction);
        if (nextRoom != null) {
            player.setCurrentRoom(nextRoom);
            System.out.println("You moved " + direction + ".");
            lookAround();
        } else {
            System.out.println("You can't go that way!");
        }
    }

    private void pickUp(String itemName) {
        if (itemName.isEmpty()) {
            System.out.println("Specify an item name.");
            return;
        }

        Room currentRoom = player.getCurrentRoom();
        Item item = currentRoom.getItem(itemName);

        if (item != null) {
            player.addItem(item);
            currentRoom.removeItem(item);
            System.out.println("You picked up the " + itemName + ".");
        } else {
            System.out.println("No item named '" + itemName + "' here!");
        }
    }

    private void checkInventory() {
        List<Item> inventory = player.getInventory();
        if (inventory.isEmpty()) {
            System.out.println("You are carrying nothing.");
        } else {
            System.out.println("You are carrying:");
            for (Item item : inventory) {
                System.out.println("- " + item.getName());
            }
        }
    }

    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("look - Describes the current room.");
        System.out.println("move <forward|back|left|right> - Moves in the given direction.");
        System.out.println("pick up <item> - Picks up an item.");
        System.out.println("inventory - Lists the items you are carrying.");
        System.out.println("help - Shows this message.");
        System.out.println("quit/exit - Ends the game.");
    }

    public static void main(String[] args) {
        Room startRoom = new Room("Starting Room", "A small stone chamber with exits in all directions.");
        Room northRoom = new Room("Hallway", "A dimly lit hallway.");
        startRoom.setConnectedRoom("forward", northRoom);

        Item sword = new Item("sword");
        startRoom.addItem(sword);

        Player player = new Player(startRoom);
        MUDController controller = new MUDController(player);
        controller.runGameLoop();
    }
}

class Player {
    private Room currentRoom;
    private List<Item> inventory;

    public Player(Room startRoom) {
        this.currentRoom = startRoom;
        this.inventory = new ArrayList<>();
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public List<Item> getInventory() {
        return inventory;
    }
}

class Room {
    private String name;
    private String description;
    private Map<String, Room> connections;
    private List<Item> items;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.connections = new HashMap<>();
        this.items = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setConnectedRoom(String direction, Room room) {
        connections.put(direction, room);
    }

    public Room getConnectedRoom(String direction) {
        return connections.get(direction);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public Item getItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    public List<Item> getItems() {
        return items;
    }
}

class Item {
    private String name;

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
