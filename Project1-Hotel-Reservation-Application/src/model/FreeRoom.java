package model;

public class FreeRoom extends Room{
    public FreeRoom(){
        super("123", 0, RoomType.SINGLE);
        this.price = 0;
    }
    @Override
    public String toString() {
        return String.format("FreeRoom");
    }
}
