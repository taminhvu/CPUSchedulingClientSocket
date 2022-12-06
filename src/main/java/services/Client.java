
package services;

import entities.DataPackage;
import entities.Process;
import entities.Ddnode;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.*;
import java.util.ArrayList;

import javax.crypto.SealedObject;
import javax.crypto.SecretKey;


public class Client {
    private  String host = "localhost";
    private  int port = 1234;
    private  Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private   ArrayList<entities.Process> processes = new ArrayList<>();
    private Ddnode ddnode = new Ddnode();
    private  String message;
    public  PublicKey servicePublicKey;
    public  PrivateKey clientPrivateKey;
    public  PublicKey clientPublicKey;
    public SecretKey secretKey;

    public Client() throws IOException, NoSuchAlgorithmException{
        this.socket = new Socket(host, port);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        generateKey();
    }
    
    public Client(String host, int port) throws IOException, NoSuchAlgorithmException{
        this.host = host;
        this.port = port;
        this.socket = new Socket(host, port);
    	this.out = new ObjectOutputStream(socket.getOutputStream());
    	this.in = new ObjectInputStream(socket.getInputStream());
    	generateKey();
    }
    
    public void generateKey() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();
        setClientPrivateKey(privateKey);
        setClientPublicKey(publicKey);
    }
    public void sendAndReceiveKey() throws Exception {
        setServicePublicKey((PublicKey) in.readObject());
        out.writeObject(getClientPublicKey());
        setSecretKey(HelperService.decryptSymmetricKey((byte[]) in.readObject(),getClientPrivateKey()));
    }

    public void send(DataPackage dataPackage) throws Exception {
        out.writeObject(dataPackage);
    }
    public DataPackage receive() throws IOException, ClassNotFoundException {

        DataPackage dataPackage = (DataPackage) in.readObject();
        return dataPackage;
    }
    public void startClient() throws Exception {
        generateKey();
        sendAndReceiveKey();
    }

    public ArrayList<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(ArrayList<Process> processes) {
        this.processes = processes;
    }

    public PublicKey getServicePublicKey() {
        return servicePublicKey;
    }

    public void setServicePublicKey(PublicKey servicePublicKey) {
        this.servicePublicKey = servicePublicKey;
    }

    public PrivateKey getClientPrivateKey() {
        return clientPrivateKey;
    }

    public void setClientPrivateKey(PrivateKey clientPrivateKey) {
        this.clientPrivateKey = clientPrivateKey;
    }

    public PublicKey getClientPublicKey() {
        return clientPublicKey;
    }

    public void setClientPublicKey(PublicKey clientPublicKey) {
        this.clientPublicKey = clientPublicKey;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDdnode(Ddnode ddnode) {
        this.ddnode = ddnode;
    }

    public Ddnode getDdnode() {
        return ddnode;
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client("localhost",1234);
//        client.startClient();
//        client.setProcesses(HelperService.readProcess("C:\\Users\\minhv\\OneDrive\\Máy tính\\servercuahieu\\CPUSchedulingClient\\text.txt"));
//        SealedObject  obj = HelperService.encryptObject(client.getProcesses(),client.getSecretKey());
//        String str = HelperService.encryptInput("fcfs",client.getSecretKey());
//        DataPackage dataPackage = new DataPackage(obj,str);
//        client.send(dataPackage);
//        //receive
//        DataPackage dataPackage1 = client.receive();
//        ArrayList<Process> processes1 = (ArrayList<Process>) dataPackage1.getSealedObject().getObject(client.getSecretKey());
//
//        System.out.println(processes1);

//        client.startClient();
//        client.setNodes(HelperService.readDDnode("C:\\Users\\minhv\\OneDrive\\Máy tính\\servercuahieu\\CPUSchedulingClient\\src\\main\\java\\GUI\\node.txt"));
//        SealedObject  obj = HelperService.encryptObject(client.getNodes(),client.getSecretKey());
//        String str = HelperService.encryptInput("ddnn",client.getSecretKey());
//        DataPackage dataPackage = new DataPackage(obj,str);
//        client.send(dataPackage);
    }
}
