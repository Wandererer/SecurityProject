package RSAKey;

import java.security.AccessController;

public final class ICNJProvider extends java.security.Provider{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String NAME = "ICNJ";

	private static final double VERSION = 1.0;

	private static final String INFO = "ICNJ RSA Provider";

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ICNJProvider() {
		super(NAME, VERSION, INFO);
		AccessController.doPrivileged(new java.security.PrivilegedAction() {
			public Object run() {

				put("Cipher.RSA","RSAKey.RSACipher");

				put("KeyPairGenerator.RSA","RSAKey.RSAKeyPairGenerator");

				return null;
			}
		});
	}

}
