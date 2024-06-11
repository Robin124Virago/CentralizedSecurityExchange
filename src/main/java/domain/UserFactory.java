package domain;

public class UserFactory {
    private static int idCounter = 0;

    public static User createUser(String name) {
        return new User(++idCounter, name);
    }
}
