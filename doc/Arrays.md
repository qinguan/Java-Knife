Arrays类
---

一、Sort

Arrays中的Sort主要基于[mergeSort](http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/8u40-b25/java/util/Arrays.java#Arrays.mergeSort%28java.lang.Object%5B%5D%2Cjava.lang.Object%5B%5D%2Cint%2Cint%2Cint%29)和[TimSort](http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/8u40-b25/java/util/TimSort.java#TimSort.sort%28java.lang.Object%5B%5D%2Cint%2Cint%2Cjava.util.Comparator%2Cjava.lang.Object%5B%5D%2Cint%2Cint%29)实现。

mergeSort的方法有两个，一个自带参数Comparator，一个不带(即排序对象可转化为Comparable对象)。
针对小数组排序，mergeSort采用插入排序算法进行优化。
在参数赋值方面，令low = -off，则能保证运算过程中的low值始终为0。

mergeSort会在将来的发布版本中删除。
<pre>
    <code>
    public static <T> void sort(T[] a, Comparator<? super T> c) {
        if (c == null) {
            sort(a);
        } else {
            if (LegacyMergeSort.userRequested)
                legacyMergeSort(a, c);
            else
                TimSort.sort(a, 0, a.length, c, null, 0, 0);
        }
    }
    
    ...

    private static void mergeSort(Object[] src,
                                  Object[] dest,
                                  int low, int high, int off,
                                  Comparator c) {
        int length = high - low;

        // Insertion sort on smallest arrays
        if (length < INSERTIONSORT_THRESHOLD) {
            for (int i=low; i<high; i++)
                for (int j=i; j>low && c.compare(dest[j-1], dest[j])>0; j--)
                    swap(dest, j, j-1);
            return;
        }

        // Recursively sort halves of dest into src
        int destLow  = low;
        int destHigh = high;
        low  += off;
        high += off;
        int mid = (low + high) >>> 1;
        mergeSort(dest, src, low, mid, -off, c);
        mergeSort(dest, src, mid, high, -off, c);

        // If list is already sorted, just copy from src to dest.  This is an
        // optimization that results in faster sorts for nearly ordered lists.
        if (c.compare(src[mid-1], src[mid]) <= 0) {
           System.arraycopy(src, low, dest, destLow, length);
           return;
        }

        // Merge sorted halves (now in src) into dest
        for(int i = destLow, p = low, q = mid; i < destHigh; i++) {
            if (q >= high || p < mid && c.compare(src[p], src[q]) <= 0)
                dest[i] = src[p++];
            else
                dest[i] = src[q++];
        }
    }
    </code>
</pre>


二、binarySearch0

参数a 和 key 针对不同的基本类型以及Object各有一个binarySearch0方法，代码基本一样。

<pre>
    <code>
    private static int binarySearch0(int[] a, int fromIndex, int toIndex,
                                     int key) {
        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = a[mid];

            if (midVal < key)
                low = mid + 1;
            else if (midVal > key)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found.
    }
    </code>
</pre>
