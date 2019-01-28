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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jlau78.common.exceptions.AppException;
import com.jlau78.common.exceptions.ExceptionHandler;
import com.jlau78.foursquare.request.VenueDetailsRequest;
import com.jlau78.foursquare.request.VenueRequest;
import com.jlau78.foursquare.response.venue.Location_;
import com.jlau78.foursquare.response.venue.SearchResponse;
import com.jlau78.foursquare.response.venue.Venue;
import com.jlau78.foursquare.response.venue.VenueDetailRS;
import com.jlau78.foursquare.response.venue.DetailResponse;
import com.jlau78.foursquare.response.venue.VenueSearchRS;
import com.jlau78.foursquare.service.VenueDetailsCall;
import com.jlau78.foursquare.service.VenueRecommendationCall;
import com.jlau78.foursquare.service.VenueSearchCall;
import com.jlau78.handler.VenueDetailsQueryHandler;
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
	private VenueRecommendationCall venueRecommendationCall;
	
  @Getter
  @Autowired
  private VenueDetailsCall venueDetailsCall;

	@ApiOperation("Get Venue by location name")
	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SearchResponse> getVenueByLocationName(@RequestParam(value = "venue") String venue,
											@RequestParam(value = "query", required = false) String query)    
	{
		String errorMsg = "";
		VenueSearchRS rs = null;
		try {
			VenueRequest rq = new VenueRequest(venue);
			rq.setQuery(query);

			rs = getVenueSearchCall().call(rq);

		} catch (FeignException e) {
			log.error("Fail connecting to the foursquare api: {0}", e.getMessage());
			errorMsg = getErrorMessage(e);
		} catch (AppException e) {
			log.error("Fail performing a Venue Search query: {0}", e.getMessage());
			errorMsg = getErrorMessage(e);
		}
			
		if (StringUtils.isNotEmpty(errorMsg)) {
			return ExceptionHandler.handleSearchError(errorMsg);
		}
		return new ResponseEntity<SearchResponse>(rs.getResponse(), HttpStatus.OK);
	}
	
	@ApiOperation("Get Venue by location name")
	@RequestMapping(value = "/recommend", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SearchResponse> getVenueRecommendations(@RequestParam(value = "venue") String venue,
											@RequestParam(value = "query", required = false) String query,
											@RequestParam(value = "section", required = false) String section,
											@RequestParam(value = "radius", required = false) String radius)    
	{
		String errorMsg = "";
		VenueSearchRS rs = null;
		try {
			VenueRequest rq = new VenueRequest(venue);
			rq.setQuery(query);
			rq.setSection(section);
			rq.setRadius(radius);

			rs = getVenueRecommendationCall().call(rq);

		} catch (FeignException e) {
			log.error("Fail connecting to the foursquare api: {0}", e.getMessage());
			errorMsg = getErrorMessage(e);
		} catch (AppException e) {
			log.error("Fail performing a Venue Search query: {0}", e.getMessage());
			errorMsg = getErrorMessage(e);
		}
			
		if (StringUtils.isNotEmpty(errorMsg)) {
			return ExceptionHandler.handleSearchError(errorMsg);
		}
		return new ResponseEntity<SearchResponse>(rs.getResponse(), HttpStatus.OK);
	}
	
  @ApiOperation("Get the Venue details")
  @RequestMapping(value = "/detail", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<DetailResponse> getVenueDetail(@RequestParam(value = "venueId") String venueId)
  {
    String errorMsg = "";
    
    VenueDetailRS rs = null;
    try {
      VenueDetailsRequest rq = new VenueDetailsRequest(venueId);

      rs = getVenueDetailsCall().call(rq);

    } catch (FeignException e) {
      log.error("Fail connecting to the foursquare api: {0}", e.getMessage());
      errorMsg = getErrorMessage(e);
    } catch (AppException e) {
      log.error("Fail performing a Venue Search query: {0}", e.getMessage());
      errorMsg = getErrorMessage(e);
    }
    
    if (StringUtils.isNotEmpty(errorMsg)) {
      return ExceptionHandler.handleDetailError(errorMsg);
    }
    return new ResponseEntity<DetailResponse>(rs.getResponse(), HttpStatus.OK);

  }
  
  
  @Getter
  @Autowired
  VenueDetailsQueryHandler venueDetailsHandler;
  
  @Getter
  @Autowired
  VenueSearchCallHandler venueSearchCallHandler;
  
  
  @ApiOperation("Search for Venue and get all the venues details")
  @RequestMapping(value = "/searchAndDetail", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<DetailResponse>> venueSearchAndDetail(@RequestParam(value = "venue") String venue,
			@RequestParam(value = "query", required = false) String query)
  {
		VenueRequest rq = new VenueRequest(venue);
		rq.setLimit("5");
  	VenueSearchRS searchResponse = getVenueSearchCallHandler().handle(rq);
  	
  	List<VenueDetailRS> detailsResponses = getVenueDetailsHandler().handle(getVenueIds(searchResponse));
  	
  	List<DetailResponse> rs = detailsResponses.stream()
  									.map(ds -> ds.getResponse())
  									.collect(Collectors.toList());
  	
  	return new ResponseEntity<List<DetailResponse>>(rs, HttpStatus.OK);
  }
  
	private String getErrorMessage(final Exception e) {
		return StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage() : ExceptionHandler.UNEXPECTED_ERROR_MSG;
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
