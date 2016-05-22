package RSAKey;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.IllegalBlockSizeException;

public class RSA {
	
	public static BigInteger rsaEncrypt (RSAPublicKey publicKey, BigInteger m)
			throws IllegalBlockSizeException{
		
		BigInteger e = publicKey.getPublicExponent();
		BigInteger n = publicKey.getModulus();

		BigInteger nMinusOne = n.subtract(BigInteger.ONE);

		BigInteger c = m.modPow(e,n);
		
		return c;
	
	}
	
	
	public static BigInteger rsaDecrypt(RSAPrivateKey privateKey, BigInteger c) {

		if (!(privateKey instanceof RSAPrivateCtrKey)) {
			// Can't use the Chinese Remainer Theorum
			BigInteger d = privateKey.getPrivateExponent();
			BigInteger n = privateKey.getModulus();

			BigInteger m = c.modPow(d,n);

			return m;
		}
		// Use Chinese Remainder Theorum to speed up calculation
		RSAPrivateCtrKey privateCrtKey = (RSAPrivateCtrKey)privateKey;

		BigInteger p = privateCrtKey.getPrimeP();
		BigInteger q = privateCrtKey.getPrimeQ();
		BigInteger dP = privateCrtKey.getPrimeExponentP();
		BigInteger dQ = privateCrtKey.getPrimeExponentQ();
		BigInteger qInv = privateCrtKey.getCrtCoefficient();

		BigInteger m1 = c.modPow(dP,p);
		BigInteger m2 = c.modPow(dQ,q);

		// Let h = qInv(m1 - m2) mod p
		BigInteger h = m1.subtract(m2);
		h = h.multiply(qInv);
		h = h.mod(p);

		// m = m2 + hq
		BigInteger m = h.multiply(q);
		m = m.add(m2);

		return m;
	}
}
