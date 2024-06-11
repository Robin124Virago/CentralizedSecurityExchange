package email;

public class Email {
    private final String recipient;
    private final String subject;
    private final String body;

    public Email(String recipient, String subject, String body) {
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
    }

    public String getRecipient() { return recipient; }
    public String getSubject() { return subject; }
    public String getBody() { return body; }

    @Override
    public String toString() {
        return "Email{" + "recipient='" + recipient + '\'' + ", subject='" + subject + '\'' + ", body='" + body + '\'' + '}';
    }
}

