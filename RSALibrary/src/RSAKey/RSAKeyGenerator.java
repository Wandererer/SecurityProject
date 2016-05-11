package RSAKey;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGeneratorSpi;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;;

public class RSAKeyGenerator extends KeyGeneratorSpi {

	@Override
	protected SecretKey engineGenerateKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void engineInit(SecureRandom random) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void engineInit(AlgorithmParameterSpec params, SecureRandom random)
			throws InvalidAlgorithmParameterException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void engineInit(int keysize, SecureRandom random) {
		// TODO Auto-generated method stub
		
	}




}
