package blockchain;

import blockchain.chat.Trader;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Miner extends Trader implements Runnable {
    private transient FindHashTask task;

    public void setTask(FindHashTask task) {
        this.task = task;
    }

    private void addReward() {
        setMoney(getMoney() + 100);
    }

    @Override
    public void run() {
        LocalDateTime start = LocalDateTime.now();

        Random random = new Random();
        Block block = task.getBlock();
        int numberOfZeros = task.getNumberOfZeros();

        long magicNumber = 0;
        String hash = "";

        while (!task.isStopFlag()) {
            magicNumber = random.nextLong();
            block.setMagicNumber(magicNumber);
            hash = applySha256(block.toString());
            if (Long.parseLong(hash.substring(0, numberOfZeros), 16) == 0) {
                block.setHash(hash);
                break;
            }
        }
        if (!task.isStopFlag()) {
            task.setResults(magicNumber, hash, start, getName());
            addReward();

        }
    }

    private String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte elem : hash) {
                String hex = Integer.toHexString(0xff & elem);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

class FindHashTask {
    private final Block block;
    private final int numberOfZeros;

    private String threadName;
    private long searchingTime;
    private final AtomicBoolean stopFlag = new AtomicBoolean(false);

    FindHashTask(Block block, int numberOfZeros) {
        this.block = block;
        this.numberOfZeros = numberOfZeros;
    }

    public synchronized Block getBlock() {
        return block.clone();
    }

    public int getNumberOfZeros() {
        return numberOfZeros;
    }

    public boolean isStopFlag() {
        return stopFlag.get();
    }

    public String getThreadName() {
        return threadName;
    }

    public long getSearchingTime() {
        return searchingTime;
    }

    public synchronized void setResults(long magicNumber, String hash, LocalDateTime startTime, String name) {
        if(!stopFlag.get()) {
            block.setMagicNumber(magicNumber);
            block.setHash(hash);
            threadName = name;
            searchingTime = ChronoUnit.SECONDS.between(startTime, LocalDateTime.now());
            stopFlag.set(true);
        }
    }
}