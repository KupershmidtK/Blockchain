package blockchain.chat;

import java.io.Serializable;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private Trader from;
    private Trader to;
    private int summa;
    private byte[] signature;
    private byte[] publicKey;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public void setFrom(Trader from) {
        this.from = from;
    }

    public void setTo(Trader to) {
        this.to = to;
    }

    public void setSumma(int summa) {
        this.summa = summa;
    }

    @Override
    public String toString() {
        return id + ". " + from.getName() + " sent " + summa + " VC to " + to.getName();
    }
}
