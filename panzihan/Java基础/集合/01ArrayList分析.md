# ArrayList分析

分析集合之前，先说以下数组。

数组是在内存中划分一块连续的地址空间用来进行元素的存储，它直接操作内存，所以性能要比集合类好一些，但有一个缺点是在初始化时必须指定数组大小，并且在后续操作中不能再更改数组的大小。

我们遇到更多的情况是初始化的时候，我们并不知道要存放多少个元素，我们希望初始时，不必指定大小，而容器后续根据元素的数量进行动态的扩容，而ArrayList刚好能满足这样的一个需求。它的底层是基于数组实现的，因此具有数组的特点，例如查询修改快，插入删除慢。我们去看看它究竟是怎么实现的。

## 1、成员变量

```java
//默认的初始化容量
private static final int DEFAULT_CAPACITY = 10;
//空对象数组
private static final Object[] EMPTY_ELEMENTDATA = {};
//默认的空对象数组
private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
//对象数组
transient Object[] elementData; 
//集合元素的个数
private int size;
```

## 2、构造函数

```java
//传入初始化容量的构造方法
public ArrayList(int initialCapacity) {
    if (initialCapacity > 0) {//新建对象数组
        this.elementData = new Object[initialCapacity];
    } else if (initialCapacity == 0) {//使用空数组对象
        this.elementData = EMPTY_ELEMENTDATA;
    } else {//抛出非法入参异常
        throw new IllegalArgumentException("Illegal Capacity: " +                  				initialCapacity);
    }
}

//不带参数的构造方法
public ArrayList() {
    //指向默认的空对象数组
    this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
}

//传入外部集合的构造方法
public ArrayList(Collection<? extends E> c) {
    elementData = c.toArray();
    if ((size = elementData.length) != 0) {//集合长度不为0的情况
     	//如果数组类型不为Object[]，转换为Object[]
        if (elementData.getClass() != Object[].class)
            elementData = Arrays.copyOf(elementData, size, Object[].class);
    } else {
        //集合长度未0，对象数组设置为空数组
        this.elementData = EMPTY_ELEMENTDATA;
    }
}
```

可以看到ArrayList的内部存储结构就是一个Object类型的数组，因此它可以存放任意类型的元素。在构造ArrayList的时候，如果传入初始大小那么它将新建一个指定容量的Object数组，如果不设置初始大小那么它将不会分配内存空间而是使用空的对象数组，在实际要放入元素时再进行内存分配。

## 3、方法

### 3.1、add方法

```java
//添加方法
public boolean add(E e) {
    //添加前先检查是否需要扩容，此时数组的最小长度为 size+1
    ensureCapacityInternal(size + 1);
    //将元素添加到对象数组中
    elementData[size++] = e;
    //返回true
    return true;
}

//添加元素到指定位置
public void add(int index, E element) {
    //检测index的范围（大于size或者小于0都将抛出异常）
    rangeCheckForAdd(index);
	//检查是否需要扩容
    ensureCapacityInternal(size + 1); 
    //移动数组元素的位置并将要添加的元素覆盖上去
    System.arraycopy(elementData, index, elementData, index + 1,
                     size - index);
    elementData[index] = element;
    size++;
}
```

### 3.2、romove方法

```java
//删除指定位置元素的方法
public E remove(int index) {
    //删除的元素位置不能大于size
    rangeCheck(index);
    modCount++;
    E oldValue = elementData(index);
    int numMoved = size - index - 1;
    if (numMoved > 0)
        //将index后面的元素向前挪动以为
        System.arraycopy(elementData, index+1, elementData, index,
                         numMoved);
    //将末尾的元素置空
    elementData[--size] = null; 
	//返回删除了的元素的
    return oldValue;
}

//删除指定元素对象的方法
public boolean remove(Object o) {
    if (o == null) {
        //如果传入的对象为空，遍历查找有没有为null的元素，如果有，删除该元素并返回true
        for (int index = 0; index < size; index++)
            if (elementData[index] == null) {
                fastRemove(index);
                return true;
            }
    } else {
        //如果传入的对象不为空，同样遍历查找有没有改对象，如果有，删除该元素并返回true
        for (int index = 0; index < size; index++)
            if (o.equals(elementData[index])) {
                fastRemove(index);
                return true;
            }
    }
    //如果查找不到该对象，返回false
    return false;
}
```

