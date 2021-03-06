package com.googlejump.adventi.models;

import java.io.Serializable;

/**
 * Model to contain information about Adventi Users
 * 
 * @author Aieswarya
 *
 */
public class AdventiUser implements Serializable {
	
	// Preferred method of transportation for the user
	private Vehicle methodOfTransport = Vehicle.CAR;
	
	public enum Vehicle{CAR, BICYCLE, WALK, BUS}
	
	// Location of user when they search for Adventis
	private Double longitude;
	private Double latitude;
	
	//Constructors
	public AdventiUser(){}
	
	public AdventiUser(Vehicle transportChoice){
		methodOfTransport = transportChoice;
	}
	
	public Vehicle getMethodOfTransport() {
		return methodOfTransport;
	}
	public void setMethodOfTransport(Vehicle methodOfTransport) {
		this.methodOfTransport = methodOfTransport;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	
	
	

}
