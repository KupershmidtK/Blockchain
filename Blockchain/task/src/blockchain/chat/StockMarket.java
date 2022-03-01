package blockchain.chat;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class StockMarket {
    private final List<Trader> traders = Collections.synchronizedList(new ArrayList<>());
    private final List<Transaction> transactions = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong id = new AtomicLong();

    public void registerOnMarket(Trader trader) {
        traders.add(trader);
    }

    public Trader getTrader() {
        Random random = new Random();
        int idx = random.nextInt(traders.size());
        return traders.get(idx);
    }

    public synchronized void makeTrans(Trader from, Trader to, int sum) {
        if (sum == 0 || sum > from.getMoney()) {
            return;
        }
        Transaction trans = new Transaction();
        long nextId = getNextId();

        try {
            trans.setId(nextId);
            trans.setFrom(from);
            trans.setTo(to);
            trans.setSumma(sum);
            trans.setSignature(from.sign(trans.toString()));
            trans.setPublicKey(from.getPublicKey());
        } catch (Exception e) {
            id.decrementAndGet();
            e.printStackTrace();
        }
        transactions.add(trans);
    }

    public synchronized List<Transaction> getAllTransactions() {
        List<Transaction> allTrans = new ArrayList<>(transactions);
        transactions.clear();
        return  allTrans;
    }

    public long getNextId() {
        return id.incrementAndGet();
    }

    public void setId(long id) {
        this.id.set(id);
    }
}

