package com.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentResponse {
	
	private String studentId;
	private String name;
	private String email;
	private Integer age;

}
