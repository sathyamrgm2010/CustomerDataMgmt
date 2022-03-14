package nl.rabobank.cdm.util;

import org.junit.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerUtilTest {
	
	@Test
	public void test_is_null_or_empty_null_value(){ assertEquals(true, CustomerUtil.isNullOrEmpty(null)); }
	
	@Test
	public void test_is_null_or_empty_empty_value(){ assertEquals(true, CustomerUtil.isNullOrEmpty("")); 	}
	
	@Test
	public void test_is_null_or_empty_valid_value(){ assertEquals(false, CustomerUtil.isNullOrEmpty("test")); }

	@Test
	public void test_string_to_date_invalid_value(){ assertNull(CustomerUtil.stringToDate("30-02-2000")); }
	
	@Test
	public void test_string_to_date_valid_value(){ assertNotNull(CustomerUtil.stringToDate("29-02-2000"));	}
	
	@Test
	public void test_date_to_string_valid_value(){ assertNotNull(CustomerUtil.dateToString(new Date()));}

	@Test
	public void test_datetime_to_string_valid_value(){ assertNotNull(CustomerUtil.dateTimeToString(new Date())); }

	@Test
	public void test_dob_greater_than_today_true(){ assertTrue(CustomerUtil.isDateGreaterThanToday("27-02-2023"));	}

	@Test
	public void test_dob_greater_than_today_false(){ assertFalse(CustomerUtil.isDateGreaterThanToday("27-02-2022")); }

	@Test
	public void test_calc_age(){ assertEquals(22, CustomerUtil.calculateAge(CustomerUtil.stringToDate("01-01-2000"))); }
	
}
