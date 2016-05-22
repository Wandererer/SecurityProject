package RSAKey;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGeneratorSpi;
import java.security.SecureRandom;

public class RSAKeyGenerator extends KeyPairGeneratorSpi {

	private static final BigInteger E = BigInteger.valueOf(65537);
	
	private static final int CERTAINTY = 85;
	
	private static final int DEFAULT_STRENGTH = 1024;
	
	private int myKeySize;
	private SecureRandom mySecureRandom;
	private boolean isInitialized= false;
	
	@Override
	public void initialize(int keysize, SecureRandom random) {
		myKeySize=keysize;
		mySecureRandom=random;
		isInitialized=true;
		
	}

	@Override
	public KeyPair generateKeyPair() {
		// TODO Auto-generated method stub
		if(!isInitialized)
			initialize(DEFAULT_STRENGTH,new SecureRandom());
		
		int pSize=myKeySize/2;
		
		int qSize=myKeySize-pSize;
		
		BigInteger p,q,pMinus1,qMinus1,pqMinus1Mul,n,d;
		
		do {	

			do {	
				p = new BigInteger(pSize, CERTAINTY, mySecureRandom);
				pMinus1 = p.subtract(BigInteger.ONE);
			} while (!(pMinus1.gcd(E).equals(BigInteger.ONE)));

			do {
				q = new BigInteger(qSize, CERTAINTY, mySecureRandom);
				qMinus1 = q.subtract(BigInteger.ONE);
			} while (!(qMinus1.gcd(E).equals(BigInteger.ONE)));
			

		} while ((p.multiply(q)).bitLength() != myKeySize);
		
		
		pqMinus1Mul = pMinus1.multiply(qMinus1);
		
		n = p.multiply(q);


		d = E.modInverse(pqMinus1Mul);


		return new KeyPair(
			      new RSAPublicKeyStore(n,E),
			      new RSAPrivateCtrKey(n,d,E,p,q)
		);
		
		
	}

}
