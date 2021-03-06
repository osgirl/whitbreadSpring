package com.jlau78.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jlau78.common.exceptions.AppException;
import com.jlau78.common.exceptions.ExceptionHandler;
import com.jlau78.foursquare.request.VenueDetailsRequest;
import com.jlau78.foursquare.request.VenueRequest;
import com.jlau78.foursquare.response.venue.SearchResponse;
import com.jlau78.foursquare.response.venue.Venue;
import com.jlau78.foursquare.response.venue.VenueDetailRS;
import com.jlau78.foursquare.response.venue.DetailResponse;
import com.jlau78.foursquare.response.venue.VenueSearchRS;
import com.jlau78.foursquare.service.VenueDetailsCall;
import com.jlau78.foursquare.service.VenueRecommendationCall;
import com.jlau78.foursquare.service.VenueSearchCall;
import com.jlau78.handler.VenueDetailsMulitQueryHandler;
import com.jlau78.handler.VenueSearchCallHandler;

import feign.FeignException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(tags = "VenueQueryController")
@RestController
@RequestMapping("/venue")
public class VenueController {
	
	@Getter
	@Autowired
	private VenueSearchCall venueSearchCall;
	  
  @Getter
  @Autowired
  VenueDetailsMulitQueryHandler venueDetailsHandler;
  
  @Getter
  @Autowired
  VenueSearchCallHandler venueSearchCallHandler;

	@ApiOperation("Get Venue by location name and optional query")
	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SearchResponse> getVenueByLocationName(@RequestParam(value = "venue") String venue,
											@RequestParam(value = "query", required = false) String query)    
	{
		String errorMsg = "";
		
		VenueRequest rq = new VenueRequest(venue);
		rq.setQuery(query);
		VenueSearchRS searchResponse = getVenueSearchCallHandler().handle(rq);
			
		if (StringUtils.isNotEmpty(errorMsg)) {
			return ExceptionHandler.handleSearchError(errorMsg);
		}
		return new ResponseEntity<SearchResponse>(searchResponse.getResponse(), HttpStatus.OK);
	}
	  
  @ApiOperation("Search for Venue and get all the venues details. Limit to 4 results due to allowed quota")
  @RequestMapping(value = "/searchAndDetail", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<DetailResponse>> venueSearchAndDetail(
  								@RequestParam(value = "venue") String venue,
									@RequestParam(value = "query", required = false) String query)
  {
		VenueRequest rq = new VenueRequest(venue);
		rq.setLimit("4");
  	VenueSearchRS searchResponse = getVenueSearchCallHandler().handle(rq);
  	
  	List<VenueDetailRS> detailsResponses = getVenueDetailsHandler().handle(getVenueIds(searchResponse));
  	
  	List<DetailResponse> rs = detailsResponses.stream()
  									.map(ds -> ds.getResponse())
  									.collect(Collectors.toList());
  	
  	return new ResponseEntity<List<DetailResponse>>(rs, HttpStatus.OK);
  }
 
	private List<String> getVenueIds(final VenueSearchRS response) {
		List<String> venueIds = null;
		if (response != null && response.getResponse() != null) {
			SearchResponse r = response.getResponse();
			List<Venue> venues = r.venues;
			
			venueIds = venues.stream()
												.map(v -> v.id)
												.collect(Collectors.toList());
		}
		return venueIds; 
	}
	

}
