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
}
