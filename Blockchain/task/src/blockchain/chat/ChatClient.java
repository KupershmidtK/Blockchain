package blockchain.chat;

import blockchain.SignatureUtil;

import java.util.List;
import java.util.Random;
import java.security.InvalidKeyException;

public class ChatClient {
    private final ChatServer server;
    private final List<String>  phrases = List.of("Hello!", "How are you?", "Want to sleep", "What are you doing?", "I'm fine");
    private byte[] privateKey;
    private byte[] publicKey;

    public ChatClient(ChatServer server) {
        this.server = server;
        try {
            publicKey = SignatureUtil.getPublic("KeyPair/publicKey");
            privateKey = SignatureUtil.getPrivate("KeyPair/privateKey");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setMessage(String sender) {
        Random phrase = new Random();
        String text = sender + ": " + phrases.get(phrase.nextInt(phrases.size()));
        server.addMessage(this, text);
    }

    public byte[] sign(String msg) throws InvalidKeyException, Exception{
        return SignatureUtil.sign(msg.getBytes(), privateKey);
    }
}
