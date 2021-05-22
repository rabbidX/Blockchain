package blockchain;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private String name;
    private KeyPairGenerator keyGen;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private static final int KEY_SIZE = 1024;
    private Cipher cipher;

    public User(String name) throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.name = name;
        this.cipher = Cipher.getInstance("RSA");
        this.keyGen = KeyPairGenerator.getInstance("RSA");
        this.keyGen.initialize(KEY_SIZE);
        this.pair = this.keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public void sendMessage(String text) throws XMLStreamException, IllegalBlockSizeException
            , BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, NoSuchPaddingException {
        String xmlText = getXMLString(text);
        List<String> encryptedText = encryptText(xmlText);
        Message message = new Message(encryptedText, publicKey);
        Blockchain blockchain = Blockchain.getInstance();
        blockchain.addMessage(message);
    }

    public List<String> encryptText(String msg)
            throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        ArrayList<String> result = new ArrayList<>();
        this.cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        ArrayList<String> dividedMsg = StringUtil.divideString(msg, 100);
        for (String msgPart: dividedMsg) {
            String encodedPart = Base64.encodeBase64String(cipher.doFinal(msgPart.getBytes("UTF-8")));
            result.add(encodedPart);
      }
        return result;
    }


    private String getXMLString(String text) throws XMLStreamException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        StringWriter stringOut = new StringWriter();
        XMLStreamWriter writer = factory.createXMLStreamWriter(stringOut);
        writer.writeStartDocument("1.0");
        writer.writeStartElement("message");
        writer.writeStartElement("text");
        writer.writeCharacters(text);
        writer.writeEndElement();

        writer.writeStartElement("timestamp");
        writer.writeCharacters(String.valueOf(new Date().getTime()));
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndDocument();
        writer.flush();

        String xmlText = stringOut.toString();
        return xmlText;
    }
}
