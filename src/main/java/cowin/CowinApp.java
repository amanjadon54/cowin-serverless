package cowin;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import cowin.model.CenterModel;

import java.util.List;
import java.util.Map;

public final class CowinApp {

    public static String handleRequest(Map<String, String> input, Context context) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        context.getLogger().log("age passed:" + input.get("age"));
        context.getLogger().log("user-agent passed:" + input.get("user-agent"));
        context.getLogger().log("state:" + input.get("state"));
        context.getLogger().log("date:" + input.get("date"));
        if (!input.containsKey("age")) return "";
        if (!input.containsKey("date")) return "";
        if (!input.containsKey("state")) return "";

        int age = Integer.parseInt(input.get("age"));
        if (age != 18 && age != 45)
            return "";


        String userAgent = input.get("user-agent");
        String date = input.get("date");
        String stateCode = input.get("state");

        CowinController cowin = new CowinController();
        List<CenterModel> cowinFilteredData = cowin.getAllAvailableSlots(age, date, stateCode, userAgent);

        String cowinString = objectMapper.writeValueAsString(cowinFilteredData);
        Object object = objectMapper.readValue(cowinString, Object.class);
        String filteredSlots = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        return filteredSlots;
    }

//    public static void main(String... s) throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        CowinController cowin = new CowinController();
//        List<CenterModel> cowinFilteredData = cowin.getAllAvailableSlots(45, "23-05-2021", "DEL", "custom");
//        String cowinString = objectMapper.writeValueAsString(cowinFilteredData);
//        Object object = objectMapper.readValue(cowinString, Object.class);
//        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));
//    }


}
