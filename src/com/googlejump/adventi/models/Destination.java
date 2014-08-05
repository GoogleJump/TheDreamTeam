package com.googlejump.adventi.models;

public class Destination {
	
	/* Member fields of an Adventi destination*/
	private String name;
	private String phoneNumber;
	private String address;
	private Integer priceLevel;
	private String url;
	
	//Do we need these for walkscore? & if so, how do we get them?
	private Double longitude;
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

	public Integer getWalkScore() {
		return walkScore;
	}

	public void setWalkScore(Integer walkScore) {
		this.walkScore = walkScore;
	}

	public String getWalkDesc() {
		return walkDesc;
	}

	public void setWalkDesc(String walkDesc) {
		this.walkDesc = walkDesc;
	}

	private Double latitude;
	// Walking or public transportation related
	private Integer walkScore;
	private String walkDesc;
	private Integer transitScore;
	
	/**
	 * Default constructor
	 */
	public Destination(){
	}
	
	/**
	 * Default constructor
	 */
	public Destination(String placeName, String placeNumber, String placeAddress, 
			Integer placePriceLevel, String placeURL){
		this.name = placeName;
		this.phoneNumber = placeNumber;
		this.address = placeAddress;
		this.priceLevel = placePriceLevel;
		this.url = placeURL;
	}
	
	public Destination(String placeName, String placeNumber, String placeAddress, 
			Integer placePriceLevel, String placeURL, Double longit, Double latit,
			Integer placeWalkScore, String placeWalkDesc ){
		this.name = placeName;
		this.phoneNumber = placeNumber;
		this.address = placeAddress;
		this.priceLevel = placePriceLevel;
		this.url = placeURL;
		this.longitude = longit;
		this.latitude = latit;
		this.walkScore = placeWalkScore;
		this.walkDesc = placeWalkDesc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getPriceLevel() {
		return priceLevel;
	}

	public void setPriceLevel(Integer priceLevel) {
		this.priceLevel = priceLevel;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	

}
