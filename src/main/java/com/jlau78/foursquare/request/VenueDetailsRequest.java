package com.jlau78.foursquare.request;


import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Places API Venue Details
 */
@RequiredArgsConstructor
@Data
public class VenueDetailsRequest extends BaseRequest {

	@NonNull
	private String venueId;
	
}
