package io.renren.modules.iots.utils.sys;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Random;
import java.util.UUID;

/**
 * 系统工具类
 */
@Slf4j
public class SysUtils {

    /**
     * 获得当前的mac地址
     */
    public static String getMACAddress() throws Exception {
        InetAddress ia = InetAddress.getLocalHost();
        // 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();

        // 下面代码是把mac地址拼装成String
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            // mac[i] & 0xFF 是为了把byte转化为正整数
            String s = Integer.toHexString(mac[i] & 0xFF);
            sb.append(s.length() == 1 ? 0 + s : s);
        }

        // 把字符串所有小写字母改为大写成为正规的mac地址并返回
        return sb.toString().toUpperCase().replaceAll("-", "");
    }

    /**
     * 获得6位数长度的身份识别码
     */
    public static String getIdentifyingCode(){
        String[] chars = new String[] { "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z" };
        StringBuffer shortBuffer = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            shortBuffer.append(chars[RandomUtils.nextInt(36)%36]);
        }
        return shortBuffer.toString();
    }

    /**
     * 测试
     */
    public static void main(String args[]){
        int index = 100;
        while (index > 0){
            index--;
            System.out.println(getIdentifyingCode());
        }
    }
}
