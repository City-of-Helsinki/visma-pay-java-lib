package org.helsinki.vismapay.util;

import lombok.NonNull;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class AuthCodeCalculator {

	public static String calcAuthCode(@NonNull String secretKey, @NonNull String data) {
		return String.format("%032x", new BigInteger(1, calcHmacSha256(
				secretKey.getBytes(StandardCharsets.UTF_8),
				data.getBytes(StandardCharsets.UTF_8)
		))).toUpperCase();
	}

	private static byte[] calcHmacSha256(byte[] secretKey, byte[] data) {
		byte[] hmacSha256;

		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
			mac.init(secretKeySpec);
			hmacSha256 = mac.doFinal(data);
		} catch (Exception e) {
			throw new RuntimeException("Failed to calculate hmac-sha256", e);
		}
		return hmacSha256;
	}
}
