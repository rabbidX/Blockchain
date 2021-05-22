package blockchain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Blockchain {
    private LinkedList<Block> chain;
    private static int zeroCount;
    private static Blockchain instance = new Blockchain();
    private List<Message> messages = new ArrayList<>();
    ;

    public void addMessage(Message message) {
        messages.add(message);
    }

    public static void setZeroCount(int zeroCount) {
        Blockchain.zeroCount = zeroCount;
    }

    public static int getZeroCount() {
        return zeroCount;
    }

    private Blockchain() {
        chain = new LinkedList<>();
    }

    public static Blockchain getInstance() {
        return instance;
    }

    public int getChainSize() {
        return chain.size();
    }

    public void increaseZeroCount() {
        zeroCount++;
    }

    public void decreaseZeroCount() {
        zeroCount--;
    }

    public void  add(Block block) {
        block.setMessages(messages);
        chain.addLast(block);
        messages = new ArrayList<>();
    }

    public Block getLastBlock() {
        return  chain.getLast();
    }

    public boolean isValid() {
        String prevHash = "0";
        for (Block block: chain) {
            String hash = block.getBlockHash();
            String validPrevHash = block.getPrevBlockHash();
            String stringForHash = block.stringForHash();
            String validHash = StringUtil.applySha256(stringForHash);
            if (!validHash.equals(hash) || !validPrevHash.equals(prevHash)) {
                return false;
            }
            prevHash = hash;
        }
        return true;
    }

    public boolean validateNewBlock(Block newBlock) {
        String prevHash = "0";
        if (chain.size() > 0) {
            Block lastBlock = chain.getLast();
            prevHash = lastBlock.getBlockHash();
        }
        return newBlock.getPrevBlockHash().equals(prevHash);
    }

    public void showFirstFive() {
        int max = Math.min(chain.size(), 5);
        for (int i = 0; i < max; i++) {
            Block block = chain.get(i);
            System.out.println(block.toString());
        }
    }
}


