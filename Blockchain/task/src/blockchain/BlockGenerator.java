package blockchain;

import java.util.Date;

public class BlockGenerator {
    public static Block generate(Block prevBlock) {
        Block block = new Block();

        long blockId = prevBlock == null
                ? 1
                : prevBlock.getId() + 1;
        block.setId(blockId);

        long timeStamp = new Date().getTime();
        block.setTimeStamp(timeStamp);

        String prevHash = prevBlock == null
                ? String.valueOf(0)
                : prevBlock.getHash();
        block.setPrevHash(prevHash);


        return block;
    }
}

