package nl.rabobank.cdm.validator;

import lombok.extern.slf4j.Slf4j;
import nl.rabobank.cdm.constants.CDMConstants;
import nl.rabobank.cdm.exception.CDMValidationException;
import nl.rabobank.cdm.dto.CustomerDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static nl.rabobank.cdm.constants.CDMConstants.*;
import static nl.rabobank.cdm.util.CustomerUtil.*;

@Component
@Slf4j
public class CustomerValidator {

	/**
	 * Validates input customer detail request fiels. (firstName, lastName, address, dateOfBirth)
	 * and error message for each field will be appended to form a single error message.
	 *
	 * @param customerDTO
	 * @throws CDMValidationException
	 */
	public void validateCustomerDetails(CustomerDTO customerDTO) throws CDMValidationException{
		String errorMessage = null;
		List<String> errorMessageList = new ArrayList<>();
		
		validateField(CDMConstants.FIRST_NAME, customerDTO.getFirstName(), PATTERN_CUSTOMER_NAME, errorMessageList);
		validateField(LAST_NAME, customerDTO.getLastName(), PATTERN_CUSTOMER_NAME, errorMessageList);
		validateField(CDMConstants.ADDRESS, customerDTO.getAddress(), PATTERN_CUSTOMER_ADDRESS, errorMessageList);
		validateField(DOB, customerDTO.getDob(), null, errorMessageList);
		
		if (errorMessageList.size() > 0) {
			errorMessage= String.join(ERROR_MESSAGE_DELIMITER, errorMessageList);
			log.error("Validation error in the request. Error message: {}", errorMessage);
			throw new CDMValidationException(errorMessage);
		}
	}

	/**
	 * Validates a given field value against the expected regex pattern.
	 *
	 * @param fieldName
	 * @param fieldValue
	 * @param pattern
	 * @param errorMessageList
	 */
	private void validateField(String fieldName, String fieldValue, Pattern pattern, List<String> errorMessageList) {
		if (isNullOrEmpty(fieldValue)){
			errorMessageList.add(fieldName + IS_MANDATORY);
		}
		else if (DOB.equals(fieldName)){
			if (stringToDate(fieldValue) == null) {
				errorMessageList.add(fieldName + IS_INVALID);
			}
			else if (isDateGreaterThanToday(fieldValue) == true){
				errorMessageList.add(fieldName + IS_GREATER_THAN_TODAY);
			}
		}
		else if (isNotValidPattern(fieldValue, pattern)){
			errorMessageList.add(fieldName + IS_INVALID);
		}
	}
}
