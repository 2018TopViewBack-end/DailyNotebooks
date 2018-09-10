# LinkedList分析

LinkedList是List接口的另一种实现，它的底层是基于双向链表实现的，因此它具有插入删除快而查找修改慢的特点。

## 1、结点内部类

```java
private static class Node<E> {
    E item;//元素
    Node<E> next;//下一个结点
    Node<E> prev;//上一个结点

    Node(Node<E> prev, E element, Node<E> next) {
        this.item = element;
        this.next = next;
        this.prev = prev;
    }
}
```

## 2、成员变量

```java
//集合元素个数
transient int size = 0;

//头结点引用
transient Node<E> first;

//尾结点引用
transient Node<E> last;
```

## 3、构造器

```java
//无参构造器
public LinkedList() {
}
//传入外部集合的构造器
public LinkedList(Collection<? extends E> c) {
    this();
    addAll(c);
}
```

## 4、增删改查方法

```java
//增加
public boolean add(E e) {
    //在链表尾部添加
    linkLast(e);
    return true;
}

//增加（插入）
public void add(int index, E element) {
    checkPositionIndex(index);
	//在链表尾部添加
    if (index == size)
        linkLast(element);
    //在链表中部插入
    else
        linkBefore(element, node(index));
}

//删除（给定下标）
public E remove(int index) {
    //检查下标是否合法
    checkElementIndex(index);
    return unlink(node(index));
}

//删除（给定元素）
public boolean remove(Object o) {
    //遍历链表，找到就删除
    if (o == null) {
        for (Node<E> x = first; x != null; x = x.next) {
            if (x.item == null) {
                unlink(x);
                return true;
            }
        }
    } else {
        for (Node<E> x = first; x != null; x = x.next) {
            if (o.equals(x.item)) {
                unlink(x);
                return true;
            }
        }
    }
    return false;
}

//改
public E set(int index, E element) {
    //检查下标是否合法
    checkElementIndex(index);
    //获取指定下标的结点引用
    Node<E> x = node(index);
    //获取指定下标的结点的值
    E oldVal = x.item;
    //将结点元素设置为新的值
    x.item = element;
    //返回之前元素的值
    return oldVal;
}

//查
public E get(int index) {
    //检查下标是否合法
    checkElementIndex(index);
    //返回指定下标的结点的值
    return node(index).item;
}
```

## 5、检查下标方法

```java
//检查元素下标的方法
private void checkElementIndex(int index) {
    if (!isElementIndex(index))
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
}

private boolean isElementIndex(int index) {
    return index >= 0 && index < size;
}

//检查插入位置下标的方法
private void checkPositionIndex(int index) {
    if (!isPositionIndex(index))
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
}

private boolean isPositionIndex(int index) {
    return index >= 0 && index <= size;
}
```

## 6、LinkLast（）和LinkBefore（）

LiskedList添加元素的方法主要是调用linkLast（）和LinkBefore（）两个方法。

```java
/**
 * 在链表末尾添加一个元素
 */
void linkLast(E e) {
    final Node<E> l = last;
    final Node<E> newNode = new Node<>(l, e, null);
    last = newNode;
    if (l == null)
        first = newNode;
    else
        l.next = newNode;
    size++;
    modCount++;
}
```

7、unlink()2

```java
E unlink(Node<E> x) {
    // assert x != null;
    final E element = x.item;
    final Node<E> next = x.next;
    final Node<E> prev = x.prev;

    if (prev == null) {
        first = next;
    } else {
        prev.next = next;
        x.prev = null;
    }

    if (next == null) {
        last = prev;
    } else {
        next.prev = prev;
        x.next = null;
    }

    x.item = null;
    size--;
    modCount++;
    return element;
}
```





而对链表的查找和修改操作都需要遍历链表进行元素的定位，这两个操作都是调用的node(int index)方法定位元素，看看它是怎样通过下标来定位元素的。

```java
Node<E> node(int index) {
    // assert isElementIndex(index);

    if (index < (size >> 1)) {
        Node<E> x = first;
        for (int i = 0; i < index; i++)
            x = x.next;
        return x;
    } else {
        Node<E> x = last;
        for (int i = size - 1; i > index; i--)
            x = x.prev;
        return x;
    }
}
```

通过下标定位时先判断是在链表的上半部分还是下半部分，如果是在上半部分就从头开始找起，如果是下半部分就从尾开始找起，因此通过下标的查找和修改操作的时间复杂度是O(n/2)。

```java
public E peek() {
    final Node<E> f = first;
    return (f == null) ? null : f.item;
}
```

双向队列操作：

```java
public boolean offerFirst(E e) {
    addFirst(e);
    return true;
}
```

栈操作：

```java
public void push(E e) {
    addFirst(e);
}

/**
 * Pops an element from the stack represented by this list.  In other
 * words, removes and returns the first element of this list.
 *
 * <p>This method is equivalent to {@link #removeFirst()}.
 *
 * @return the element at the front of this list (which is the top
 *         of the stack represented by this list)
 * @throws NoSuchElementException if this list is empty
 * @since 1.6
 */
public E pop() {
    return removeFirst();
}
```

不管是单向队列还是双向队列还是栈，其实都是对链表的头结点和尾结点进行操作，它们的实现都是基于addFirst()，addLast()，removeFirst()，removeLast()这四个方法，它们的操作和linkBefore()和unlink()类似，只不过一个是对链表两端操作，一个是对链表中间操作。可以说这四个方法都是linkBefore()和unlink()方法的特殊情况，因此不难理解它们的内部实现。















## 小结

1. LinkedList是基于双向链表实现的，不论是增删改查方法，还是队列和栈的实现，都可通过操作接结点实现。
2. LinkedList无需提前指定容量，因为基于链表操作，集合的容量随着元素的加入自动增加。
3. LinkedList删除元素后集合占用的内存自动缩小，无需向ArrayList一样调用trimSize（）方法。
4. LinkedList的所有方法没有进行同步，因此它也不是线程安全的，应该避免在多线程环境下使用。