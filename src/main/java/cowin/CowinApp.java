package cowin;

import com.amazonaws.services.lambda.runtime.Context;

import java.util.Map;

public final class CowinApp {

    public static String handleRequest(Map<String, String> input, Context context) {
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
        return cowin.getAllAvailableSlots(age, date, stateCode, userAgent).toString();
    }

//    public static void main(String... s) {
//        CowinController cowin = new CowinController();
//        cowin.getAllAvailableSlots(45, "custom").toString();
//    }


}
