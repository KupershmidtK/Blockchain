package blockchain;

import blockchain.chat.Transaction;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class BlockChain implements Serializable {
    private static final long serialVersionUID = 7L;

    private final LinkedList<Block> blockChain = new LinkedList<>();
    private int numberOfZeros = 4;

    public int getNumberOfZeros() {
        return numberOfZeros;
    }

    public int increaseNumberOfZeros() {
        return numberOfZeros < 4 ? ++numberOfZeros : 4;
    }

    public int decreaseNumberOfZeros() {
        return numberOfZeros > 0 ? --numberOfZeros : 0;
    }

    public synchronized void add(Block block) {
        blockChain.addLast(block);
    }

    public synchronized Block getLast() {
        return blockChain.peekLast();
    }

    public void printBlock(Block block) {
        System.out.println("Id: " + block.getId());
        System.out.println("Timestamp: " + block.getTimeStamp());
        System.out.println("Magic number: " + block.getMagicNumber());
        System.out.println("Hash of the previous block:");
        System.out.println(block.getPrevHash());
        System.out.println("Hash of the block:");
        System.out.println(block.getHash());
        System.out.println("Block data:");
        block.getMessages().forEach(System.out::println);
    }

    public boolean checkChain() {
        long prevMsgId = 0L;
        String prevHash = "0";
        for (Block block : blockChain) {
            if (!block.getPrevHash().equals(prevHash)) {
                System.out.println("Previous hash number is different!");
                return false;
            }

            prevHash = block.getHash();
            for (Transaction tran : block.getMessages()) {
                long msgId = tran.getId();
                if (msgId != prevMsgId + 1) {
                    System.out.println("Message ID is not in ascending order!");
                    return false;
                }
                prevMsgId = msgId;

                boolean isSigned = false;
                try {
                isSigned = SignatureUtil.verifySignature(
                        tran.toString().getBytes(),
                        tran.getSignature(),
                        tran.getPublicKey());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!isSigned) {
                    System.out.println("Message signature is corrupted!");
                    return false;
                }
            }
        }
        return true;
    }

    public long getLastMessageId() {
        Block block = getLast();
        if (block == null) {
            return 0;
        }
        List<Transaction> list = block.getMessages();
        return list.size() == 0 ? 1 : list.get(list.size() - 1).getId();
    }
}
