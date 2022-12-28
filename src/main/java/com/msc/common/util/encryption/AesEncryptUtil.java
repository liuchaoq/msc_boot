package com.msc.common.util.encryption;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.shiro.codec.Base64;
import org.springframework.stereotype.Component;

/**
 * Description AES CBC + BASE64加密
 *
 * @author
 * @date 14:58 2022/3/31
 */
@Component
public final class AesEncryptUtil {

    public static String NO_PADDING = "AES/CBC/NoPadding";

    private AesEncryptUtil() {}

    /**
     * 加密方法
     *
     * @param data
     *            要加密的数据
     * @param key
     *            加密key
     * @param iv
     *            加密iv
     * @return 加密的结果
     * @throws Exception
     *             异常
     */
    public static String encrypt(String data, String key, String iv, String padding) throws Exception {
        try {
            // "算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance(padding);
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyStr = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivStr = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.ENCRYPT_MODE, keyStr, ivStr);
            byte[] encrypted = cipher.doFinal(plaintext);
            return new String(Base64.encode(encrypted));
        } catch (Exception e) {
            throw new Exception("加密失败:" + e.getMessage());
        }
    }

    /**
     * 解密方法
     *
     * @param data
     *            要解密的数据
     * @param key
     *            解密key
     * @param iv
     *            解密iv
     * @return 解密的结果
     * @throws Exception
     *             异常
     */
    public static String desEncrypt(String data, String key, String iv, String padding) throws Exception {
        try {
            byte[] encrypted1 = Base64.decode(data);
            Cipher cipher = Cipher.getInstance(padding);
            SecretKeySpec keyStr = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivStr = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.DECRYPT_MODE, keyStr, ivStr);
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new Exception("解码失败:" + e.getMessage());
        }
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {

        if (hexStr.length() < 1) {
            return new byte[0];
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte)(high * 16 + low);
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            String data = encrypt("2c90027b8210b0b2018210b0b2330000","df1420cbbfb44103","1234567890hjlkew","AES/CBC/NOPADDING");
            System.out.println(desEncrypt(data,"df1420cbbfb44103","1234567890hjlkew","AES/CBC/NOPADDING"));
            EncryptedString encryptedString = new EncryptedString();
            System.out.println(encryptedString.getKey());
            System.out.println(encryptedString.getIv());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}