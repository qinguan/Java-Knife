List、ArrayList、LinkList类
---

一、以集合作为参数构造ArrayList，为什么需要强制转换为Object对象?

<pre>
    <code>
    public ArrayList(Collection<? extends E> c) {
        elementData = c.toArray();
        if ((size = elementData.length) != 0) {
            // c.toArray might (incorrectly) not return Object[] (see 6260652)
            if (elementData.getClass() != Object[].class)
                elementData = Arrays.copyOf(elementData, size, Object[].class);
        } else {
            // replace with empty array.
            this.elementData = EMPTY_ELEMENTDATA;
        }
    }
    </code>
</pre>

若发现参数集合不是Object对象时，做了转Object的操作。原因如下：

    List<Object> l = new ArrayList<Object>(Arrays.asList("foo", "bar"));
    // Arrays.asList("foo", "bar").toArray() produces String[], 
    // and l is backed by that array
    
    l.set(0, new Object()); 
    // Causes ArrayStoreException, because you cannot put arbitrary 
    // Object into String[]


二、迭代的实现

ArrayList迭代基于两个内部类实现，Itr和ListItr。ListItr继承了Itr，新增了前向查询功能，即ListItr迭代器支持双向查询。迭代过程中通过维护一个内部修改次数计数字段，来进行长度校验，防止并发修改引起的读取错误。

LinkList迭代也是基于内部ListItr类实现的。

ArrayList和LinkList的内部ListItr实现机制不一，但逻辑大同小异，只不过一个是基于数组的操作，一个是基于链表节点的操作。

三、排序实现

ArrayList实际上委托Arrays.sort进行实现了，LinkList不支持排序。
