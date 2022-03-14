package nl.rabobank.cdm;

import nl.rabobank.cdm.data.entities.Customer;
import nl.rabobank.cdm.dto.CustomerDTO;
import nl.rabobank.cdm.util.CustomerUtil;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class CDMApplication {

    public static void main(String[] args) {
        SpringApplication.run(CDMApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        Converter<Date, String> dateToStrConv = ctx -> CustomerUtil.dateToString(ctx.getSource());
        Converter<Date, String> dateTimeToStrConv = ctx -> CustomerUtil.dateTimeToString(ctx.getSource());
        Converter<String, Date> strToDateConv = ctx -> CustomerUtil.stringToDate(ctx.getSource());

        TypeMap<Customer, CustomerDTO> entityToDtoTypeMap = modelMapper.createTypeMap(Customer.class,
                CustomerDTO.class);
        entityToDtoTypeMap
                .addMappings(mapper -> {
                    mapper.using(dateToStrConv).map(Customer::getDob, CustomerDTO::setDob);
                    mapper.using(dateTimeToStrConv).map(Customer::getCreatedDate, CustomerDTO::setCreatedDate);
                    mapper.using(dateTimeToStrConv).map(Customer::getUpdatedDate, CustomerDTO::setUpdatedDate);
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
