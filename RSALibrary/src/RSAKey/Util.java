package RSAKey;

import java.math.BigInteger;

import javax.crypto.*;

/**
 *	Utility class of static methods for use
 *	in RSA encryption and signature provider.
 */
public class Util {

	/**
	 *	Convert a BigInteger into a byte array
	 *	of length k.
	 *
	 *	@param	Input
	 *	@param	length of desired array
	 */
	public static byte[] I2OSP(BigInteger c, int k)
		throws IllegalBlockSizeException {
		
		byte[] C = c.toByteArray();
		// remove the leading zero if necessary
		if (C[0] == 0) {
			byte[] temp = new byte[C.length - 1];
			System.arraycopy(C,1,temp,0,temp.length);

			C = temp;
		}

		if (C.length > k) {
			throw new IllegalBlockSizeException("Block too large");
		}

		if (C.length == k) {
			return C;
		}
		// C is not long enough
		byte[] result = new byte[k];
		System.arraycopy(C,0,result,k-C.length,C.length);
		return result;
	}

	/**
	 *	Convert a byte array to a String of hex values
	 *	for printing.
	 *
	 *	@param	The byte array
	 *	@returns	String of hex values
	 */
	public static String bytesToHex(byte[] octets) {
		StringBuffer sb = new StringBuffer();
		boolean spacer = true;
		for (int k = 0; k < octets.length; ++k) {
			int i = octets[k];
			if (i < 0) {
				i += 256;
			}
			String hex = Integer.toHexString(i);
			if (spacer) {
				sb.append(' ');
			}
			spacer = !spacer;
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(i));
		}
		return sb.toString();
	}
}

