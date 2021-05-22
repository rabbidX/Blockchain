package blockchain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Block {
    private String prevBlockHash;
    private String blockHash;
    private int id;
    private long timeStamp;
    private int magicNumber;
    private int spentTime;
    private int minerID;
    private String comment;
    private List<Message> messages = new ArrayList<>();

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = new ArrayList<>(messages.size());
        this.messages.addAll(messages);
    }

    public Block(String prevBlockHash, int id, int minerID) {
        this.prevBlockHash = prevBlockHash;
        this.id = id;
        this.minerID = minerID;
        timeStamp = new Date().getTime();
        Random random = new Random();
        boolean isProved = false;
        String head = "0".repeat(Math.max(0, Blockchain.getZeroCount()));
        while (!isProved) {
            magicNumber = random.nextInt();
            String stringForHash = this.stringForHash();
            blockHash = StringUtil.applySha256(stringForHash);
            if (blockHash.startsWith(head)) {
                isProved = true;
            }
        }
        spentTime = (int)(new Date().getTime() - timeStamp) / 1000;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public int getId() {
        return id;
    }

    public String getPrevBlockHash() {
        return prevBlockHash;
    }

    public int getSpentTime() {
        return spentTime;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        String blockData;
        if (messages.size() == 0) {
            blockData = "no messages";
        } else {
            blockData = messages.stream().map((Message message) -> {
                try {
                    return message.getText();
                  } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }).collect(Collectors.joining("\n"));
        }
        return "Block:\n" +
                "Created by miner # " + minerID + "\n" +
                "Id: "+ id + "\n" +
                "Timestamp: " + timeStamp + "\n" +
                "Magic number: " + magicNumber + "\n" +
                "Hash of the previous block:\n" +
                prevBlockHash + "\n" +
                "Hash of the block:\n" +
                blockHash + "\n" +
                "Block data: " + blockData + "\n" +
                "Block was generating for " + spentTime + " seconds" + "\n" +
                comment + "\n\n";
    }

    public String stringForHash() {
        return "Block:\n" +
                "Id: "+ id + "\n" +
                "Timestamp: " + timeStamp + "\n" +
                "Magic number: " + magicNumber + "\n" +
                "Hash of the previous block:\n" +
                prevBlockHash + "\n";
    }
}