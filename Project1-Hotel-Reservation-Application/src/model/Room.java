package model;

public class Room implements IRoom {
    private final String roomNumber;
    protected double price;
    private final RoomType roomType;
    public Room(String roomNumber, double price, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomType = roomType;
    }
    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public Double getRoomPrice() {
        return price;
    }

    @Override
    public RoomType getRoomType() {
        return roomType;
    }

    @Override
    public boolean isFree() {
        return price == 0;
    }

    @Override
    public void displayInfo() {
        System.out.println("roomNumber: " + roomNumber);
        System.out.println("price: " + price);
        System.out.println("RoomType: " + roomType.toString());
        System.out.println("---------------------------");
    }

    @Override
    public String toString() {
        return String.format("RoomNumer:\n %s \nRoomType: %s \nprice: %d",roomNumber, roomType, price);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        IRoom room = (IRoom) obj;
        return roomNumber == room.getRoomNumber();
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(roomNumber);
    }
}
