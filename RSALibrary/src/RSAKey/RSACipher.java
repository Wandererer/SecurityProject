package RSAKey;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

public class RSACipher extends CipherSpi {

	private Key myKey;
	
	private int opMode;
	
	private int myKeySize;
	
	private ByteArrayOutputStream myBaos;
	
	public RSACipher(){
		super();
		myBaos=new ByteArrayOutputStream();
	}
	
	public RSACipher(Key myKey,int opMode,int keysize){
		super();
		this.myKey=myKey;
		this.opMode=opMode;
		this.myKeySize=keysize;
		myBaos=new ByteArrayOutputStream();
	}
	

	
	@Override
	protected byte[] engineDoFinal(byte[] input, int inputOffset, int inputLen)
			throws IllegalBlockSizeException, BadPaddingException {
		// TODO Auto-generated method stub
		engineUpdate(input, inputOffset, inputLen);
		
		byte[] output;
		
		if(opMode==Cipher.ENCRYPT_MODE)
			output=encrypt();
		
		else
			output=decrypt();
		
		myBaos.reset();
		return output;

	
	}

	@Override
	protected int engineDoFinal(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset)
			throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
		// TODO Auto-generated method stub
		byte[] buffer;
		 buffer=engineDoFinal(input, inputOffset,inputLen );
		 if(output.length-outputOffset<buffer.length)
			 throw new ShortBufferException("Output longer than buffer");
		 
		// System.arraycopy(buffer, 0, output, outputOffset, buffer.length);
		 return buffer.length;
	}

	@Override
	protected int engineGetBlockSize() {

		if(opMode==Cipher.ENCRYPT_MODE)
			return myKeySize-1;
		
		else
			return myKeySize;
	}

	@Override
	protected byte[] engineGetIV() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int engineGetOutputSize(int inputLen) {
		if(opMode==Cipher.ENCRYPT_MODE)
			return myKeySize;
		
		else
			return myKeySize-1;
	}

	@Override
	protected AlgorithmParameters engineGetParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void engineInit(int opmode, Key key, SecureRandom random) throws InvalidKeyException {
		try{
			engineInit(opmode,key,(AlgorithmParameters)null,random);
		}catch(InvalidAlgorithmParameterException iape)
		{
			iape.printStackTrace();
		}	
	}

	@Override
	protected void engineInit(int opmode, Key key, AlgorithmParameterSpec params, SecureRandom random)
			throws InvalidKeyException, InvalidAlgorithmParameterException {
		
		 int modulusLength = 0;
	        if (key instanceof RSAPublicKey) {
				if (!(opmode == Cipher.ENCRYPT_MODE)) {
					throw new InvalidKeyException("Public Keys can only be used for encrypting");
				}
	            modulusLength = ((RSAPublicKey)key).getModulus().bitLength();
	        } else if (key instanceof RSAPrivateKey) {
				if (!(opmode == Cipher.DECRYPT_MODE)) {
					throw new InvalidKeyException("Private Keys can only be used for decrypting");
				}
	            modulusLength = ((RSAPrivateKey)key).getModulus().bitLength();
	        } else {
	            throw new InvalidKeyException("Key must be an RSA Key");
	        }

	        
	        myKeySize = (modulusLength+7)/8;
	
	        myKey = key;
	        opMode = opmode;
	        myBaos.reset();
	}

	@Override
	protected void engineInit(int opmode, Key key, AlgorithmParameters params, SecureRandom random)
			throws InvalidKeyException, InvalidAlgorithmParameterException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void engineSetMode(String mode) throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void engineSetPadding(String padding) throws NoSuchPaddingException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected byte[] engineUpdate(byte[] input, int inputOffset, int inputLen) {
	
		if(input!=null)
			myBaos.write(input, inputOffset, inputLen);
		
		return null;
	}

	@Override
	protected int engineUpdate(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset)
			throws ShortBufferException {
		engineUpdate(input, inputOffset, inputLen);
		return 0;
	}
	
	private byte[] encrypt() throws IllegalBlockSizeException{
		byte[] message= myBaos.toByteArray();
		int k=myKeySize;
		BigInteger m=new BigInteger(1,message);
		BigInteger c=RSA.rsaEncrypt((RSAPublicKey)myKey, m);
		byte[] C=c.toByteArray();
		
		return C;
	}
	
	private byte[] decrypt() throws IllegalBlockSizeException{
		byte[] C=myBaos.toByteArray();
		int k=myKeySize;
		//if(k!=C.length)
			//throw new IllegalBlockSizeException("decryption error");
		
		BigInteger c=new BigInteger (1,C);
		BigInteger m=RSA.rsaDecrypt((RSAPrivateKey)myKey, c);
		byte[] M=m.toByteArray();
		
		return M;
	}
	
	

}
