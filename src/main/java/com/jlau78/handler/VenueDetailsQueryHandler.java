package com.jlau78.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jlau78.common.exceptions.AppException;
import com.jlau78.foursquare.request.VenueDetailsRequest;
import com.jlau78.foursquare.response.venue.VenueDetailRS;
import com.jlau78.foursquare.service.VenueDetailsCall;

import feign.FeignException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler to process a list of venueIds and makes calls to the api
 */
@Slf4j
public class VenueDetailsQueryHandler implements Handler<List<String>, List<VenueDetailRS>> {
	
	@Getter
	@Autowired
	VenueDetailsCall apiCall;

	@Override
	public List<VenueDetailRS> handle(List<String> inputs) {
		List<VenueDetailRS> details = new ArrayList<>();
		
		if (inputs != null) {
			details = getAllVenueDetails(inputs);
		}
		return details;
	}
	
	protected List<VenueDetailRS> getAllVenueDetails(final List<String> venueIds) {
		List<VenueDetailRS> venueDetails = null;

		if (CollectionUtils.isNotEmpty(venueIds)) {
			log.info("Start query for the list of venueIds: {0}", venueIds);

			ExecutorService executor = Executors.newFixedThreadPool(6);
			try {
				List<CompletableFuture<Void>> futures = venueIds.stream().map(k -> {
					return CompletableFuture.runAsync(() -> getDetail(k), executor);
				}).collect(Collectors.toList());

				futures.stream().forEach(CompletableFuture::join);

			} finally {
				executor.shutdownNow();
			}
		}
		
		return venueDetails;
	}
	
	private VenueDetailRS getDetail(final String venueId) {
		String errorMsg = "";
    VenueDetailRS rs = null;
    try {
      VenueDetailsRequest rq = new VenueDetailsRequest(venueId);
      rs = getApiCall().call(rq);

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
