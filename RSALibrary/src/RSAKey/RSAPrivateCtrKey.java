package RSAKey;

import java.math.BigInteger;

@SuppressWarnings("serial")
public class RSAPrivateCtrKey extends RSAPrivateKeyStore {
	
	
	private BigInteger e;	// e
	private BigInteger p;			// p
	private BigInteger q;			// q
	private BigInteger primeP;	// d mod (p-1), dP
	private BigInteger primeQ;	// d mod (q-1), dQ
	private BigInteger qInverse;	// (q^-1) mod p, qInv

	
	protected RSAPrivateCtrKey (
		BigInteger modulus,
		BigInteger privateExponent,
		BigInteger publicExponent,
		BigInteger primeP,
		BigInteger primeQ) 
	{

		super(modulus,privateExponent);

		this.e = publicExponent;
		this.p = primeP;
		this.q = primeQ;

		primeP = super.getPrivateExponent().mod(this.p.subtract(BigInteger.ONE));
		primeQ = super.getPrivateExponent().mod(this.q.subtract(BigInteger.ONE));
		qInverse = this.q.modInverse(this.p);
	}

	public BigInteger getPublicExponent() {
		return e;
	}

	public BigInteger getPrimeP() {
		return p;
	}

	public BigInteger getPrimeQ() {
		return q;
	}

	public BigInteger getPrimeExponentP() {
		return primeP;
	}

	public BigInteger getPrimeExponentQ() {
		return primeQ;
	}

	public BigInteger getCrtCoefficient() {
		return qInverse;
	}
}
