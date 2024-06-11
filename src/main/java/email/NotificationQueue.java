package email;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class NotificationQueue {
    private final BlockingQueue<Email> queue = new LinkedBlockingQueue<>();

    public void addEmail(Email email) {
        queue.add(email);
    }

    public Email takeEmail() throws InterruptedException {
        return queue.take();
    }
}

