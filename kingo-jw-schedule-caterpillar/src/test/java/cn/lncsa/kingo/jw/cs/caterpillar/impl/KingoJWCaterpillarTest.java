package cn.lncsa.kingo.jw.cs.caterpillar.impl;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

/**
 * Created by cattenlinger on 2017/3/12.
 */
public class KingoJWCaterpillarTest {

    @Test
    public void md5Test() throws NoSuchAlgorithmException {
        String s = "14601120234";
        //String md5Crypt = Md5Crypt.md5Crypt(s.getBytes());
        //System.out.println(md5Crypt.toUpperCase());
        MessageDigest md = MessageDigest.getInstance("MD5");
        // 计算md5函数
        md.update(s.getBytes());
        // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
        System.out.println(new BigInteger(1, md.digest()).toString(16));
    }

    @Test
    public void encryptPasswordTest(){
        Assert.assertTrue(KingoJWCaterpillar.getEncryptedPassword("14601120234","14601120234").equals("B82AA43F8653B8E4420CA239DC1BA0"));
    }

}