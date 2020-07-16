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

`T`可以是任意`class`，但不能是基本数据类型，所以这个`ArrayList`就成了一个模版，可以创建任意类型的`ArrayList`，由编译器对类型作出检查

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

这段代码先创建了一个`Integer`类型的`ArrayList`，添加了一个`Integer`数字，接着又转型成为`Number`类型的`ArrayList`，此时`intArrayList`和`numberList`两者的引用其实指向同一个地址，因此`numberList.add(new Float(100.1))`是合法的，但是`Integer n = intArrayList.get(1)`就会抛出类型转换异常`ClassCastException`，所以java编译器会在你写到向上转型的时候也就是`ArrayList<Number> numberList = intArrayList`，就会直接提示你`incompatible type`，类型不匹配

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
- 无法获取带有泛型的`Class`， 因为`Pair<String> p1 = new Pair<>()`和`Pair<Integer> p2 = new Pair<>()`其实是统一个类，`p1.getClass() == p2.getClass()`显示结果为`true`。
- 不能直接实例化`<T>`因为`new T()`会被擦拭成`new Object()`, 这样无法区分类型，所以会被编译器阻止，只能通过`Class.newInstance`来实现实例化泛型类(其实是利用反射)
- 导致不正确的覆写比如下面这个例子
```java
public class Pair<T> {
    public boolean equals(T t){
        return this == t;
    }
}
```
这个例子就会发生编译错误，因为 `equals(T t)` 会被擦拭成 `equals(Object t)` 而这个方法是继承自 `Objcet` 的，所以编译器会阻止一个实际会变成覆写的泛型方法，换个方法名字就可以了，要注意避开类似这样的方法。

#### **获取泛型类型**

之前说了，在泛型类中是无法获取泛型类型的，但是如果泛型类有子类，编译器为了能明确限制子类，就必须把父类的泛型类型传递给子类，因此在子类中，可以通过代码获得父类的泛型类型，如下代码

父类
``` java
class Pair<T> {
    private T first;
    private T last;
    public Pair(T first, T last){
        this.first = first;
        this.last = last;
    }
}
```

子类

``` java
class IntPair extends Pair <Integer> {
    public IntPair(Integer first, Integer last) {
        super(first, last);
    }
}
```

从子类中获取父类泛型类型

``` java
public class Main {
    public static void main(String[] args){
        Class<IntPair> clazz = IntPair.Class;
        Type t = clazz.getGenericSuperclass();
        if(t instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) t;
            Type[] types = pt.getActualTypeArguments();
            Type firstType = types[0];
            Class<?> typeClass = (Class<?>) firstType;
            System.out.println(typeClass); // 父类泛型类型 Integer
        }  
    }
}
```

#### **extends通配符**

我们已经知道，`Pair<Integer>`不是`Pair<Number>`的子类，因此在传递泛型类型的时候，无法像传递类那样使用多态，但是确实存在需要这样功能的场景，比如下面

静态方法

``` java
static void add(Pair<Number> p){
    Number first = p.getFirst();
    Number last = p.getLast();
}
```

泛型类

``` java
class Pair<T> {
    private T first;
    private T last;
    public T getFirst(){return this.first;}
    public T getLast(){return this.last;}
    public T setFirst(T first){this.first = first;}
    public T setLast(T last){this.last = last;}
}
```

在`add`传入`Pair<Number>`

``` java
add(new Pair<Number>(123,123));
```

此时我们观察发现add方法内部，其实使用`Number`的子类也同样满足方法，比如

``` java
add(new Pair<Integer>(100,200))
add(new Pair<Long>(100,200))
add(new Pair<Float>(100,200))
```

但是由于 `add` 方法定义的时候指定的类型是`Pari<Number>` 所以我们只能传如泛型类型是`Number`的`Pair`，那有办法让它更通用么？我们就需要使用上界通配符(Upper Bounds Wildcards)，写法`<? extends Number>`, 它表示接收`Number`和`Number`的子类，我们如下修改`add`

``` java
static void add(Pair<? extends Number> p){
    Number first = p.getFirst();
    Number last = p.getLast();
}
```

现在我们知道`add`方法内部使用的`Number`接收`getFirst()`，那如果使用`Integer`类型接收呢？

``` java
Integer first = p.getFirst();
```

上面的代码会编译出错，因为我们此时不知道`getFirst`的具体类型，只知道它会返回`Number`和`Number`的子类，因此不能使用`Integer`接收。

然后再来看一下`Pair<T>`的`set`方法，修改`add`如下

``` java
static void add(Pair<? extends Number> p){
    Number first = p.getFirst();
    Number last = p.getLast();
    p.setFirst(new Integer(100));
    p.setLast(new Integer(200));
}
```

此时我们调用`add`如下

``` java
add(new Pair<Integer>(100,200));
```

同样会得到一个编译错误，但是我们发现，`Pair`的泛型类型是`Integer`，`add`方法里调用的`setFirst`传入的也是`Integer`，应该不会出问题，但是之所以这个错误会在编译阶段爆出来，就是因为在定义`add`的时候，并不知道Pair的具体泛型类型，试想如果`Pair<Long>`被添加到`add`，那么`setFirst(new Integer(100))`就会导致一个`RunTimeException`，为了杜绝此类问题，所以在`add`方法内部，无法传递任何类型给`set`方法，除了`null`，换句话来说，此时的`add`方法，只会读取`Pair`的属性，不会写入，表示对`Pair`只读

