
package services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import entities.Process;
import entities.Ddnode;

public class HelperService {

    public static ArrayList<entities.Process> readProcess(String name) throws Exception {
        ArrayList<entities.Process> processes = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(name));
        String line;
        while (reader.ready()) {
            line = reader.readLine();
            StringTokenizer str = new StringTokenizer(line, ";");
            entities.Process process = new entities.Process();
            process.setNameProcess(str.nextToken());
            int temp = Integer.parseInt(str.nextToken());
            if (temp < 0)
                throw new Exception("loi");
            process.setArrivalTime(temp);
            temp = Integer.parseInt(str.nextToken());
            if (temp <= 0)
                throw new Exception("loi");
            process.setBurstTime(temp);
            if (str.hasMoreTokens()) {
                temp = Integer.parseInt(str.nextToken());
                if (temp < 0)
                    throw new Exception("loi");
                process.setPriority(temp);
            }
            processes.add(process);
        }
        reader.close();
        return processes;
    }

    public static Ddnode readDDnode(String name) throws Exception {
        Ddnode nodes = new Ddnode();

        BufferedReader reader = new BufferedReader(new FileReader(name));
        String line;
        while (reader.ready()) {
            line = reader.readLine();
            nodes.insertNode(line);
        }
        reader.close();
        return nodes;
    }

    public static SecretKey decryptSymmetricKey(byte[] symmetricKey, PrivateKey privateKey) throws Exception {
        // initialize the cipher...
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        // generate the aes key!
        SecretKey key = new SecretKeySpec(cipher.doFinal(symmetricKey), "AES");
        return key;
    }

    public static byte[] encryptSymmetricKey(SecretKey symmetricKey, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        // contact.getPublicKey returns a public key of type Key
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // skey is the SecretKey used to encrypt the AES data
        byte[] key = cipher.doFinal(symmetricKey.getEncoded());
        return key;
    }

    public static SealedObject encryptObject(ArrayList<entities.Process> processes, SecretKey secretKey) throws Exception {
        // generate symmetric key & generate IV
        SecretKey key = secretKey;
        byte[] iv = new byte[16];
        // create cipher
        Cipher cipher = Cipher.getInstance(key.getAlgorithm() + "/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        // create sealed object
        SealedObject sealedEm1 = new SealedObject(processes, cipher);
        return sealedEm1;
    }

    public static SealedObject encryptObject(Ddnode ddnode, SecretKey secretKey) throws Exception {
        // generate symmetric key & generate IV
        SecretKey key = secretKey;
        byte[] iv = new byte[16];
        // create cipher
        Cipher cipher = Cipher.getInstance(key.getAlgorithm() + "/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        // create sealed object
        SealedObject sealedEm1 = new SealedObject(ddnode, cipher);
        return sealedEm1;
    }

    public static String encryptInput(String input, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(key.getAlgorithm() + "/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[16]));
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decryptInput(String encryptInput, SecretKey symmetricKey) throws Exception {
        SecretKey key = symmetricKey;
        byte[] iv = new byte[16];
        Cipher cipher = Cipher.getInstance(key.getAlgorithm() + "/CBC/PKCS5Padding");
        // turn the mode of cipher to decryption
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv)); // reuse the key and iv generated before
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(encryptInput));
        return new String(plainText);
    }

    public static SecretKey generatorSymmetricKey() throws NoSuchAlgorithmException {
        return KeyGenerator.getInstance("AES").generateKey();
    }


    public static void main(String[] args) throws Exception {
        //Nhận KHÓA ĐỐI XỨNG (ĐÃ BỊ MÃ HÓA) và Khóa PUBLIC của thằng client
        SecretKey clientSymmetricKey;
        PublicKey clientPublicKey;
        SecretKey symmetricKey = generatorSymmetricKey();
        //Tạo publickey và private key cho server
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();


        HelperService fs = new HelperService();
        try {
            ArrayList<Process> process = fs.readProcess("text.txt");
            SealedObject arrayobj = encryptObject(process, symmetricKey);

            byte[] keycipher = encryptSymmetricKey(symmetricKey, publicKey);
            System.out.println(keycipher);

            clientSymmetricKey = decryptSymmetricKey(keycipher, privateKey);
            System.out.println(clientSymmetricKey);

            System.out.println(arrayobj.getObject(clientSymmetricKey));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
