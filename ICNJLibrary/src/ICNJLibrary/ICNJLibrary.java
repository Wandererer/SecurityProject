package ICNJLibrary;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import javax.crypto.Cipher;

public class ICNJLibrary {
	
	private  PublicKey publicKey ;
	private  PrivateKey privateKey;
	private  Cipher cipher;
	
	public  PublicKey Init() throws Exception
	{
		Security.addProvider(new RSAKey.ICNJProvider());
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA","ICNJ");
		kpg.initialize(1024);
		KeyPair keyPair = kpg.generateKeyPair();
		publicKey=keyPair.getPublic();
		privateKey=keyPair.getPrivate();
		cipher = Cipher.getInstance("RSA");
		return publicKey;
	}
	
	public  byte[] Encrypt(String msg, PublicKey oppPublicKey) throws Exception {
		byte[] messageBytes = msg.getBytes("UTF8");

		// Encrypt the message with the public key.
		cipher.init(Cipher.ENCRYPT_MODE, oppPublicKey);
		byte[] encryptedMessage = cipher.doFinal(messageBytes);

		return encryptedMessage;
	}
	
	
	public  String Decrypt(byte[] encryptedMsg) throws Exception
	{
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decryptedMessage = cipher.doFinal(encryptedMsg);
		return new String(decryptedMessage,"UTF8");

	}
}
