package database;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import entity.Payment;
import entity.Reservation;

/**
 * DB Class for data access for payment controller.
 * 
 * @author Lau Geok Teng
 * @version 1.0
 * @since 2016-03-22
 */

public class PaymentDB {

	/**
	 * Delimiter for data in text file.
	 */
	private static final String SEPARATOR = "|";

	/**
	 * Reading of payment data from text file.
	 * 
	 * @param filename
	 *            To specify the name of text file to read.
	 * @return arraylist the list of payment data taken from the text file.
	 */
	public ArrayList readPayment(String filename) throws IOException {

		DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		// Read String from text file
		ArrayList stringArray = (ArrayList) UtilityDB.read(filename);
		ArrayList alr = new ArrayList(); // To store Reservation data

		for (int i = 0; i < stringArray.size(); i++) {
			String st = (String) stringArray.get(i);

			// Get individual 'fields' of the string separated by SEPARATOR
			StringTokenizer star = new StringTokenizer(st, SEPARATOR); // Pass in the string to the string tokenizer using delimiter "|"

			Date date = null;
			try {
				date = formatter.parse(star.nextToken().trim());
			} catch (ParseException e) {

			}

			// Reservation ID
			Reservation reservation = new Reservation();
			reservation.setReservationID(star.nextToken().trim());

			double charges = (Double.parseDouble(star.nextToken().trim()));
			double tax = (Double.parseDouble(star.nextToken().trim()));

			double discount = (Double.parseDouble(star.nextToken().trim()));
			double total = (Double.parseDouble(star.nextToken().trim()));

			// Create payment object from file data
			Payment payment = new Payment(reservation, charges, tax, discount, total, date);

			// Add to payment list
			alr.add(payment);
		}
		return alr;
	}

	/**
	 * Saving of payment data to the text file.
	 * 
	 * @param filename
	 *            To specify the name of text file to read.
	 * @param al
	 *            The list of payment data to store into the text file.
	 */
	public void savePayment(String filename, List al) throws IOException {

		DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		List alw = new ArrayList();

		for (int i = 0; i < al.size(); i++) {
			Payment payment = (Payment) al.get(i);
			StringBuilder st = new StringBuilder();

			st.append(formatter.format(payment.getDate()));
			st.append(SEPARATOR);

			// Reservation ID
			st.append(payment.getReservation().getReservationID().trim());

			st.append(SEPARATOR);
			st.append(payment.getCharges());
			st.append(SEPARATOR);
			st.append(payment.getTax());
			st.append(SEPARATOR);
			st.append(payment.getDiscount());
			st.append(SEPARATOR);
			st.append(payment.getTotal());

			alw.add(st.toString());
		}
		UtilityDB.write(filename, alw);
	}
}