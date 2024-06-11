package email;

public class EmailService {
    public void sendEmail(Email email) throws EmailException {
        System.out.println("Sending email to: " + email.getRecipient());
    }
}

