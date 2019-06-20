package cc.aveast.tool.string;

public class StringUtil {

    /**
     * 字符串填充，如果带填充字符串长度不短于填充长度。则直接返回待填充字符串
     * @param orig 待填充字符串
     * @param left 是否在左侧填充（如果是否，则在右侧填充）
     * @param length 填充后长度
     * @param padChar 填充字符
     * @return 填充后字符串
     */
    public static final String pad(String orig, boolean left, int length, char padChar) {
        if (orig.length() >= length) {
            return orig;
        }

        StringBuffer sb = new StringBuffer();
        if (left) {
            // left
            for (int i = 0; i < length - orig.length(); i++) {
                sb.append(padChar);
            }
            sb.append(orig);
        } else {
            // right
            sb.append(orig);
            for (int i = 0; i < length - orig.length(); i++) {
                sb.append(padChar);
            }
        }

        return sb.toString();
    }

    /**
     * 字符串比较，对null也支持
     * @param comp1
     * @param comp2
     * @return
     */
    public static final boolean equal(String comp1, String comp2) {
        if (isNotEmpty(comp1)) {
            return comp1.equals(comp2);
        } else {
            return isEmpty(comp2);
        }
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (null == str || "".equals(str.trim())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串是否不为空
     */
    public static boolean isNotEmpty(String str) {
        if (str != null && !"".equals(str.trim())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 将hex字符串转换成byte数组
     * @param hexString hex字符串
     * @return
     */
    public static byte[] hex2bytes(String hexString) {
        // 转换成大写
        hexString = hexString.toUpperCase();

        // 计算字节数组的长度
        char[] chars = hexString.toCharArray();
        byte[] bytes = new byte[chars.length / 2];

        // 数组索引
        int index = 0;

        for (int i = 0; i < chars.length; i += 2) {
            byte newByte = 0x00;

            // 高位
            newByte |= char2byte(chars[i]);
            newByte <<= 4;

            // 低位
            newByte |= char2byte(chars[i + 1]);

            // 赋值
            bytes[index] = newByte;

            index++;
        }
        return bytes;
    }

    /**
     * 将char转换成对应byte
     * @param ch char
     * @return byte
     */
    private static byte char2byte(char ch) {
        switch (ch) {
            case '0':
                return 0x00;
            case '1':
                return 0x01;
            case '2':
                return 0x02;
            case '3':
                return 0x03;
            case '4':
                return 0x04;
            case '5':
                return 0x05;
            case '6':
                return 0x06;
            case '7':
                return 0x07;
            case '8':
                return 0x08;
            case '9':
                return 0x09;
            case 'A':
                return 0x0A;
            case 'B':
                return 0x0B;
            case 'C':
                return 0x0C;
            case 'D':
                return 0x0D;
            case 'E':
                return 0x0E;
            case 'F':
                return 0x0F;
            default:
                return 0x00;
        }
    }
}
