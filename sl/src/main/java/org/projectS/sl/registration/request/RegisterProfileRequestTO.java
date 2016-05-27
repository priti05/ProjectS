package org.projectS.sl.registration.request;

import org.projectS.sl.enumHelper.Sex;

public final class RegisterProfileRequestTO {
	
	
	private String firstName;
	private String lastName;
	private String middleName;
	private String nickName;
	private String employer;
	private String phoneNumber;
	private String email;
	private Integer occupationId;
	private String userName;
	private String password;
	private Sex sex;
	
	
	public RegisterProfileRequestTO(String firstName,String lastName, String phoneNumber, String email, String userName, String password){
		this.firstName = firstName;
		this.lastName =lastName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.userName = userName;
		this.password = password;
	}


	public String getMiddleName() {
		return middleName;
	}


	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}


	public String getNickName() {
		return nickName;
	}


	public void setNickName(String nickName) {
		this.nickName = nickName;
	}


	public String getEmployer() {
		return employer;
	}


	public void setEmployer(String employer) {
		this.employer = employer;
	}


	public Integer getOccupationId() {
		return occupationId;
	}


	public void setOccupationId(Integer occupationId) {
		this.occupationId = occupationId;
	}


	public Sex getSex() {
		return sex;
	}


	public void setSex(Sex sex) {
		this.sex = sex;
	}


	public String getFirstName() {
		return firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public String getEmail() {
		return email;
	}


	public String getUserName() {
		return userName;
	}


	public String getPassword() {
		return password;
	}
	
	

}
