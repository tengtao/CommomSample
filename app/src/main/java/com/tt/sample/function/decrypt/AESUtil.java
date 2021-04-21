package com.tt.sample.function.decrypt;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * app和服务端对称性加密
 *
 * @auther: wangzw
 * @date: 2018/6/11 08:39
 * @description: aes加解密工具
 */
public class AESUtil {

    private static final String CODEING = "utf-8";
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 功能描述: aes加密
     *
     * @auther: wangzw
     * @date: 2018/6/11 8:29
     */
    public static String encrypt(String data, String key) {
        try {
            key = key.substring(0, 16);
            byte[] raw = key.getBytes(CODEING);
            SecretKeySpec secretKeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypted = cipher.doFinal(data.getBytes("utf-8"));
            return parseByte2HexStr(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 功能描述: aes解密
     *
     * @auther: wangzw
     * @date: 2018/6/11 8:36
     */
    public static String decrypt(String data, String key) {
        try {
            key = key.substring(0, 16);
            byte[] raw = key.getBytes(CODEING);
            SecretKeySpec secretKeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] encrypted1 = parseHexStr2Byte(data);
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, CODEING);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static String parseByte2HexStr(byte[] buf) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; ++i) {
            String hex = Integer.toHexString(buf[i] & 255);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        } else {
            byte[] result = new byte[hexStr.length() / 2];
            for (int i = 0; i < hexStr.length() / 2; ++i) {
                int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
                result[i] = (byte) (high * 16 + low);
            }
            return result;
        }
    }
}
