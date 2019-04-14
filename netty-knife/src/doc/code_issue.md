代码阅读issue
=========

1. 越界判断(referring MathUtil.isOutOfBounds)
```Java
    public static boolean isOutOfBounds(int index, int length, int capacity) {
        return (index | length | (index + length) | (capacity - (index + length))) < 0;
    }
```
这里的越界判断先采用了一连串的按位或处理，而不是像下面一样直接使用容量和申请量做比较呢?
```
    public static boolean isOutOfBounds1(int index, int length, int capacity) {
        return capacity - (index + length) < 0;
    }
```
我们举几个case看看:
```Java
isOutOfBounds1(2,Integer.MAX_VALUE-1,4)
==> true
isOutOfBounds0(2,Integer.MAX_VALUE-1,4)
==> true

isOutOfBounds1(-1,1,5)
==> false
isOutOfBounds(-1,1,5)
==> true

isOutOfBounds1(0,-1,5)
==> false
isOutOfBounds(0,-1,5)
==> true

isOutOfBounds1(2,1,-2)
==> true
isOutOfBounds(2,1,-2)
==> true

isOutOfBounds1(2,0,2)
==> false
isOutOfBounds(2,0,2)
==> false
```


从上面几个case中，我们可以看到，isOutOfBounds方法其实实现了入参数据的有效性判断。
