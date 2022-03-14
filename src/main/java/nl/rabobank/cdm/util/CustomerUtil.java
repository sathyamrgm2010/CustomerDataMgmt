package nl.rabobank.cdm.util;

import lombok.extern.slf4j.Slf4j;

import static nl.rabobank.cdm.constants.CDMConstants.dateFormatter;
import static nl.rabobank.cdm.constants.CDMConstants.dateTimeFormatter;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class CustomerUtil {
	/**
	 * Validate if the given String is in given regexPattern.
	 * @param fieldvalue string inpt
	 * @param regexPattern pattern to be verified.
	 * @return
	 */
	public static boolean isNotValidPattern(String fieldvalue, Pattern regexPattern) {
		Matcher matcher = regexPattern.matcher(fieldvalue);
		if (!matcher.matches()) {
			return true;
		}
		return false;
	}
	
	public static boolean isNullOrEmpty(String fieldvalue) {
		if (fieldvalue == null || "".equals(fieldvalue)) {
			return true;
		}
		return false;
	}

	/**
	 *
	 * @param fieldvalue
	 * @return
	 */
	public static Date stringToDate(String fieldvalue) {
		Date convertedDate = null;
		if (!isNullOrEmpty(fieldvalue)) {
			try {
				dateFormatter.setLenient(false);
				convertedDate = dateFormatter.parse(fieldvalue);
			} catch (ParseException e) {
				log.error("Could not parse supplied Date");
			}
		}
		return convertedDate;
	}

	/**
	 * Find if the given date is not a future date.
	 * @param dob
	 * @return
	 */
	public static boolean isDateGreaterThanToday(String dob) {
		LocalDate currDate = LocalDate.now();
		LocalDate dobDate = stringToDate(dob).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		if (currDate.compareTo(dobDate) >= 0){
			return false;
		}
		return true;
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		String dateToString = null;
		try {
			dateToString = dateFormatter.format(date);
		} catch (Exception e) {
			log.error("Could not format Date into format");
		}
		return dateToString;
	}

	/**
	 * Convert Date time to String.
	 * @param dateTime
	 * @return
	 */
	public static String dateTimeToString(Date dateTime) {
		String dateTimeToString = null;
		try {
			if (dateTime != null) {
				dateTimeToString = dateTimeFormatter.format(dateTime);
			}
		} catch (Exception e) {
			log.error("Could not format DateTime into format");
		}
		return dateTimeToString;
	}

	/**
	 * Calculates age from the given date of birth.
	 * @param dobStr
	 * @return
	 */
	public static Integer calculateAge(Date dob) {
		Integer age = null;
		if (dob != null) {
			LocalDate dobDate = dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(); 
			LocalDate currDate = LocalDate.now();  
			age = Period.between(dobDate, currDate).getYears();  
		}
		return age;
	}
	
}
