package by.bsuir.matys_rozin_zarudny.layer.service;

import java.util.List;
import javax.ejb.Local;

import by.bsuir.matys_rozin_zarudny.common.exceptions.AccountAuthenticationFailed;
import by.bsuir.matys_rozin_zarudny.common.exceptions.BookingNotFoundException;
import by.bsuir.matys_rozin_zarudny.common.exceptions.IllegalBookingStateException;
import by.bsuir.matys_rozin_zarudny.common.exceptions.InvalidBookingException;
import by.bsuir.matys_rozin_zarudny.common.exceptions.InvalidGoogleApiResponseException;
import by.bsuir.matys_rozin_zarudny.common.exceptions.InvalidLocationException;
import by.bsuir.matys_rozin_zarudny.common.exceptions.RouteNotFoundException;
import by.bsuir.matys_rozin_zarudny.common.exceptions.TaxiNotFoundException;
import by.bsuir.matys_rozin_zarudny.layer.persistence.dto.BookingDto;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.booking.Booking;

/**
 * An interface for defining and enforcing operations needed for the Booking
 * Service class.
 */
@Local
public interface BookingFacade {

	/**
	 * Find booking by id.
	 *
	 * @param id
	 *            id of taxi.
	 * @return booking or null if not found.
	 */
	public Booking findBooking(Long id);

	/**
	 * Return booking if booking id matches the authenticated user, else null.
	 *
	 * @param id
	 *            id of booking.
	 * @param username
	 *            username of authenticated user.
	 * @return return booking if booking id matches the authenticated user, else
	 *         null.
	 */
	public Booking findBookingForUser(Long id, String username);

	/**
	 * Book a taxi.
	 *
	 * @param booking
	 *            booking data transfer object. Requires: - username username of
	 *            passenger. - numberPassengers number of passengers. - pickup
	 *            location - destination location
	 *
	 * @return the created booking.
	 * @throws InvalidLocationException
	 *             invalid location/not found.
	 * @throws RouteNotFoundException
	 *             route not found.
	 * @throws InvalidGoogleApiResponseException
	 *             unable to parse Google API response.
	 * @throws InvalidBookingException
	 *             invalid booking e.g. user has active bookings.
	 * @throws AccountAuthenticationFailed
	 *             if account authentication fails.
	 */
	public Booking makeBooking(BookingDto booking) throws InvalidLocationException, AccountAuthenticationFailed,
			RouteNotFoundException, InvalidGoogleApiResponseException, InvalidBookingException;

	/**
	 * Update the specified booking.
	 *
	 * @param booking
	 *            update booking.
	 */
	public void updateBooking(Booking booking);

	/**
	 * Find all bookings in awaiting taxi to be dispatched state.
	 *
	 * @return all bookings in awaiting taxi to be dispatched state.
	 */
	public List<Booking> findBookingsInAwaitingTaxiDispatchState();

	/**
	 * A collections of bookings. If passenger display booking history. If
	 * driver display job history.
	 *
	 * @param username
	 *            username.
	 * @return booking history.
	 */
	public List<Booking> findBookingHistory(String username);

	/**
	 * Return most recent active booking for a user.
	 *
	 * @param username
	 *            username of user.
	 * @return active booking for a user.
	 * @throws IllegalArgumentException
	 *             if username is null;
	 */
	public Booking checkActiveBooking(String username);

	/**
	 * Accept a taxi booking.
	 *
	 * @param username
	 *            of taxi driver.
	 * @param bookingId
	 *            booking id.
	 * @throws TaxiNotFoundException
	 *             taxi not found.
	 * @throws BookingNotFoundException
	 *             booking not found.
	 * @throws IllegalBookingStateException
	 *             if booking in an illegal state.
	 */
	public void acceptBooking(String username, long bookingId)
			throws TaxiNotFoundException, BookingNotFoundException, IllegalBookingStateException;

	/**
	 * Drop of passenger.
	 *
	 * @param username
	 *            username of the taxi driver.
	 * @param bookingId
	 *            id of booking to update.
	 * @param timestamp
	 *            timestamp of update.
	 * @throws BookingNotFoundException
	 *             booking not found.
	 * @throws TaxiNotFoundException
	 *             username for taxi not found.
	 * @throws IllegalBookingStateException
	 *             if booking in an illegal state.
	 */
	public void pickUpPassenger(String username, long bookingId, long timestamp)
			throws BookingNotFoundException, TaxiNotFoundException, IllegalBookingStateException;

	/**
	 * Pink up passenger.
	 *
	 * @param username
	 *            of taxi driver.
	 * @param bookingId
	 *            id of booking to update.
	 * @param timestamp
	 *            timestamp of update.
	 * @throws BookingNotFoundException
	 *             booking not found.
	 * @throws TaxiNotFoundException
	 *             username for taxi not found.
	 * @throws IllegalBookingStateException
	 *             if booking in an illegal state.
	 */
	public void dropOffPassenger(String username, long bookingId, long timestamp)
			throws BookingNotFoundException, TaxiNotFoundException, IllegalBookingStateException;

	/**
	 * Cancel a booking.
	 *
	 * @param username
	 *            username of person requesting cancellation of booking.
	 * @param bookingId
	 *            booking to cancel.
	 * @throws BookingNotFoundException
	 *             booking not found.
	 * @throws AccountAuthenticationFailed
	 *             user does not have permission to cancel booking.
	 * @throws IllegalBookingStateException
	 *             if booking in an illegal state.
	 */
	public void cancelBooking(String username, long bookingId)
			throws BookingNotFoundException, AccountAuthenticationFailed, IllegalBookingStateException;

	/**
	 * Allocate booking to taxi.
	 *
	 * @param booking
	 */
	public void allocateTaxi(Booking booking);

	/**
	 * On booking completion/cancel reallocate taxis.
	 */
	public void allocateTaxi();
}
