package hotel;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface BookingInterface extends Remote {
     String printRooms() throws RemoteException;
     String currentCart() throws RemoteException;
     AddBookingStatus addBookingDetail(BookingDetail bookingDetail) throws RemoteException;
     boolean bookAll() throws RemoteException;;
}
