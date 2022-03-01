package blockchain.chat;

import blockchain.SignatureUtil;

import java.io.Serializable;
import java.util.Random;

public class Trader implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name = "";
    private transient StockMarket market;
    private int money;
    private byte[] privateKey;
    private byte[] publicKey;

    private transient Thread tradeThread;

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Trader() { }

    public Trader(String name, StockMarket market) {
        this.name = name;
        initialize(market);
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void initialize(StockMarket market) {
        this.market = market;
        market.registerOnMarket(this);

        try {
            publicKey = SignatureUtil.getPublic("KeyPair/publicKey");
            privateKey = SignatureUtil.getPrivate("KeyPair/privateKey");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMoney() {
        Trader to = market.getTrader();
        if (to == this) {
            return;
        }
        Random random = new Random();
        int sum = random.nextInt(money + 1);
        market.makeTrans(this, to, sum);
    }

    public byte[] sign(String msg) throws Exception{
        return SignatureUtil.sign(msg.getBytes(), privateKey);
    }

    public void startTrading() {
        tradeThread = new Thread(() -> {
            try {
                while (!Thread.interrupted()){
                    sendMoney();
                    Thread.sleep(550);
                }
            } catch (InterruptedException e) {
//                e.printStackTrace();
            }
        });

        tradeThread.start();
    }

    public void stopTrading() {
        tradeThread.interrupt();
    }
}
