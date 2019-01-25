package com.jlau78.foursquare.request;

import lombok.Data;

/**
 * Places API Venue queries
 */
@Data
public class VenueRequest extends BaseRequest {

	private String near;
	private String query;
	private String section;
	private String radius;
	private String intent;
	
}
