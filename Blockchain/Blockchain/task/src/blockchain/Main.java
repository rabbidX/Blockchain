package blockchain;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.stream.XMLStreamException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException, NoSuchPaddingException, NoSuchAlgorithmException
            , InvalidKeyException, BadPaddingException, UnsupportedEncodingException, IllegalBlockSizeException
            , XMLStreamException {
        Blockchain.setZeroCount(0);

        User vasya = new User("Vasya");
        vasya.sendMessage("Hello, blockchain");
        vasya.sendMessage("Hello, blockchain, it's me again");

        ExecutorService taskExecutor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            taskExecutor.execute(new Miner());
        }
        taskExecutor.awaitTermination(10, TimeUnit.SECONDS);
        Blockchain blockchain = Blockchain.getInstance();
        blockchain.showFirstFive();
    }
}
