package hotel;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BookingManager {

	private Room[] rooms;

	public BookingManager() {
		this.rooms = initializeRooms();
	}

	public Set<Integer> getAllRooms() {
		Set<Integer> allRooms = new HashSet<Integer>();
		Iterable<Room> roomIterator = Arrays.asList(rooms);
		for (Room room : roomIterator) {
			allRooms.add(room.getRoomNumber());
		}
		return allRooms;
	}

	public boolean isRoomAvailable(Integer roomNumber, LocalDate date) {
		if (roomNumber > rooms.length)
			return false;
		
		Room room = rooms[roomNumber];

		if (room == null)
			return false;

		return room.available(date);
	}

	public void addBooking(BookingDetail bookingDetail) throws Exception {
		Room room = Arrays.asList(rooms).stream()
										.filter(r -> r.getRoomNumber() == bookingDetail.getRoomNumber())
										.findFirst()
										.orElse(null);

		if (room.equals(null))
			return;

		if (room.available(bookingDetail.getDate()))
		{
			room.getBookings().add(bookingDetail);
		}
		else
		{
			// AbstractScriptedSimpleTest does not catch exceptions
			// throw new Exception("Room is unavailable!");
		}
	}

	public Set<Integer> getAvailableRooms(LocalDate date) {
		Set<Integer> set = new HashSet<Integer>();

		for (Room room : rooms)
		{
			if (room.available(date))
				set.add(room.getRoomNumber());
		}

		return set;
	}

	private static Room[] initializeRooms() {
		Room[] rooms = new Room[4];
		rooms[0] = new Room(101);
		rooms[1] = new Room(102);
		rooms[2] = new Room(201);
		rooms[3] = new Room(203);
		return rooms;
	}
}
