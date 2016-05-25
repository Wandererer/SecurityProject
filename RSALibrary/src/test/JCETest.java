package test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import javax.crypto.Cipher;

public class JCETest {

	
	public static void main(String[] args) throws Exception {

		String message = "This is a test";
		if (args.length != 0) {	// Args provided
			message = args[0];
		}

		Security.addProvider(new RSAKey.ICNJProvider());
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA","ICNJ");
		kpg.initialize(1024);
		System.out.println("Generating a key pair...");
		KeyPair keyPair = kpg.generateKeyPair();
		System.out.println("Done generating keys.\n");
		
		
		PublicKey publicKey =  keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		// Create a byte array from the message.
		byte[] messageBytes = message.getBytes("UTF8");

		// Encrypt the message with the public key.
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encryptedMessage = cipher.doFinal(messageBytes);

		
		// Decrypt the encrypted data with the private key.
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decryptedMessage = cipher.doFinal(encryptedMessage);
		String decryptedMessageString = new String(decryptedMessage,"UTF8");

		System.out.println("\nDecrypted message: " + decryptedMessageString);

		// Check that the decrypted message and the original
		// message are the same.
		if (decryptedMessageString.equals(message)) {
			System.out.println("\nTest succeeded.");
		} else {
			System.out.println("\nTest failed.");
		}
		
	}
}
