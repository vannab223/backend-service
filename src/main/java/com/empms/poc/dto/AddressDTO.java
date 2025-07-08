package com.empms.poc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

	private String street;
	private String city;
	private String state;
	private String country;
	private String zipCode;

}
