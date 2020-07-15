## 泛型知识点

#### **什么是泛型？**

一种模版类型，可以把类型想参数一样传递，下面是一个泛型类的示例

定义

``` java
public class ArrayList<T> {
    private T[] arrry;
    private int size;
    private T get(int index){
        return ArrayList[i];
    }
}
```

使用

``` java
ArrayList<String> stringArrayList = new ArrayList<String>();
```

`T`可以是任意class，但不能是基本数据类型，所以这个ArrayList就成了一个模版，可以创建任意类型的ArrayList，由编译器对类型作出检查

#### **泛型有什么用？**

为了把很多运行期间的异常提前到编译阶段暴露出来，可以帮助封装代码，提高代码的通用型

#### **泛型的向上转型**

泛型的参数类型和继承没有关系，向上转型也不会收到参数类型的影响，比如`ArrayList<T>`可以向上转型`List<T>`，但是`ArrayList<Integer>`不能向上转型为`ArrayList<Number>`或者`List<Number>`。

试想一下

``` java
ArrayList<Integer> intArrayList = new ArrayList<>();
intArrayList.add(new Integer(100));
ArrayList<Number> numberList = intArrayList;
numberList.add(new Float(100.1);
Integer n = intArrayList.get(1););
```

这段代码先创建了一个Integer类型的ArrayList，添加了一个Integer数字，接着又转型成为Number类型的ArrayList，此时intArrayList和numberList两者的引用其实指向同一个地址，因此`numberList.add(new Float(100.1))`是合法的，但是`Integer n = intArrayList.get(1)`就会抛出类型转换异常`ClassCastException`，所以java编译器会在你写到向上转型的时候也就是`ArrayList<Number> numberList = intArrayList`，就会直接提示你`incompatible type`，类型不匹配

#### **泛型接口**

比如在`Array.sort(Object[])`方法可以给任意数组进行排序， 但是数组内的元素必须实现接口`Comparable<T>`，这个接口就是一个泛型接口

``` java
public interface Comparable<T> {
    int compareTo(T o);
}
```

那么现在有一个`Parson`类

``` java
class Parson {
    String name;

    Parson (String name) {
        this.name = name;
    }
}
```

然后生成一个`Parson`类的数组

``` java
Parson[] parsonArray = new Parson[] {
    new Parson("Bob"), new Parson("Alice"), new Parson("Mary")
}
```

我们对`parsonArray`进行排序

``` java
Array.sort(parsonArray)
```

然后会得到一个 `ClassCastException` 那是因为 `Parson` 类没有实现 `Comparable`接口，然后改写一下 `Parson`

 ``` java
class Parson implements Comparable<Parson> {
    String name;
    
    Parson (String name) {
        this.name = name;
    }

    public int compareTo(Parson other) {
        return this.name.compareTo(other.name);
    }
}
 ```

然后再次执行，就可以了

#### **静态方法**

泛型类型不能用于静态方法，例如下代码会编译错误

``` java
public class Pair<T> {
    private T first;
    private T last;
    public Pair (T first, T last) {
        this.first = first;
        this.last = last;
    }
    // 对静态方法使用<T> 对导致编译错误
    public static Pair<T> create (T first, T last){
        return new Pair<T>(first, last);        
    }
}
```

如果想使用带有泛型的 `create` 方法，必须改写

``` java
public class Pair<T> {
    private T first;
    private T last;
    public Pair (T first, T last) {
        this.first = first;
        this.last = last;
    }
    // 编译通过
    public static<K> Pair<K> create (K first, K last){
        return new Pair<K>(first, last);        
    }
}
``` 
由于静态方法被调用的时候，类还没有实例化，无法确定泛型类型，所以静态方法的泛型类型是按照参数类型来处理的。

#### **多个泛型类型**

下面是一个同时使用多个泛型类型的例子

``` java
public class Pair<T, V> {
    private T first;
    private V value;
    public Pair (T first, V value) {
        this.first = first;
        this.value = value;
    }
    public T getFirst(){ return this.first;}
    public V getFirst(){ return this.value;}
}
```

使用

``` java
Pair<String, Integer> pair = new Pair<>("String", "Integer");
```

#### **擦拭法**

Java 在实现泛型的时候采用的是擦拭法，当我们开发一个泛型类 `Pair<T>` 的时候，虚拟机根部不知道泛型这回事
因为编译器会把所有 `<T>` 都擦拭成 `Object`，只在需要转型的时候编译器会根据`<T>`类型自动安全转型，所以这也导致了一些问题，比如
- 泛型类型不能是基本数据类型，`Pair<int> p = new Pair<>(1,2);`， 类似这样的代码会编译错误。
- 无法获取带有泛型的`Class`， 因为`Pair<String> p1 = new Pair<>()`和`Pair<Integer> p2 = new Pair<>()`其实是统一个类，`p1.getClass() == p2.getClass()`显示结果为true。
- 不能直接实例化`<T>`因为`new T()`会被擦拭成`new Object()`, 这样无法区分类型，所以会被编译器阻止，只能通过`Class.newInstance`来实现实例化泛型类(其实是利用反射)

#### **获取泛型类型**
#### **extends通配符**
#### **super通配符**
#### **泛型和反射**
