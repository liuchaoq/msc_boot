package com.msc.common.util.encryption;


import lombok.Data;

@Data
public class  EncryptedString {

    public String key;//长度为16个字符

    public String iv;//长度为16个字符

    public EncryptedString() {
        String library = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String key = "";
        String iv = "";
        for (Integer i = 0; i < 16; i++) {
            Double randomPoz = Math.floor(Math.random() * library.length());
            key += library.substring(randomPoz.intValue(), randomPoz.intValue() + 1);
        }
        this.key = key;
        for (Integer i = 0; i < 16; i++) {
            Double randomPoz = Math.floor(Math.random() * library.length());
            iv += library.substring(randomPoz.intValue(), randomPoz.intValue() + 1);
        }
        this.iv = iv;
    }
}
