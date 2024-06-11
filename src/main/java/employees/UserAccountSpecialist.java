package employees;

import domain.Exchange;
import domain.User;

public class UserAccountSpecialist extends ExchangeEmployee {
    public UserAccountSpecialist(int id, String name) {
        super(id, name);
    }

    public void registerUser(User user, Exchange exchange) {
        exchange.registerUser(user);
    }
}

