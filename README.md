Customer Data Management API
==============================================================

Swagger URL
--------------

http://localhost:8080/swagger-ui.html


API Operation List
-----

<table border="1"> 
  <tr>
    <th>Options
    </th>
    <th>Request URL
    </th>
    <th>Request Method
    </th>
    <th>Sample Request
    </th>
    <th>Sample Response
    </th>
  </tr>
  <tr>
    <td>Get all customers
    </td>
    <td>/api/customer
    </td>
    <td>GET
    </td>
    <td>NA
    </td>
    <td>[
  {
      "id": "123e4567-e89b-12d3-a456-426614174000", <br/>
    "firstName": "Barrett",<br/>
    "lastName": "Velez",<br/>
    "address": "4747 Butternut",<br/>
    "dob": "17-12-2000",<br/>
    "age": "21",<br/>
    "createdDate": "13-03-2022 09:08:09"<br/>
  }
]
    </td>
  </tr>
  
  <tr>
    <td>Get customer by Id
    </td>
    <td>/api/customer/id/{id}
    </td>
    <td>GET
    </td>
    <td>NA
    </td>
    <td>
  {
      "id": "123e4567-e89b-12d3-a456-426614174000", <br/>
    "firstName": "Barrett",<br/>
    "lastName": "Velez",<br/>
    "address": "4747 Butternut",<br/>
    "dob": "17-12-2000",<br/>
    "age": "21",<br/>
    "createdDate": "13-03-2022 09:08:09"<br/>
  }
    </td>
  </tr>
  <tr>
    <td>Get customer by Name
    </td>
    <td>/api/customer/search?firstName={firstName}&lastName={lastName}
    </td>
    <td>GET
    </td>
    <td>NA
    </td>
    <td>
  [{<br/>
      "id": "123e4567-e89b-12d3-a456-426614174000", <br/>
    "firstName": "Barrett",<br/>
    "lastName": "Velez",<br/>
    "address": "4747 Butternut",<br/>
    "dob": "17-12-2000",<br/>
    "age": "21",<br/>
    "createdDate": "13-03-2022 09:08:09"<br/>
  }]
    </td>
  </tr>
  <tr>
    <td>Save Customer
    </td>
    <td>/api/customer
    </td>
    <td>POST
    </td>
    <td>{<br/>
    "firstName": "Mark",<br/>
    "lastName": "Evans",<br/>
    "address": "1212 Water street",<br/>
    "dob": "17-12-2000"<br/>
}
    </td>
    <td>
  [{<br/>
      "id": "123e4567-e89b-12d3-a456-426614174000", <br/>
    "firstName": "Barrett",<br/>
    "lastName": "Velez",<br/>
    "address": "4747 Butternut",<br/>
    "dob": "17-12-2000",<br/>
    "age": "21",<br/>
    "createdDate": "13-03-2022 09:08:09"<br/>
  }]
    </td>
  </tr>
  <tr>
    <td>Update Customer
    </td>
    <td>/api/customer/id/{id}
    </td>
    <td>PUT
    </td>
    <td>{<br/>
    "firstName": "Mark",<br/>
    "lastName": "Evans",<br/>
    "address": "1212 Water street",<br/>
    "dob": "17-12-2000"<br/>
}
    </td>
    <td>
  [{<br/>
      "id": "123e4567-e89b-12d3-a456-426614174000", <br/>
    "firstName": "Barrett",<br/>
    "lastName": "Velez",<br/>
    "address": "4747 Butternut",<br/>
    "dob": "17-12-2000",<br/>
    "age": "21",<br/>
    "createdDate": "13-03-2022 09:08:09",<br/>
      "updatedDate": "13-03-2022 09:20:09" <br/>
  }]
    </td>
  </tr>
 
</table
