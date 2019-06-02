String 类
---

一、int类型数组转String会涉及到需要1个字char还是2个char来表示int的问题。

[Java内部使用UTF-16](https://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.1)，即一个char占16位，由2个字节表示。

<pre><code>
public String(int[] codePoints, int offset, int count){
    if Character.isBmpCodePoint... 判断是否是第0平面，则只需要1个char即可表示
    else if Character.isValidCodePoint...若是其他平面，则需要2个char。
}
</code></pre>

[Unicode各平面说明](https://www.sttmedia.com/unicode-basiclingualplane)



二、CaseInsensitiveComparator比较三次问题

<pre><code>
    private static class CaseInsensitiveComparator
            implements Comparator<String>, java.io.Serializable {
        // use serialVersionUID from JDK 1.2.2 for interoperability
        private static final long serialVersionUID = 8575799808933029326L;

        public int compare(String s1, String s2) {
            int n1 = s1.length();
            int n2 = s2.length();
            int min = Math.min(n1, n2);
            for (int i = 0; i < min; i++) {
                char c1 = s1.charAt(i);
                char c2 = s2.charAt(i);
                if (c1 != c2) {
                    c1 = Character.toUpperCase(c1);
                    c2 = Character.toUpperCase(c2);
                    if (c1 != c2) {
                        c1 = Character.toLowerCase(c1);
                        c2 = Character.toLowerCase(c2);
                        if (c1 != c2) {
                            // No overflow because of numeric promotion
                            return c1 - c2;
                        }
                    }
                }
            }
            return n1 - n2;
        }

        /** Replaces the de-serialized object. */
        private Object readResolve() { return CASE_INSENSITIVE_ORDER; }
    }
</code></pre>
原因在于有些字符转换大写是无效的，详见String.regionMatches方法的注释。
<pre><code>
    // Unfortunately, conversion to uppercase does not work properly
    // for the Georgian alphabet, which has strange rules about case
    // conversion.  So we need to make one last check before
    // exiting.
</code></pre>

更进一步的说明，详见[Simple_Loose_Matches](http://www.unicode.org/reports/tr18/#Simple_Loose_Matches)。

<pre><code>
    In addition, because of the vagaries of natural language, there are situations where two different Unicode characters have the same uppercase or lowercase. To meet this requirement, implementations must implement these in accordance with the Unicode Standard. For example, the Greek U+03C3 "σ" small sigma, U+03C2 "ς" small final sigma, and U+03A3 "Σ" capital sigma all match.
</code></pre>

三、split 和 matches

[split](http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/8u40-b25/java/lang/String.java#String.split%28java.lang.String%2Cint%29)
该方法支持按正则表达式分隔和常规字符进行分隔。
若常规字符包含在正则符号中，则需要使用'\\'进行转义。
若按正则表达式进行分隔，则会调用[Pattern.compile(regex)](http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/8u40-b25/java/util/regex/Pattern.java#Pattern.compile%28java.lang.String%29).split进行处理。

[matches](http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/8u40-b25/java/lang/String.java#String.matches%28java.lang.String%29)直接委托[Pattern.matches](http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/8u40-b25/java/util/regex/Pattern.java#Pattern.matches%28java.lang.String%2Cjava.lang.CharSequence%29)进行实现了。
