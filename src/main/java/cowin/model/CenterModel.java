package cowin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CenterModel {

    private String name;
    private String address;
    private String state_name;
    private long pincode;
    private List<SessionModel> sessions;

    public List<SessionModel> getSessions() {
        return sessions;
    }

    public void setSessions(List<SessionModel> sessions) {
        this.sessions = sessions;
    }
}
