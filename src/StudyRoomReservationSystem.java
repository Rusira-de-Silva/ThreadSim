import java.util.ArrayList;

class StudyRoom {
    private final int roomNumber;
    private final int capacity;
    private boolean isAvailable;

    public StudyRoom(int roomNumber, int capacity, boolean status){
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.isAvailable = status;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public String getAvailability(){
        if (isAvailable) return "Available";
        else return "Unavailable";
    }
}

class StudyRoomUnavailableException extends RuntimeException{

    public StudyRoomUnavailableException(String message){
        super(message);
    }
}
public class StudyRoomReservationSystem {
    private final ArrayList<StudyRoom> rooms = new ArrayList<>();

    public synchronized void reserveStudyRoom(int roomNumber) throws StudyRoomUnavailableException{
        for (StudyRoom room : rooms){
            if (room.getRoomNumber() == roomNumber){
                if (room.isAvailable()) {
                    System.out.println("Reserving room = " + roomNumber);
                    room.setAvailable(false);
                }
                else throw new StudyRoomUnavailableException("Room number = " + roomNumber + " already reserved!");
            }

        }
    }

    public synchronized void releaseStudyRoom(int roomNumber){
        for (StudyRoom room : rooms){
            if (room.getRoomNumber() == roomNumber){
                System.out.println("Releasing room = " + roomNumber);
                room.setAvailable(true);
            }
        }
    }

    public void displayStudyRoomStatus(){
        for (StudyRoom room : rooms){
            System.out.println("Room number = " + room.getRoomNumber() + "  Availability = " + room.getAvailability());
        }
    }

    public static void main(String[] args) {
        StudyRoomReservationSystem managingSystem = new StudyRoomReservationSystem();
        StudyRoom room1 = new StudyRoom(1, 4,true);
        StudyRoom room2 = new StudyRoom(2, 2,true);
        StudyRoom room3 = new StudyRoom(3, 6,true);

        managingSystem.rooms.add(room1);
        managingSystem.rooms.add(room2);
        managingSystem.rooms.add(room3);

        managingSystem.displayStudyRoomStatus();

        Thread manageThread1 = new Thread(() -> {
            try{
                managingSystem.reserveStudyRoom(1001);
                managingSystem.reserveStudyRoom(1003);
                managingSystem.releaseStudyRoom(1001);
            }
            catch (StudyRoomUnavailableException exception){
                System.out.println(exception.getMessage());
            }

        });

        Thread manageThread2 = new Thread(() -> {
            try{
                managingSystem.reserveStudyRoom(1002);
                managingSystem.reserveStudyRoom(1001);
                managingSystem.releaseStudyRoom(1002);
            }
            catch (StudyRoomUnavailableException exception){
                System.out.println(exception.getMessage());
            }
        });

        manageThread1.start();
        manageThread2.start();
    }
}
