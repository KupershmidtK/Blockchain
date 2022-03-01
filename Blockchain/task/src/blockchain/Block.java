package blockchain;

import blockchain.chat.Transaction;

import java.io.Serializable;
import java.util.List;

public class Block implements Serializable {
    private static final long serialVersionUID = 7L;

    private long id;
    private long timeStamp;
    private String prevHash;
    private String hash;
    private long magicNumber;
    private List<Transaction> transactions;

    public Block() { }

    private Block(Block source) {
        this.id = source.id;
        this.timeStamp  = source.timeStamp;
        this.prevHash = source.prevHash;
        this.hash = source.hash;
        this.magicNumber = source.magicNumber;
        this.transactions = source.transactions;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public void setPrevHash(String prevHash) {
        this.prevHash = prevHash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getMagicNumber() {
        return magicNumber;
    }

    public void setMagicNumber(long magicNumber) {
        this.magicNumber = magicNumber;
    }

    public void setMessages(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Transaction> getMessages() {
        return transactions;
    }

    @Override
    public String toString() {
        return id + String.valueOf(timeStamp) + prevHash + magicNumber + transactions;
    }

    public Block clone() {
        return new Block(this);
    }
}
