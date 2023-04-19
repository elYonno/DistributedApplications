package hotel;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.*;

public class BookingManager implements BookingInterface {

	private final Room[] rooms;
	private Set<BookingDetail> currentBookings;

	public BookingManager() {
		this.rooms = initializeRooms();
		currentBookings = new HashSet<>();
	}

	public Set<Integer> getAllRooms() {
		Set<Integer> allRooms = new HashSet<>();
		for (Room room : rooms) {
			allRooms.add(room.getRoomNumber());
		}
		return allRooms;
	}

	public boolean isRoomAvailable(Integer roomNumber, LocalDate date) {
		Room room = getRoom(roomNumber);

		if (room == null)
			return false;
		else
			return room.available(date);
	}

	public void addBooking(BookingDetail bookingDetail) throws Exception {
		Room room = Arrays.stream(rooms)
				.filter(r -> Objects.equals(r.getRoomNumber(), bookingDetail.getRoomNumber()))
				.findFirst()
				.orElse(null);

		if (room == null)
			return;

		if (room.available(bookingDetail.getDate())) {
			room.getBookings().add(bookingDetail);
		} else {
			throw new Exception("Room is unavailable!");
		}
	}

	@Override
	public String currentCart() throws RemoteException {
		if (currentBookings.isEmpty())
			return "No bookings in this session!\n";
		else {
			StringBuilder builder = new StringBuilder();

			for (BookingDetail bookingDetail : currentBookings) {
				builder.append(bookingDetail.toString());
			}

			return builder.toString();
		}
	}

	@Override
	public String printRooms() throws RemoteException {
		StringBuilder buffer = new StringBuilder();

		for (Room room : rooms) {
			buffer.append(room.toString());
		}

		return buffer.toString();
	}

	@Override
	public AddBookingStatus addBookingDetail(BookingDetail bookingDetail) throws RemoteException {
		if (getRoom(bookingDetail.getRoomNumber()) != null) {
			if (currentBookings.add(bookingDetail))
				return AddBookingStatus.OK;
			else
				return AddBookingStatus.DUPLICATE;
		} else return AddBookingStatus.INVALID_ROOM;
	}

	/**
	 * Check all rooms are available and then add each booking detail to respective room. If any room is unavailable,
	 * does not do any booking. If booking is successful, clear currentBookings. Is synchronized to make sure no other
	 * client can book at the same time.
	 * @return True if booking is completed. False if failed.
	 */
	@Override
	public synchronized boolean bookAll() throws RemoteException {
		// check all rooms are available
		for (BookingDetail booking : currentBookings) {
			Room room = getRoom(booking.getRoomNumber());
			if (room == null || !room.available(booking.getDate()))
				return false;
		}

		// all rooms available, book all
		currentBookings.forEach(booking -> {
			Room room = getRoom(booking.getRoomNumber());
			assert (room != null);
			room.getBookings().add(booking);
		});

		currentBookings.clear();

		return true;
	}

	public Set<Integer> getAvailableRooms(LocalDate date) {
		Set<Integer> set = new HashSet<>();

		for (Room room : rooms) {
			if (room.available(date))
				set.add(room.getRoomNumber());
		}

		return set;
	}

	private Room getRoom(Integer roomNumber) {
		for (Room room : rooms) {
			if (room.getRoomNumber().equals(roomNumber))
				return room;
		}

		return null;
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
