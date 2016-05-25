package RSAKey;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class RSATest {

	public static void main(String[] args) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException {

		//String message ="This is test";
		String message ="Minkihyung";

		Vector<Byte> test=new Vector<Byte>();
		
		SecureRandom secure=new SecureRandom();
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(1024);
		int encryptMode=Cipher.ENCRYPT_MODE;
		int decryptMode=Cipher.DECRYPT_MODE;
		
		RSAKeyGenerator testGenerator=new RSAKeyGenerator();
		KeyPair keyPair=kpg.generateKeyPair();
	
		System.out.println("Generate Key");
		
		
		PublicKey publicKey =  keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();
		
		byte[] messageBytes = message.getBytes("UTF8");

		System.out.println(messageBytes.length);
		try {

			RSACipher rsa=new RSACipher(publicKey,Cipher.ENCRYPT_MODE,1024);
			rsa.engineInit(Cipher.ENCRYPT_MODE, publicKey, secure);
	
			
			byte[] encryptMsg = rsa.engineDoFinal(messageBytes, 0, messageBytes.length);;
			String encryptmsg=new String(encryptMsg);
			
			System.out.println(encryptMsg.length);
		
			System.out.println("Encrypt msg: "+encryptmsg);
				
			rsa=new RSACipher(privateKey,Cipher.DECRYPT_MODE,1024);	
			rsa.engineInit(Cipher.DECRYPT_MODE, privateKey, secure);	
			
			byte [] decryptMsg=rsa.engineDoFinal(encryptMsg, 0, encryptMsg.length);	
			System.out.println(decryptMsg.length);

			for(int i=0;i<decryptMsg.length;i++)
			{
				if(decryptMsg[i]==0)
					continue;
				else{
					test.add(decryptMsg[i]);			
				}
			}
			int limit=test.size();
			
			byte[] test2 =new byte[limit];
			
			for(int i=0;i<test.size();i++)
				test2[i]=test.get(i);
			
			String decrypt=new String(test2,"UTF-8");
			System.out.println("Decrpyt = "+ decrypt);
			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			System.out.println("error");
		}		
	}

}
