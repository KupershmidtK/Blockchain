package blockchain.chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ChatServer {
    List<Message> messages = Collections.synchronizedList(new ArrayList<>());
    private AtomicLong id = new AtomicLong();

    public synchronized void addMessage(ChatClient client, String text) {
        Message message = new Message();
        long nextId = getNextId();
        try {
            message.setId(nextId);
            message.setText(text);
            message.setSignature(client.sign(message.toString()));
            message.setPublicKey(client.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        messages.add(message);
    }

    public synchronized List<Message> getAllMessages() {
        List<Message> allMessages = new ArrayList<>(messages);
        messages.clear();
        return  allMessages;
    }

    public long getNextId() {
        return id.incrementAndGet();
    }

    public void setId(long id) {
        this.id.set(id);
    }
}
