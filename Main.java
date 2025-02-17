public class Main {
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

