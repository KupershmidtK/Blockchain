package blockchain;

import blockchain.chat.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static class App {
        private final int NUMBER_OF_BLOCKS = 15;
        private final int NUMBER_OF_MINERS = 3;
        private final static String fileName = "blockchain.dat";

        private final List<Trader> traders = new ArrayList<>();

        public void run() {
            BlockChain blockChain = createBlockChain();
            if (blockChain == null) {
                System.out.println("Chain is corrupted!");
                return;
            }

            StockMarket market = new StockMarket();
            market.setId(blockChain.getLastMessageId());

            startTrading(market);
            runBlocksCreation(blockChain, market);
            stopTrading();

            saveBlockChain(blockChain);
        }

        private BlockChain createBlockChain() {
            BlockChain blockChain;
            try {
                blockChain = readBlockChain();
                if(!blockChain.checkChain()) {
                    return null;
                }
            }
            catch (IOException | ClassNotFoundException e) {
                // e.printStackTrace();
                blockChain = new BlockChain();
            }
            return blockChain;
        }

        private void startTrading(StockMarket market) {
            for (int i = 0; i < NUMBER_OF_MINERS; i++) {
                Trader trader = new Trader("Trader #" + i, market);
                trader.initialize(market);
                traders.add(trader);
                trader.startTrading();

                trader = new Miner();
                trader.initialize(market);
                trader.setName("Miner #" + (i + 1));
                traders.add(trader);
                trader.startTrading();
            }

        }


        private void stopTrading() {
            traders.forEach(Trader::stopTrading);
        }

        private void runBlocksCreation(BlockChain blockChain, StockMarket market) {
            for (int i = 0; i < NUMBER_OF_BLOCKS; i++) {
                List<Transaction> transactions = market.getAllTransactions();

                Block block = BlockGenerator.generate(blockChain.getLast());
                block.setMessages(transactions);
                FindHashTask task = new FindHashTask(block, blockChain.getNumberOfZeros());
                List<Thread> miners = new ArrayList<>(NUMBER_OF_MINERS);
                for (Trader miner : traders) {
                        if (miner instanceof Miner) {
                            Miner m = (Miner) miner;
                            m.setTask(task);
                            miners.add(new Thread(m));
                        }
                }
                miners.forEach(Thread::start);
                miners.forEach(miner -> {
                    try { miner.join(); }
                    catch (Exception e) { e.printStackTrace(); }
                });
                blockChain.add(block);

                System.out.println("Block:");
                System.out.println("Created by " + task.getThreadName());
                System.out.println(task.getThreadName() + " gets 100 VC");
                blockChain.printBlock(blockChain.getLast());
                System.out.println("Block was generating for " + task.getSearchingTime() + " seconds");

                if (task.getSearchingTime() < 1) {
                    System.out.println("N was increased to " + blockChain.increaseNumberOfZeros() + "\n");
                } else if (task.getSearchingTime() > 10) {
                    System.out.println("N was decreased by " + blockChain.decreaseNumberOfZeros() + "\n");
                } else {
                    System.out.println("N stays the same\n");
                }
            }
        }

        public void saveBlockChain(BlockChain blockChain) {
            try { writeBlockChain(blockChain); }
            catch (Exception e) { e.printStackTrace(); }
        }

        private void writeBlockChain(BlockChain blockChain) throws IOException {
            FileOutputStream fos = new FileOutputStream(fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(blockChain);

            oos.close();
            bos.close();
            fos.close();
        }

        private BlockChain readBlockChain() throws IOException, ClassNotFoundException {
            FileInputStream fis = new FileInputStream(fileName);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            BlockChain blockChain = (BlockChain) ois.readObject();

            ois.close();
            bis.close();
            fis.close();
            return blockChain;
        }
    }

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }
}