package cowin;

import cowin.model.CenterModel;
import cowin.model.SessionModel;
import cowin.response.CowinResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

public class CowinController {

    private RestTemplate restTemplate = new RestTemplate();
    private HashMap<String, Set<Integer>> stateDistricts;

    private Logger log = LoggerFactory.getLogger(CowinController.class);


    public CowinController() {
        stateDistricts = new HashMap<>();
        Set<Integer> delhi = new HashSet<>();
        delhi.add(141);  //central delhi
        delhi.add(145); //East
        delhi.add(140); //New delhi
        delhi.add(146); //North delhi
        delhi.add(147); //NorthEast
        delhi.add(143); //NorthWest
        delhi.add(148); //shahdara
        delhi.add(149); //South
        delhi.add(144); //SouthEast
        delhi.add(150); //SouthWest
        delhi.add(142); //West

        Set<Integer> chandigarh = new HashSet<>();
        chandigarh.add(108);

        Set<Integer> up = new HashSet<>();
        up.add(651); //ghaziabad

        Set<Integer> mp = new HashSet<>();
        up.add(313); //gwalior

        Set<Integer> telangana = new HashSet<>();
        up.add(581); //hyderabad

        stateDistricts.put("DEL", delhi);
        stateDistricts.put("CHD", chandigarh);
        stateDistricts.put("UP", up);
        stateDistricts.put("MP", mp);
        stateDistricts.put("TEL", telangana);

    }

    public List<CenterModel> getAllAvailableSlots(int ageLimit, String startDate, String stateCode, String userAgent) {

        Set<Integer> allDistricts = getAllDistricts(stateCode);
        if (allDistricts.isEmpty())
            return new LinkedList<>();

        List<CenterModel> allSlots = new LinkedList<>();
        for (Integer district : allDistricts) {
            allSlots.addAll(getCentersByDistrictAndStartDate(district, startDate, ageLimit, userAgent));
        }
        return allSlots;
    }

    private List<CenterModel> getCentersByDistrictAndStartDate(Integer district, String startDate, int ageLimit, String userAgent) {
        String cowinUrl = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/calendarByDistrict";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(cowinUrl).queryParam("district_id", district).queryParam("date", startDate);

        HttpEntity<String> entity = new HttpEntity("", createHttpHeaders(userAgent));
        ResponseEntity<CowinResponse> response = null;
        String googleUrl = "https://jsonplaceholder.typicode.com/todos/1";
        UriComponentsBuilder googleBuilder = UriComponentsBuilder.fromHttpUrl(googleUrl);

        try {
            log.info("Headers passsed" + entity.getHeaders().toString());
            response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, CowinResponse.class);
        } catch (Exception e) {
            log.error("Exception occurred in retrieval from cowin" + e.getMessage());
            log.error("exception: " + e.getCause());
            e.getStackTrace();
        }

        if (response == null)
            log.warn(String.format("Error in retrieving data for the given district: %s and ageLimit: %s", district, ageLimit));

        List<CenterModel> availableSlots = new LinkedList<>();
        if (response != null) availableSlots = filterByAgeAndAvailability(response.getBody(), ageLimit);
        return availableSlots;
    }

    private List<CenterModel> filterByAgeAndAvailability(CowinResponse response, int ageLimit) {
        List<CenterModel> availableSlots = new LinkedList<>();

        for (CenterModel centerModel : response.getCenters()) {
            List<SessionModel> availableSessions = centerModel.getSessions().stream().filter(center -> center.getMin_age_limit() == ageLimit && center.getAvailable_capacity() > 0).collect(Collectors.toList());
            if (!availableSessions.isEmpty()) {
                centerModel.setSessions(availableSessions);
                availableSlots.add(centerModel);
            }
        }
        return availableSlots;

    }

    private Set<Integer> getAllDistricts(String state) {
        if (stateDistricts.containsKey(state.toUpperCase()))
            return stateDistricts.get(state.toUpperCase());
        return new HashSet<>();
    }

    private HttpHeaders createHttpHeaders(String userAgent) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", userAgent);
        headers.set("Host", "cdn-api.co-vin.in");
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));
        return headers;
    }

}
