package nl.rabobank.cdm.util;

import nl.rabobank.cdm.data.entities.Customer;
import nl.rabobank.cdm.dto.CustomerDTO;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;

import java.util.Date;

public class TestUtil {

    public static ModelMapper getModelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        Converter<Date, String> dateToStrConv = ctx -> CustomerUtil.dateToString(ctx.getSource());
        Converter<String, Date> strToDateConv = ctx -> CustomerUtil.stringToDate(ctx.getSource());

        TypeMap<Customer, CustomerDTO> entityToDtoTypeMap = modelMapper.createTypeMap(Customer.class,
                CustomerDTO.class);
        entityToDtoTypeMap
                .addMappings(mapper -> {
                    mapper.using(dateToStrConv).map(Customer::getDob, CustomerDTO::setDob);
                    mapper.using(dateToStrConv).map(Customer::getCreatedDate, CustomerDTO::setCreatedDate);
                    mapper.using(dateToStrConv).map(Customer::getUpdatedDate, CustomerDTO::setUpdatedDate);
                });

        TypeMap<CustomerDTO, Customer> dtoToEntTypeMap = modelMapper.createTypeMap(CustomerDTO.class,
                Customer.class);
        dtoToEntTypeMap
                .addMappings(mapper -> {
                    mapper.using(strToDateConv).map(CustomerDTO::getDob, Customer::setDob);
                    mapper.skip(Customer::setCreatedDate);
                    mapper.skip(Customer::setUpdatedDate);
                });
        return modelMapper;
    }
}
