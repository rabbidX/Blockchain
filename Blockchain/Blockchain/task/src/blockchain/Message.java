package blockchain;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.List;
import java.util.stream.Collectors;

public class Message {
    private List<String> encryptedXML;
    private PublicKey publicKey;
    private Cipher cipher;

    public String getText() throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException, ParserConfigurationException, SAXException {
        String decryptedXML = decryptText(encryptedXML);
        Node textNode = getXMLNodes(decryptedXML).item(0);
        return textNode.getTextContent();
    }

    public long getTime() throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException, SAXException, ParserConfigurationException {
        String decryptedXML = decryptText(encryptedXML);
        Node textNode = getXMLNodes(decryptedXML).item(1);
        String timeStamp = textNode.getTextContent();
        return Long.parseLong(timeStamp);
    }

    public Message(List<String> text, PublicKey publicKey) throws XMLStreamException, NoSuchPaddingException, NoSuchAlgorithmException {
        this.encryptedXML = text;
        this.publicKey = publicKey;
        this.cipher = Cipher.getInstance("RSA");

    }

    public String decryptText(List<String> msg)
            throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        this.cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return msg.stream().map(text -> {
            try {
                return new String(cipher.doFinal(Base64.decodeBase64(text)), "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }).collect(Collectors.joining());
    }

    private NodeList getXMLNodes(String xmlString) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        StringReader stringReader = new StringReader(xmlString);
        InputSource inputSource = new InputSource(stringReader);
        Document document = documentBuilder.parse(inputSource);
        Node root = document.getDocumentElement();
        NodeList elements = root.getChildNodes();

        return elements;
    }
}
