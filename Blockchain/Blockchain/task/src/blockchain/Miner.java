package blockchain;

public class Miner implements Runnable {
    private static int count;
    private int id;

    public Miner() {
        count++;
        id = count;
    }

    @Override
    public void run() {

        Blockchain blockchain = Blockchain.getInstance();
        while (blockchain.getChainSize() < 5) {
            String prevBlockHash;
            int prevId;
            if (blockchain.getChainSize() == 0) {
                prevBlockHash = "0";
                prevId = 0;
            } else {
                Block prevBlock = blockchain.getLastBlock();
                prevBlockHash = prevBlock.getBlockHash();
                prevId = prevBlock.getId();
            }
            Block newBlock = new Block(prevBlockHash, prevId + 1, id) ;
            if (blockchain.validateNewBlock(newBlock)) {

                blockchain.add(newBlock);
                int spentTime = newBlock.getSpentTime();
                if (spentTime < 10) {
                    blockchain.increaseZeroCount();
                    newBlock.setComment("N was increased to " + Blockchain.getZeroCount());
                } else if (spentTime > 60) {
                    blockchain.decreaseZeroCount();
                    newBlock.setComment("N was decreased by 1");
                } else {
                    newBlock.setComment("N stays the same");
                }

            }
        }
    }
}
