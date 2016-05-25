package RSAKey;

import java.security.Provider;

public class RSAProvider extends Provider {

	private static final String NAME = "ICNJ";

	private static final double VERSION = 1.0;

	private static final String INFO = "ICNJ RSA Provider";

	
	protected RSAProvider() {
		super("RSA Cipher", 1.0, "RSA Cipher v1.0");
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