### 3.3、set方法 

```java
//修改方法
public E set(int index, E element) {
    //范围小于size
    rangeCheck(index);
	//保留要旧元素
    E oldValue = elementData(index);
    //替换成新元素
    elementData[index] = element;
    //返回旧元素
    return oldValue;
}
```

### 3.4、get方法

```java
//查找方法
public E get(int index) {
    //范围小于size
    rangeCheck(index);
	//返回指定位置的元素
    return elementData(index);
}
```

每次添加元素都会检查容量是否足够，不够的话，进行扩容。扩容的细节等下再说。

1. `add(E e)`：仅将这个元素添加到末尾，操作迅速。

2. `add(int index, E element)`:需要移动插入位置后的元素，涉及数组的复制，操作较慢。
3. `remove(int index)`:需要将删除位置后的元素向前移动，也会涉及数组的复制，操作较慢。
4. `remove(Object o)`:需要将删除位置后的元素向前移动，也会涉及数组的复制，操作较慢。

5. `set(int index, E element)`:直接对指定位置的元素进行修改，操作迅速。
6. ` get(int index)`:直接返回指定位置的元素，操作迅速。

由于查找和修改直接定位到元素的下标，不涉及元素的挪动和数组复制，所以比较快，而插入和删除需要涉及元素的挪动和数组的复制，所以比较慢。并且每次添加操作还可能进行数组扩容，也会影响性能。下面看看这样实现扩容。

### 3.5、数组扩容

```java
private void ensureCapacityInternal(int minCapacity) {
    //如果当前的对象数组还是默认的空对象数组
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        //比较默认容量和当前的最小容量，取比较大的那个。
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
    }
	//确定明确的容量
    ensureExplicitCapacity(minCapacity);
}

private void ensureExplicitCapacity(int minCapacity) {
    //对容器的操作次数加1
	modCount++;
    //如果最小容量大于当前对象数组的长度，进行扩容
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}

//扩容
private void grow(int minCapacity) {
    //记录旧容量
    int oldCapacity = elementData.length;
    //新数组容量为原数组的1.5倍
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    //检查新容量是否大于最小容量
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    //检查新容量是否大于最大数组容量
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    //进行数组拷贝
    elementData = Arrays.copyOf(elementData, newCapacity);
}
```

每次添加元素之前都会调用`ensureCapacityInternal(size + 1)` 进行集合容量检查。在这个方法内部会检查当前对象数组是不是默认的空数组，如果是，那就新建默认大小为10的Object数组；如果不是则证明当前集合已经被初始化过，那么就调用ensureExplicitCapacity方法检查当前数组的容量是否满足这个最小所需容量，不满足的话就调用grow方法进行扩容。在grow方法内部可以看到，每次扩容都是增加原来数组长度的一半，扩容实际上是新建一个容量更大的数组，将原先数组的元素全部复制到新的数组上，然后再抛弃原先的数组转而使用新的数组。

## 4、小结

1. ArrayList底层实现是基于数组的，因此对指定下标的查找和修改比较快，但是删除和插入操作比较慢。

2. 构造ArrayList时尽量指定容量，减少扩容时带来的数组复制操作。

3. 每次添加元素之前会检查是否需要扩容，每次扩容都是增加原有容量的一半。

4. 每次对下标的操作都会进行安全性检查，如果出现数组越界就立即抛出异常。

5. ArrayList的所有方法都没有进行同步，因此它不是线程安全的。

