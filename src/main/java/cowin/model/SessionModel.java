package cowin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessionModel {

    private Integer available_capacity;
    private Integer min_age_limit;
    private String vaccine;
    private List<String> slots;

    public Integer getAvailable_capacity() {
        return available_capacity;
    }

    public Integer getMin_age_limit() {
        return min_age_limit;
    }

}
