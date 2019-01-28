package com.jlau78.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlau78.common.exceptions.AppException;
import com.jlau78.foursquare.request.VenueRequest;
import com.jlau78.foursquare.response.venue.VenueSearchRS;
import com.jlau78.foursquare.service.VenueSearchCall;

import feign.FeignException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VenueSearchCallHandler implements Handler<VenueRequest, VenueSearchRS> {
	
	@Getter
	@Autowired
	VenueSearchCall apiCall;

	@Override
	public VenueSearchRS handle(VenueRequest inputs)  {
		VenueSearchRS details = new VenueSearchRS();
		
		if (inputs != null) {
			details = doCallApi(inputs);
		}
		return details;
	}
	
	private VenueSearchRS doCallApi(final VenueRequest input) {
		String errorMsg = "";
		VenueSearchRS rs = null;
    try {

      rs = getApiCall().call(input);

    } catch (FeignException e) {
      log.error("Fail connecting to the foursquare api: {0}", e.getMessage());
      errorMsg = e.getMessage();
    } catch (AppException e) {
      log.error("Fail performing a Venue Search query: {0}", e.getMessage());
      errorMsg = e.getMessage();
    }

    return rs;
	}
}
