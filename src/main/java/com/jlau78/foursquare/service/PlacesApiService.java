package com.jlau78.foursquare.service;

import com.jlau78.common.exceptions.AppException;
import com.jlau78.foursquare.response.Response;

public interface PlacesApiService {

	/**
	 * Query venue by name
	 */
	public Response getVenueByName(final QueryContext queryContext) throws AppException;

	/**
	 * Get the recommended venues in the vicinity
	 * 
	 * @param queryContext
	 * @return
	 */
	public Response getRecommendedVenues(final QueryContext queryContext) throws AppException;
}
