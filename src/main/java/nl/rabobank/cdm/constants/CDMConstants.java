package nl.rabobank.cdm.constants;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class CDMConstants {
	
	public static final Pattern PATTERN_CUSTOMER_NAME = Pattern.compile("[a-zA-Z-]+");
	public static final Pattern PATTERN_CUSTOMER_ADDRESS = Pattern.compile("[a-zA-Z0-9-., ]+");
	public static final String DATE_FORMAT = "dd-MM-yyyy";
	public static final String DATE_TIME_FORMAT = "dd-MM-yyyy hh:mm:ss";
	public static final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
	public static final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(DATE_TIME_FORMAT);

	public static final String STATUS_FAILURE = "FAILURE";
	public static final String NO_RECORD_FOUND = "No record found";
	public static final String CUSTOMER_UPDATE_OPERATION_FAILED = "Customer Update operation failed";
	public static final String CUSTOMER_SAVE_OPERATION_FAILED = "Customer Save operation failed";
	public static final String EXCEPTION_IN_PROCESSING = "Exception in Processing";
	public static final String WRONG_HTTP_METHOD = "Wrong HTTP Method used for the operation";

	public static final String IS_MANDATORY = " is mandatory";
	public static final String IS_INVALID = " is invalid";
	public static final String IS_GREATER_THAN_TODAY = " is greater than today";
	public static final String ERROR_MESSAGE_DELIMITER = ", ";

    public static final String ID = "id";
	public static final String FIRST_NAME = "firstName";
	public static final String ADDRESS = "address";
	public static final String LAST_NAME = "lastName";
	public static final String DOB = "dob";
}
