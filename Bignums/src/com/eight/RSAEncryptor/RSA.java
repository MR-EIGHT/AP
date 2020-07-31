package com.eight.RSAEncryptor;


import com.eight.num.BigNum;

import java.io.*;
import java.util.ArrayList;


public class RSA {
    private final static BigNum one = new BigNum("1");
    private final BigNum privateKey;
    private final BigNum publicKey;
    private final BigNum modulus;


    RSA(int n) {
        BigNum p = BigNum.generatePrime(n / 2);
        BigNum q = BigNum.generatePrime(n / 2);
        BigNum phi = (p.subtract(one)).multiply(q.subtract(one));

        modulus = p.multiply(q);
        publicKey = new BigNum("547");
        privateKey = publicKey.modInverse(phi);
    }

    public static void encrypt(RSA key) throws IOException {
        String s;

        BufferedWriter writer = new BufferedWriter(new FileWriter(("K:\\Projects of AP\\bignums\\Bignums\\src\\com\\eight\\RSAEncryptor\\hello" + "Encrypted" + ".txt")));

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream("K:\\Projects of AP\\bignums\\Bignums\\src\\com\\eight\\RSAEncryptor\\hello.txt"));

        ArrayList<BigNum> encryptedArr = new ArrayList<>();

        System.out.println("Encryption Started!");
        while (bis.available() > 0) {
            int NumberD = bis.read();
            s = Integer.toString(NumberD);
            byte[] bytes = s.getBytes();
            BigNum Text = BigNum.fromByte(bytes);

            BigNum encrypt = key.encrypt(Text);
            encryptedArr.add(encrypt);

        }


        StringBuilder encrypted = new StringBuilder();

        for (BigNum data : encryptedArr) {
            encrypted.append(data);
        }


        writer.write(encrypted.toString());
        System.out.println("Encryption Finished!");

        bis.close();
        writer.close();

    }

    public static void decrypt(RSA key) throws IOException {
        String s;

        BufferedWriter writer = new BufferedWriter(new FileWriter(("K:\\Projects of AP\\bignums\\Bignums\\src\\com\\eight\\RSAEncryptor\\hello" + "Decrypted" + ".txt")));

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream("K:\\Projects of AP\\bignums\\Bignums\\src\\com\\eight\\RSAEncryptor\\helloEncrypted.txt"));

        ArrayList<BigNum> decryptedArr = new ArrayList<>();
        System.out.println("Decryption Started!");
        while (bis.available() > 0) {
            int NumberD = bis.read();
            s = Integer.toString(NumberD);
            byte[] bytes = s.getBytes();
            BigNum Text = BigNum.fromByte(bytes);

            BigNum decrypt = key.decrypt(Text);
            decryptedArr.add(decrypt);

        }


        StringBuilder decrypted = new StringBuilder();

        for (BigNum data : decryptedArr) {
            decrypted.append(data);
        }


        writer.write(decrypted.toString());

        System.out.println("Decryption Finished!");

        bis.close();
        writer.close();

    }

    public static void main(String[] args) throws IOException {
        int N = 4;
        RSA key = new RSA(N);
        System.out.println(key);
        key.print();


        encrypt(key);
        decrypt(key);
    }

    private void print(){
        System.out.println("modulus: "+modulus);
        System.out.println("publicKey: "+publicKey);
        System.out.println("privateKey: "+privateKey);
    }

    BigNum encrypt(BigNum message) {
        return message.modPow(publicKey, modulus);
    }

    BigNum decrypt(BigNum encrypted) {
        return encrypted.modPow(privateKey, modulus);
    }

}
