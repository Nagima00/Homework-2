import java.util.*;

public class Room {
    private String name;
    private String description;
    private Map<String, Room> connectedRooms;
    private List<Item> items;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.connectedRooms = new HashMap<>();
        this.items = new ArrayList<>();
    }

    public void setConnectedRoom(String direction, Room room) {
        connectedRooms.put(direction, room);
    }

    public Room getConnectedRoom(String direction) {
        return connectedRooms.get(direction);
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

    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append("Room: ").append(name).append("\n");
        sb.append(description).append("\n");

        if (!items.isEmpty()) {
            sb.append("Items here: ");
            for (Item item : items) {
                sb.append(item.getName()).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}

