package RSAKey;

import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;

public class RSAPublicKeyStore implements RSAPublicKey {

	private BigInteger n,e;
	
	protected RSAPublicKeyStore(BigInteger N,BigInteger E)
	{
		this.n=N;
		this.e=E;
	}
	
	@Override
	public String getAlgorithm() {
		// TODO Auto-generated method stub
		return "RSA";
	}

	@Override
	public String getFormat() {
		// TODO Auto-generated method stub
		return "NONE";
	}

	@Override
	public byte[] getEncoded() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger getModulus() {
		// TODO Auto-generated method stub
		return n;
	}

	@Override
	public BigInteger getPublicExponent() {
		// TODO Auto-generated method stub
		return e;
	}

}
