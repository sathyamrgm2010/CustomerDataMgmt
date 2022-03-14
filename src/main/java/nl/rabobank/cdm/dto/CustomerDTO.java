package nl.rabobank.cdm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDTO {
   
    String id;
    String firstName;
    String lastName;
    String address;
    String dob;
    String age;
    String createdDate;
    String updatedDate;
}
