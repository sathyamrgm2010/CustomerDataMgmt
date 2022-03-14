package nl.rabobank.cdm.controllers;

import io.swagger.annotations.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import nl.rabobank.cdm.dto.CustomerDTO;
import nl.rabobank.cdm.exception.CDMApiException;
import nl.rabobank.cdm.service.CustomerService;
import nl.rabobank.cdm.util.CustomerPojoUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static nl.rabobank.cdm.config.SwaggerConfiguration.CUSTOMER_TAG;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Data
@Api(tags = CUSTOMER_TAG)
public class CustomerController {

	private final CustomerService customerService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping(value = "/customer")
	@ApiOperation("List all the customer details.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "The customer details have been listed.")
	})
	public List<CustomerDTO> getCustomers() throws CDMApiException {
		return CustomerPojoUtil.mapList(customerService.getCustomers(), CustomerDTO.class, modelMapper);
	}

	@ApiOperation("Get a customer detail by passing customer id.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "The customer detail has been found."),
			@ApiResponse(code = 404, message = "The customer detail was not found.")
	})
	@GetMapping(value = "/customer/id/{id}")
	public CustomerDTO getCustomerById(@ApiParam(name = "id", value = "The id of the customer") @PathVariable String id) throws CDMApiException {
		return modelMapper.map(customerService.getCustomerById(id), CustomerDTO.class);
	}

	@ApiOperation("Search customers by FirstName or LastName or Both.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "The customer detail search results have been listed."),
			@ApiResponse(code = 404, message = "The customer detail was not found.")
	})
	@GetMapping(value = "/customer/search")
	public List<CustomerDTO> getCustomerByFieldName(@RequestParam(required = false, defaultValue="") String firstName,
													@RequestParam(required = false, defaultValue="") String lastName) throws CDMApiException {

		return CustomerPojoUtil.mapList(customerService.getCustomerByFieldName(firstName, lastName), CustomerDTO.class, modelMapper);
	}

	@ApiOperation("Create new customer detail")
	@ApiResponses({
			@ApiResponse(code = 201, message = "The customer details have been created."),
			@ApiResponse(code = 400, message = "The customer details request body missing required fields/illegal values.")
	})
	@PostMapping(value = "/customer")
	@ResponseStatus(HttpStatus.CREATED)
	public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) throws CDMApiException {
		return modelMapper.map(customerService.saveCustomer(customerDTO), CustomerDTO.class);

	}

	@ApiOperation("Update customer details")
	@ApiResponses({
			@ApiResponse(code = 200, message = "The customer details have been updated."),
			@ApiResponse(code = 404, message = "The customer record doesn't exist"),
			@ApiResponse(code = 400, message = "The customer details request body missing required fields/illegal values.")
	})
	@PutMapping(value = "/customer/id/{id}")
	public CustomerDTO updateCustomer(@ApiParam(name = "id", value = "The id of the customer") @PathVariable String id, @RequestBody CustomerDTO customerDTO) throws CDMApiException {
		return modelMapper.map(customerService.updateCustomer(id, customerDTO), CustomerDTO.class);
	}

}
