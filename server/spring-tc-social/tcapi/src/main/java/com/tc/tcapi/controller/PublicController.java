package com.tc.tcapi.controller;

import com.tc.core.response.ForwardGeocodingResponse;
import com.tc.core.response.PlaceAutocompleteResponse;
import com.tc.tcapi.utilities.Constance;
import com.tc.tcapi.helper.UserHelper;
import com.tc.core.request.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PublicController {

    private final UserHelper userHelper;

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        return null;
    }

    @GetMapping("validation-input")
    public ResponseEntity<?> validationInput(@RequestParam("input") String input,
                                             @RequestParam("value") String value) {
        return userHelper.validationInput(input, value);
    }

    @PostMapping("/users/registration")
    public ResponseEntity<?> registrationAccount(@RequestBody @Valid RegistrationRequest registrationRequest) {
        return userHelper.registrationAccount(registrationRequest);
    }

    @GetMapping("/images/{name}")
    public ResponseEntity<?> getImage(@PathVariable("name") String name) {
        return userHelper.getImage(name);
    }

    @GetMapping("/goong/geocode/address/{address}") ResponseEntity<ForwardGeocodingResponse> getForwardGeocoding(@PathVariable String address){
        String URL = Constance.GOONG_URL+"/geocode";
        RestTemplate template = new RestTemplateBuilder().build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(URL)
                .queryParam("address", address)
                .queryParam("api_key", Constance.GOONG_API_KEY);
        ResponseEntity<ForwardGeocodingResponse> responseEntity = template.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                null,
                ForwardGeocodingResponse.class
        );
        return responseEntity;
    }
    @GetMapping("/goong/place/autocomplete/{place}") ResponseEntity<PlaceAutocompleteResponse> getPlaceAutoComplete(@PathVariable String place){
        String URL = Constance.GOONG_URL+"/Place/AutoComplete";
        RestTemplate template = new RestTemplateBuilder().build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(URL)
                .queryParam("input", place)
                .queryParam("api_key", Constance.GOONG_API_KEY);
        ResponseEntity<PlaceAutocompleteResponse> responseEntity = template.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                null,
                PlaceAutocompleteResponse.class
        );
        return responseEntity;
    }
    @GetMapping("/goong/place/detail/{placeId}") ResponseEntity getPlaceDetailById(@PathVariable String placeId){
        String URL = Constance.GOONG_URL+"/Place/Detail";
        RestTemplate template = new RestTemplateBuilder().build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(URL)
                .queryParam("place_id", placeId)
                .queryParam("api_key", Constance.GOONG_API_KEY);
        ResponseEntity<Object> responseEntity = template.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                null,
                Object.class
        );
        return responseEntity;
    }
}
