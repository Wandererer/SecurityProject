package RSAKey;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;

public class RSAPrivateKeyStore implements RSAPrivateKey {

	private BigInteger n,d;
	
	protected RSAPrivateKeyStore(BigInteger N,BigInteger D)
	{
		this.n=N;
		this.d=D;
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
	public BigInteger getPrivateExponent() {
		// TODO Auto-generated method stub
		return d;
	}

}
