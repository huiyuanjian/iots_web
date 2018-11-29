package io.renren.common.utils;

import java.util.Random;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 
 * @描述: 担心UUID太low?又担心重复问题?用这个生成Token,绝对重复不了
 * @作者: yywboruo@163.com
 * @创建: 2018-10-1 下午19:17
 */
public final class TokenUtils {
    public static String getRandomToken() {
        String part1 = DigestUtils.md5Hex(UUID.randomUUID().toString());
        String part2 = DigestUtils.md5Hex(UUID.randomUUID().toString());
        String part3 = DigestUtils.md5Hex(UUID.randomUUID().toString());
        String token = part1.substring(0, part1.length() / 2)
                + part2.substring(0, part2.length() / (new Random().nextInt(part2.length()) + 1))
                + part3.substring(0, part3.length() / 5);
        // 怕了吧
        return DigestUtils.md5Hex(DigestUtils.md5Hex(token + "salt").substring(
                new Random().nextInt(new Random().nextInt(3) + 1)));
    }
}
