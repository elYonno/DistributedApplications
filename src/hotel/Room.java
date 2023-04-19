package hotel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Room {

	private Integer roomNumber;
	private List<BookingDetail> bookings;

	public Room(Integer roomNumber) {
		this.roomNumber = roomNumber;
		bookings = new ArrayList<>();
	}

	public synchronized Integer getRoomNumber() {
		return roomNumber;
	}

	public synchronized void setRoomNumber(Integer roomNumber) {
		this.roomNumber = roomNumber;
	}

	public synchronized List<BookingDetail> getBookings() {
		return bookings;
	}

	public synchronized void setBookings(List<BookingDetail> bookings) {
		this.bookings = bookings;
	}

	public synchronized boolean available(LocalDate date) {
		for (BookingDetail bookingDetail : getBookings()) {
			if (bookingDetail.getDate().equals(date))
				return false;
		}
		return true;
	}

	@Override
	public synchronized String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("Number = ").append(roomNumber).append("\n");

		if (!bookings.isEmpty()) {
			buffer.append("\tBookings:\n");

			for (BookingDetail booking : bookings) {
				buffer.append("\t");
				buffer.append(booking.toString());
			}
		}

		return buffer.toString();
	}
}