限定T类型

除了在通配符做限制，我们也可一限制`T`类型，查看下面的代码

`public class Pair<T extends Number> {...}`

此时使用`Pair`的时候，是能定义为`Number`和`Number`的子类

``` java
Pair<Integer> p = new Pair<>(123);
Pair<Long> p = new Pair<>(123L);
Pair<String> p = new Pair<>("test"); // 编译错误
```

#### **super通配符**

`super`和`extends`相反，`<? super Number>`只允许传入`Number`和`Number`的父类，并且`<? super T>`签名的参数方法内部，只能调用`set`方法，但不允许使用`get`方法获取，与`extends`正好相反，原因在于在方法内部，无法使用子类接收不确定的父类(除了使用`Object`接收，`Object`是顶级父类)，所以在编译阶段阻止此类用法。

对比`Collections`类的`copy`方法

``` java
public class Collection {
    public static <T> void copy (List<? super T> dest, List<? extends T> src) {
        for(int i = 0; i < src.size(); i++){
            T t = src.get(i);
            dest.add(t);
        }
    }
}
```

它的作用是复制一个`List`，第一个参数`List<? super T>dest`，表示是要写入的对象，第二个参数`List<? extends T> src`，是数据来源，但是我们知道`super`表示只能写入，`extends`表示只能读取，因此如果在`copy`方法内部，意外的读取了`dest`或者意外写入了`src`，那么就会在编译阶段报出异常，这就是一个正确使用`super`和`extends`的例子

#### **无限定通配符**

除了 `<? extends T>`和 `<? super T>` 以外，还有无限定通配符 `?`，如下代码

``` java　
void sample (Pair<?> p) {}
```

但是这样的方法内部，`set`和`get`都不允许，也就是说不允许读也不允许写（除了写入`null`，和获取`Object`），此外`?`还有一个特点就是`Pair<?>` 是所有 `Pair<T>`的超类，也就是说所有的`Pair<T>`都可一向上转型为`Pair<?>`。

``` java
Pair<Integer> intP = new Pair<>(123);
Pair<?> p = intP; // 可以安全转型
```

#### **泛型和反射**

Java里有很多反射的API也是泛型，例如 `Class`, `Constructor`, 因此有如下代码

``` java
// 编译警告
Class clazz = String.class;
String str = (String) clazz.newInstance();

// 无编译警告
Class<String> clazz = String.class;
String str = clazz.newInstance();
```

当调用`Class`的`getSuperClass()`方法返回的类型实际上是`Class<? super T>`，同样构造方法也有类似的用法

``` java
Class<Integer> clazz = Integer.class;
Constructor<Integer> cons = clazz.getConstructor(int.class);
```

#### **泛型数组**

使用泛型数组要非常小心，因为在运行期间数组是没有泛型的，如果在编译阶段没有明确标注为泛型，那么很有可能出现在编译阶段没有问题，但是到了运行阶段出现问题，下面这段代码展示了不安全的使用泛型数组

``` java
Pair[] arr = new Pair[2];
Pair<String>[] ps = (Pair<String>[]) arr;
ps[0] = new Pair<String>("a", "b");
arr[1] = new Pair<Integer>(1, 2);
// 这时在原本类型是 Pair<String>类型的数组中，就出现了一个Pair<Integer>
Pair<String> p = ps[1];
String s = p.getFirst(); // ClassCastException
```

如果想要在编译阶段排除这样的问题，就必须减少引用，如下改写

``` java
Pair<String>[] ps = (Pair<String>[]) new Pair[2];
```

这样写就不存在`arr`引用，因此所有操作都是在`ps`上操作，编译器能够追踪到正确的类型从而进行检查。

想实例化泛型一样，我们也不能直接泛型创建泛型数组，下面代码会报出编译错误

``` java
public class myArray {
    T[] createArray(){
        return new T[5]; // 编译出错
    }
}
```

如果想创建泛型数组，必须借助`Class<T>`来实现，代码如下

``` java
T[] createArray(Class<T> cls){
    return (T[]) Array.newInstance(cls, 5);
}
```

由此可见，`T`不能直接用来实例化，只能用于转型，泛型数组还可以应用到函数的参数，如下代码

``` java
static <T> T[] asArry(T... objs) {
    return objs;
}
```

这段代码似乎创建了一个泛型数组，但实际上这样的做法是很危险的，检查下面的例子

``` java
public class Main {
    public static void main(String[] args){
        String[] arr = asArray("one", "tow", "three");
        System.out.println(Arrays.toString(arr));
        String[] firstString = pickTow("1", "2", "3"); // ClassCastException
        System.out.println(Arrays.toString(firstString));
    }
    
    static <K> K[] pickTow (K k1, K k2, K k3) {
        return asArray(k1, k2, k3);
    }
    
    static <T> T[] asArray (T... objs) {
        return objs;
    }
}
```

原因在于`pickTow`方法内部无法确定`K[]`的类型因此返回了`Object[]`， 但是`Object[]`不能被`String[]`接收，所以会抛出异常。


















