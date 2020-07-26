## java集合知识点

*2020-07-19 Dyinfalse* 本篇是阅读[廖雪峰文章](https://www.liaoxuefeng.com/wiki/1252599548343744)的总结

#### **什么是集合？**

由若干个确定的元素构成的一个整体，称为一个集合，在Java中，如果一个对象可以在内部持有若干其他对象，并对外部提供访问接口，就可以成为Java集合，Java数组就是一个例子

```java
public class StringArray {
    public static void main(String[] args){
        String[] s = new String[10];
        s[0] = "Hello";
        System.out.println(s[0]);
    }
}
```
因为每种集合都有自己的优缺点以满足不同的业务场景，所以集合的种类非常多，后面会一一介绍。

#### **Collection**

在Java的标准库中提供了`java.util.Collection`类，除了`Map`以外，其他所有的集合都是其派生类，也就是说`Collection`类是其他集合的跟接口。在`java.util`中，主要提供了`List`,`Set`,`Map`三种集合。

- `List`是一种有序列表的集合，每个元素有索引，索引从`0`开始递增
- `Set`是一种保证无重复元素的集合
- `Mao`是一种通过键值对查找表映射的方式存贮元素

Java的集合设计特点

- 实现类接口和实现类总是分离，例如有序列表的接口`List`，其实现类包括`ArrayList`,`LinkedList`
- 总是支持泛型，在泛型篇也讲过，集合使用泛型是有助于正确使用集合的，在编译阶段提前暴露问题
- 访问集合总是通过统一的迭代器`Iterator`来实现

Java的集合历史非常悠久，有很多集合类是遗留类，不应该继续使用

- `Hashtable`一种线程安全的`Map`实现
- `Vector`一种线程安全的`List`实现
- `Stack`基于`Vector`实现的`Lifo`的栈
- `Enumeration<E>`一种实现集合的接口，已经被`Iterator<E>`替代

#### **使用List**

`List`是一种有序列表，作为最基础的集合类和数组的行为基本相同，`List`按照元素的放入顺序存放，每个元素都可以通过索引来被访问，和数组一样`List`的索引从`0`开始，数组和`List`都是有序结构，但是我们使用数组的时候如果想增加元素，或者删除元素，由于数组的长度是固定的，因此会非常麻烦，所以在需要增删元素的场景的时候，最好使用`ArrayList`，因为`ArrayList`把常用的一些`List`方法都已经实现，下面查看`List`接口的主要方法。

- `boolean add(E e)`在末尾追加一个元素
- `boolean add(int index, E e)`在指定位置插入一个元素
- `int remove(int index)`删除指定位置元素
- `int remove(Object o)`删除指定元素
- `E get(int index)`获取指定位置元素
- `int size()`获取有效元素个数

`ArrayList`并非是`List<E>`的唯一实现方式，还有一种链表`LinkedList`, 也实现了`List<E>`接口，其结构如下
```
LinkedList
        ┌───┬───┐   ┌───┬───┐   ┌───┬───┐   ┌───┬───┐
HEAD ──>│ A │ ●─┼──>│ B │ ●─┼──>│ C │ ●─┼──>│ D │   │
        └───┴───┘   └───┴───┘   └───┴───┘   └───┴───┘

ArrayList
┌───┬───┬───┬───┬───┬───┬──┐
│ A │ B │ F │ C │ D │ E │  │
└───┴───┴───┴───┴───┴───┴──┘
```

下面对比一下`ArrayList`和`LinkedList`

|操作|ArrayList|LinkedList|
|----|---------|----------|
|获取指定元素|速度很快|需要从头开始查找|
|添加元素到末尾|速度很快|速度很快|
|在指定位置添加/删除|需要移动元素|不用移动元素|
|内存占用|少|较大|

一般情况下，总是优先使用`ArrayList`

#### **List的特点**

`List`允许添加重复的元素，也可以添加`null`，代码如下
```java
public class Main {
    public static void main(String[] args){
        List<String> list = new ArrayList<>();
        list.add("apple");
        list.add(null);
        list.add("apple");
    }
}
```
除了使用`ArrayList`和`LinkedList`创建`List`，还可以使用`List.of()`创建`List`
```java
public class Main {
    public static void main(String[] args){
        List<Integer> list = List.of(1,2,3);     
    }
}
```
但是`List.of()`不允许传入`null`，会抛出`NullPointerException`

#### **List遍历**

基本的遍历方式是使用`for`循环配合`get(int)`遍历

```java
public class Main {
    public static void main(String[] args){
        List<Integer> list = List.of(1,2,3,4);
        for(int i = 0; i < list.size(); i++){
            Integer integer = list.get(i);
        }
    }
}
```

但是这样的遍历并不推荐，因为代码很复杂，并且`List.get(int)`方法只有`ArrayList`的实现比较高效，对于`LinkedList`来说，索引越大，访问的速度越慢，所以我们使用应该使用迭代器`Iterator`来访问`List`，`Iterator`本身也是个对象，但是它由`List`的实例，调用`iterator()`方法来创建，`Iterator`会知道如何遍历一个`List`，并且不容的`List`，为了保证效率最高，返回的`Iterator`也不同。

`Iterator`提供两个方法，`boolean hasNext()`判断是否有下一个元素，`E next()`返回下一个元素，使用`Iterator`遍历`List`的写法如下

```java
public class Main {
    public static void main(String[] args){
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        for(Iterator<Integer> it = list.iterator(); it.hasNext();){
            Integer i = it.next();
        }
    }
}
```
由于`Iterable`遍历非常常用，Java的`for each`循环本身就使用了`Iterator`来遍历，所以上面代码可改写为
```java
public class Main {
    public static void main(String[] args){
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        for (Integer i : list) {
            System.out.println(i);
        }
    }
}
```
任何实现了`Iterable`的集合都可以使用`for each`来遍历，因为`Iterable`接口定义了`Iterator<E> iterator()`方法，强迫集合返回一个`Iterator`实例

#### **List和ArrayList的转换**

第一种转换方法，使用`toArray()`
```java
public class Main {
    public static void main(String[] args){
        List<Integer> list = List.of(1, 2, 3, 4);
        Object[] array = list.toArray();
        for(Object s: array){
            System.out.println(s);           
        }
    }
}
```
这种做法会丢失类型的信息，所以并不实用

第二种是给`toArray(T[])`传入一个类型相同的`Array`，`List`内部自动把元素复制到传入的`Array`中
```java
public class Main {
    public static void main(String[] args){
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        Integer[] array = list.toArray(new Integer[5]);
        for (Integer i : array){
            System.out.println(i);
        }
    }
}
```
但是返回`array`的类型`<T>`，和原本定义在`List<E>`的类型`<E>`，可以不一样，例如我们改写为`Number`类型的数组，那么返回的就是`Number`类型
```java
public class Main {
    public static void main(String[] args){
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        Number[] array = list.toArray(new Number[5]);
        for (Number i : array){
            System.out.println(i);
        }
    }
}
```
加入传入了类型不匹配的数组，例如`String[]`类型的数组，因为最开始`List`的元素类型是`Integer`，所以无法放入`String`的数组，这个方法就会抛出`ArrayStoreException`。

如果传入的数组长度不够大，那么在`List`内部会重新创建一个刚好够大的数组，填充之后返回，如果比传入的数组长度比`List`要大，那么填充完毕之后，会把剩下的位置填充为`null`

我们完全可以传入一个大小刚好的数组
```java
public class Main {
    public static void main(String[] args){
        List<Integer> list = List.of(1,2,3,4,5);
        Integer[] array = list.toArray(new Integer[list.size()]);
    }
}
```

最后一种是通过`List`接口定义的`T[] toArray(IntFunction<T[]> generator)`方法
```java
public class Main {
    public static void main(String[] args){
        List<Integer> list = List.of(1,2,3,4,5);
        Integer[] array = list.toArray(Integer[]::new);
    }
}
```

下面是把数组转变成`List`，可以通过`List.of(T...)`方法
```java
public class Main {
    public static void main(String[] args){
        Integer[] array = {1,2,3};
        List<Integer> list = List.of(array);
    }
}
```
如果在JDK 11之前，还可以使用`Array.asList(T...)`把数组转成`List`，值得一提的是因为`Link`只是一个接口，所以我们调用`Link.of()`的时候，返回的实际上是一个只读数组，在对只读数组操作的时候例如`add()`，`remove()`就会抛出`UnsupportedOperationException`

#### **List查找元素**

既然`List`是个容器，能存储当然也可以获取和对比，`List`提供了`boolean cantains(Object)`方法判断数组是否包含某个元素和`int indexOf(Object)`来查找元素在数组中的索引，下面介绍一个用法

```java
public class Main {
    public static void main(String[] args){
        List<String> list = List.of("H", "E", "L", "L", "O");
        System.out.println(list.contains("C")); // false
        System.out.println(list.contains("E")); // true
        System.out.println(list.indexOf("C"));  // -1
        System.out.println(list.indexOf("E"));  // 1
    }
}
```
在`contains`和`indexOf`内部，对比元素的时候，并不是使用的`==`，而是使用`equals`，包括我们在以后的Java开发中，也要使用`equals`来判断引用数据类型，数组中的元素必须正确实现`equals`才可以正确使用`contains`，`indexOf`方法，基本包装类型如`String`，`Integer`都已经帮我们实现了，所以可以直接用，如果数组中的元素是我们自己定义的呢?

```java
public class Main {
    public static void main(String[] args){
        List<Person> list = List.of(
            new Person("Mike"),
            new Person("Mary"),
            new Person("Bob")
        );
        list.contains(new Person("Bob")); // false
    }
}

public class Person {
    String name;
    public Person (String name) {
        this.name = name;
    }
}
```

如果数组中元素是我们自己定义的，那么就需要我们自己去实现一个`equals`，编写`equals`方法，有以下几点需要注意：

- 自反性(Reflexive)，对于非`null`的元素来说，`x.equals(x)`，必须返回`true`
- 对称性(Symmetric)，对于非`null`的元素来说，如果`x.equals(y)`等于`true`，那么`y.equals(x)`也必须为`true`
- 传递性(Transitive)，对于非`null`的元素来说，如果想`x.equals(y)`是`true`，并且`y.equals(z)`也是`true`，那么`x.equals(z)`也必须是`true`
- 一致性(Consistent)，对于非`null`的元素来说，只要`x`，`y`状态不变，则`x.equals(y)`，总是一致返回`true`或`false`

在给`Person`编写`equals`方法之前，要先明确相等的逻辑含义，及什么样的两个`Person`可以视为相等？所以我们假设，`name`和`age`都相等的两个`Person`类是相等的，于是可以编写下面这样的`equals`

```java
public class Person {
    String name;
    Integer age;
    public Person (String name, Integer age){
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals (Object o){
        if(o instanceof Person){
            Person p = (Person) o;
            return this.name.equals(p.name) && this.age.equals(p.age);
        }
        return false;
    }   
}
```

如果`this.name`或`this.age`是`null`的时候，上面的方法会报错，所以需要使用`Object.equals`静态方法继续修改

``` java
@Override
public boolean equals (Object o){
    if(o instanceof Person){
        Person p = (Person) o;
        return Object.equals(this.name, p.name) && Object.equals(this.age, p.age);
    }
    return false;
}
```

如果不需要使用`List`的`contains()`和`indexOf()`的时候，不需要实现放入元素的`equals`

#### **使用Map**

我们已经知道`List`是一种顺序列表，如果有一组`Student`，我们需要根据名字快速查找对应的学生成绩，如果使用`List`，那么代码如下

```java
public class Main {
    public static void main(String[] args){
        List<Student> list = List.of(
                new Student("XiaoMing", 60),
                new Student("XiaoHong", 80),
                new Student("XiaoHua", 90));
        Student target;
        for(Student s: list){
            if("XiaoMing".equals(s.name)){
                target = s;
                break;
            }
        }
        System.out.println(target.score);
    }
}

public class Student {
    String name;
    Integer score;
    public Student (String name, Integer score) {
        this.name = name;
        this.score = score;
    }
}
```

这个需求非常常见，但是使用`List`的话，平均查询次数是数组长度的一般，而这种时候就需要使用`Map`，`Map`是一种通过键值映射表的数据结构，作用就是高效的通过`key`快速查找`value`，下面使用`Map`重写这个需求

```java
public class Main {
    public static void main(String[] args){
        Map<String, Student> map = new HashMap<>();
        // 建立映射关系
        map.put("XiaoMing", new Student("XiaoMing", 60)); 
        map.put("XiaoHong", new Student("XiaoHong", 80));
        map.put("XiaoHua", new Student("XiaoHua", 90));
        
        System.out.println(map.get("Xiaohua").score);
    }
}
```

当使用`Map.get(K key)`方法时，如果`key`不存在，则返回`null`，和`List`类似，`Map`也只是一个接口，最常用的实现类是`HashMap`，另外如果想查找一个`key`是否在`Map`中存在，使用`boolean containsKey(K key)`方法，`Map`中的`key`是唯一的，也就是不存在重复的`key`

```java
public class Main {
    public static void main(String[] args){
        Map<String, Integer> map = new HashMap<>();
        map.put("Mick", 100);
        map.put("Mick", 200);
        System.out.println(map.get("Mick")); // 200
    }
}
```
上面代码`put`了两个`Mick`，可以看出，第二次`put`的`value`把第一`put`的给冲掉了，也就是说，在`Map`中重复添加相同`key`的映射，`value`以最后一次添加的为准。

#### **遍历Map**

遍历`Map`的时候，可以使用`for each`循环遍历`Map`实例的`keySet`方法返回的`Set`集合，它包含不重复的`key`

```java
public class Main {
    public static void main(String[] args){
        Map<String, Integer> map = new HashMap<>();
        map.put("apple", 100);
        map.put("pear", 200);
        for(String key : map.keySet()){
            Integer value = map.get(key);
            System.out.println(key + "=" + value);
        }
    }
}
```

如果想同时遍历`key`和`value`，可以使用`for each`循环遍历`Map`实例的`entrySet()`方法返回的集合，它包含每一个`key-value`映射

```java
public class Main {
    public static void main(String[] args){
        Map<String, Integer> map = new HashMap<>();
        map.put("apple", 100);
        map.put("pear", 200);
        for(Map.Entry<String, Integer> entry : map.entrySet()){
            String key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println(key + "=" + value);
        }
    }
}
```
重要的一点：遍历`Map`的时候，不能假设`key`是有序的

#### **Map的equals和hashCode**

`HashMap`之所以能根据`key`直接拿到`value`，原因是内部通过空间换时间的方法，用一个大数组存贮所有的`value`，并且根据`key`计算得出对应的索引，如果`key`是`"a"`，那么得到的索引总是`1`，因此从数组中找到的`value`意识固定的，这样就不用遍历整个数组就可以直接读取`key`对应的`value`。

我们放入的字符串`"a"`，当我们获取`value`的时候，传入的变量不一定就是当时放入的那个`key`，也就是说，两个`key`内容相同，但并不是同一个对象。类似`List`中的元素，在查找的做对比的做法，`Map`的`key`也需要正确的实现一个`equals`，用来对比`key`

```java
public class Main {
    public static void main(String[] args){
        String k1 = "a";
        Map<String, Integer> map = new HashMap<>();
        map.put(k1, 100);

        String k2 = new String("a");
        map.put(k2, 200);
        System.out.println(k1 == k2); // false
        System.out.println(k1.equals(k2)); // true
    }
}
```
其实在`Map`内部对比`key`是否相同是通过`equals`，所以如果我们放入了自己定义的类型作为`key`，那么也需要自己腹泻`equals`方法，以确`Map`能够正确的比较。

`HashMap`为什么能够通过`key`计算出`value`存贮的索引呢？`Map`是通过`hashCode()`方法来实现这个功能的，它返回一个`int`，就是`value`对应的索引位置，因此正确的使用`Map`就必须保证：

- 作为`key`的对象必须正确覆写`equals()`方法，相等的两个`key`调用`equals`必须返回`true`
- 作为`key`的对象必须正确覆写`hashCode()`方法，并且`hashCode()`方法必须严格遵守规范，及：
    - 如果两个对象相等，则两个对象的`hashCode()`结果必须相等
    - 如果两个对象不想等，则两个对象的`hashCode()`结果尽量不要相等

如何正确覆写`equals()`之前已经已经讲过，把需要比较的字段抽出来，引用类型使用`Objects.equals()`，基本类型使用`==`，在这个基础上，我们还需要正确的覆写`hashCode()`，以`Person`类为例

```java
public class Person {
    String firstName;
    String lastName;
    int age;
    
    @Override
    int hashCode () {
        int h = 0;
        h = 31 * h + this.firstName.hashCode();
        h = 31 * h + this.lastName.hashCode();
        h = 31 * h + this.age;
        return h;
    }
}
```

因为`String`类已经正确实现了`hashCode()`方法，所以在实现`Person`类的`hashCode()`的时候，反复使用`31 * h`，目的是为了把不同的`Person`实例均匀的存贮在`int`范围内。

观察到，如果上面的`firstName`，`lastName`是`null`的时候，方法会报错，所以我们使用`Objects.hash()`来方便我们处理

```java
public class Person {
    String firstName;
    String lastName;
    int age;
    
    @Override
    int hashCode () {
        return Objects.hash(this.firstName, this.lastName, this.age);
    }
}
```

编写`equals()`和`hashCode()`的时候，需要遵循一个原则，`equals()`中用到的每一个字段，都需要在`hashCode()`中计算，`equals()`没有用到的字段，绝不能放进`hashCode()`中计算。

#### **HashMap扩展**

`hashMap`返回的`int`范围是在±21亿，那么`HashMap`存贮`value`的数组有多大？

实际上初始化`HashMap`的时候，它的数组长度是`16`，任何`key`，无论你的`hashCode()`有多大，最终都可以通过

``` java
int index = key.hashCode() & 0xf; 
```

转化成`0-15`之间的数字，所以不会超出数组长度，那么场景中的确有超过16个`value`需要存储怎么办？

当添加到一定量的`key-value`的时候，`HashMap`会和`ArrayList`一样进行自动翻倍扩容，频繁的扩容对`HashMap`的性能影响很大，所以我们可以在一开就直接指定HashMap的存贮容量

``` java
Map<String, Integer> map = new HashMap<>(1000);
```
虽然明确指定了`HashMap`的长度是`1000`，但是由于`HashMap`内部的数组长度总是2^n，所以实际初始化的数组长度是比`1000`要大的`1024（2^10）`

如果不同的两个`key`，它们的`hashCode()`结果相同，那么相当于我们向同一个索引位置`put`两次，先放入的元素会不会被后放入的元素给替换掉呢？

答案是不会，只要key不相同，它们映射的value就互不相干，不过这种key不同计算的hashCode()结果相同的场景确实存在，这种情况称为`哈希冲突`，例如`"a"`，`"b"`两个`key`计算的索引都是`5`，那么在`HashMap`中，存贮在索引`5`位置的就不是一个`Person`实例，而是一个`List`，它包含两个`Entry`，一个是`"a"`映射，一个是`"b"`的映射，看起来像这样

```
      ┌───┐
 0 -> │   │
      ├───┤
 1 -> │   │
      ├───┤
 2 -> │   │
      ├───┤
 3 -> │   │
      ├───┤
 4 -> │   │       
      ├───┤      ┌───┬───┐
 5 -> │ ●─┼───>  │ a │ b │  List<Entry<String, Person>>
      ├───┤      └───┴───┘
 6 -> │   │
      ├───┤
 7 -> │   │
      └───┘
```
因此在获取的时候，`HashMap`通过`"a"`计算出来的实际上是`List<Entry<String, Person>>`类型的数组，所以还要再遍历一遍这个数组，才能拿到`"a"`的`value`，如果这种冲突的概率很高，那么数组的长度就很长，进而导致`HashMap`的`get`方法效率变低，相反的`hashCode()`方法编写的越好，`HashMap`工作效率就越高，这就是为什么要：

> 如果两个对象不想等，则两个对象的`hashCode()`尽量不要相等

#### **使用EnumMap**

我们已经知道了，`Map`的`key`可以存储任意类型，并且通过`hashCode()`计算`value`所存储的索引位置，那么如果`key`的对象是`enum`类型，我们还可以使用`EnumMap`，它的内部是非常紧凑的数组存储`value`，即节省空间，查询效率也非常高，不需要计算`hashCode()`，没有额外的浪费。

```java
public class EnumMapClass {
    public static void main(String[] args){
        Map<DayOfWeek, String> map = new EnumMap<>(DayOfWeek.class);
        map.put(DayOfWeek.MONDAY, "周一");
        map.put(DayOfWeek.TUESDAY, "周二");

        System.out.println(map.get(DayOfWeek.MONDAY)); // 周一
    }
}
```
需要注意的是，在实例化`EnumMap`的时候，需要传入枚举类型的反射

#### **使用TreeMap**

`HashMap`的`key`是无序的，那么有没有一种`Map`的`key`是有序的呢？那就是`SortedMap`，`SortedMap`是继承自`Map`的一个接口，其实现类是`TreeMap`，`SortedMap`保证遍历时以`key`的顺序来排序
```java
public class TreeMapClass {
    public static void main(String[] args){
        Map<String, Integer> map = new TreeMap<>();
        map.put("banana", 200);
        map.put("apple", 100);
        map.put("pear", 300);
        for(String s: map.keySet()){
            System.out.println(s);
        }
        // apple, banana, pear // String默认按照字母排序
    }
}
```
使用`TreeMap`的时候，放入的`key`必须实现`Comparable`接口才能实现排序，`String`和`Integer`已经实现，所以可以直接拿来当`key`，如果`key`没有实现`Comparable`接口，必须在实例化`TreeMap`的时候，指定一个`Comparator`自定义排序算法

> 注意，`Comparable`和`Comparator` 都是接口，在实现排序的时候，如果在作为`key`的类上实现时，需要让类实现`Comparable`接口，在单独传递给`TreeMap`的时候，需要单独实现并实例化`Comparator`接口，它们对应要实现的方法名字也不同，不过内容相同，`Comparable`需要实现的是`compareTo`，而`Comparator`需要实现的是`compare`

```java
public class TreeMapClassComparable {
    public static void main(String[] args){
        Map<Person, Integer> map = new TreeMap<>(new Comparator<Person>(){
            public int compare(Person p1, Person p2){
                return p1.name.compareTo(p2.name);
            }
        });
    }
}

public class Person {
    String name;
    public Person (String name){
        this.name = name;
    }
}
```
实现`Comparator`接口时需要实现一个比较方法，它负责比较传入的两个元素`"a"`和`"b"`，如果`a < b`，则返回负数`-1`，如果相等则返回`0`，如果`a > b`则返回正数`1`，`TreeMap`内部根据比较结果对`key`进行排序。

可以看到`Person`并没有实现`equals()`和`hashCode()`，因为`TreeMap`不使用这两个方法，观察下面的例子

```java
public class TreeMapStudentEx {
    public static void main(String[] args){
        Map<Student, Integer> map = new TreeMap<>(new Comparator<Student>(){
            public int compare(Student s1, Student s2){
                return s1.score > s2.score ? -1 : 1;
            }
        });
        map.put(new Student("Tom", 60), 1);
        map.put(new Student("Bob", 80), 2);
        map.put(new Student("Mary", 90), 3);
        for(Student s: map.keySet()){
            System.out.println(s.name);
        }
        System.out.println(map.get(new Student("Bob", 80))); // null
    }
}

public class Student {
    String name;
    Integer score;

    public Student (String name, Integer score){
        this.name = name;
        this.score = score;
    }
}
```

明明已经添加了一个`key`是`new Student("Bob", 80)`的映射，为什么还会返回`null`呢？

原因就是`Comparator`
``` java
new Comparator<Student>(){
    public int compare(Student s1, Student s2){
         return s2.score > s2.score ? -1 : 1;
     }
}
``` 
并没有处理相等的情况，也就是说，`TreeMap`在比较两个`key`相等的时候，依赖的就是`Comparator.compare`或者作为`key`的类型实现的`compareTo()`，因此在两个`key`相等的时候，一定要返回`0`，代码改写如下

``` java
new Comparable<Student>(){
    public int compare(Student s1, Studente s2){
        if(s1.score.equals(s2.score)){
            return 0;
        }
        return s1.score > s2.score ? -1 : 1;
    }
}
```

也可以使用`Integer.compare(Integer, Integer)`静态方法

#### **使用Properties**

下面是一个标准的`properties`文件

``` properties
#应用名称
application=my_blog
#作者
author=dwy
```
配置文件的特点是它的`key-value`总是`String-String`类型，因此我们完全可以使用`Map<String, String>`来表示，因为配置文件非常常用，所以Java库提供了一个`Properties`来表示一组配置，因为这个技术比较久远，`Properties`内部本质是一个`HashTable`，不过我们只需要一些关于`Properties`一些读写的接口，可以从文件系统读取一个`.properties`文件

```java
public class PropertiesRead {
    public static void main(String[] args){
        String f = "绝对路径/application.properties";
        Properties p = new Properties():
        p.load(new FileInputStream(f));
        
        String bingImageUrl = p.getProperty("bingImageUrl");
        String hymeleafCache = p.getProperty("spring.hymeleaf.cache", "true");
        System.out.println(bingImageUrl, hymeleafCache);
    }
}
```
使用`Properties`读取配置文件分三个步骤

- 创建`Properties`实例
- 使用`load()`载入文件
- 调用`getProperty(String)`获取配置信息

调用`getProperty()`的时候，如果`key`不存在，将返回`null`，所以我们可以提供一个默认值，当`key`不存在的时候，就会返回默认值，使用方法

``` java
properties.getProperty("unKey", "defaultValue");
```
除了文件系统，还可以从`classpath`读取`.properties`文件，因为`load(InputStream)`，接收一个`InputStream`实例，表示一个字节流，它不一定是文件流，也可以是从jar包读取的资源流

```java
public class PropertiesReadByClassPath {
    public static void main(String[] args){
        Properties pro = new Properties();
        // 如果是在静态方法里，可直接使用 类名.class.getResourceAsStream()
        // 当前路径是在 resource包下
        pro.load(getClass().getResourceAsStream("/application.properties"));
    }
}
```

还可以从内从中直接读取

```java
public class PropertiesReadByByteArray {
    public static void main(String[] args){
        String f = "# 注释\nlang=Java\nyear=2020";
        ByteArrayInputStream bais = new ByteArrayInputStream(f.getBytes("UTF-8"));
        Properties pro = new Properties();
        pro.load(bais);
        System.out.println(pro.getProperty("lang") + pro.getProperty("year")); // Java2020
    }
}
```

一个`Properties`实例，可以反复调用`laod()`方法，将多个配置文件的信息加载过来，如果`key`相同，后读取的`.properties`文件会覆盖前面读取的`value`，除了`getProperty()`方法，当然就有`setProperty()`方法，下面看一个写入配置文件的例子

```java
public class PropertiesWrite {
    public static void main(String[] args){
        Properties pro = new Properties();
        pro.setProperty("set_test_value", "一个测试的属性");
        pro.store(new FileOutputStream("绝对路径/application.properties"));
    }
}
```

早期的`.properties`文件编码是`ASCII编码(ISO8859-1)`，如果涉及到中文，就必须使用`Unicode`来表示，非常麻烦，为了解决这个，可以使用下面的写法

```java
public class PropertiesDecode {
    public static void main(String[] args){
        Properties props = new Properties();
        props.load(new FileReader("settings.properties", StandardCharsets.UTF_8));
        // 写入使用下面的 FileWriter
        // props.store(new FileWriter("..."));
    }
}
```

`InputStream`和`Reader`的区别在于一个是字符流，一个是字节流，字符流在内存中已经是以`char`类型存储，所以不涉及编码问题。

除了上面说的，因为`Properties`是`HashTable`的派生类，所以继承了`HashTable`的`get()`，`put()`方法，但是不可以使用这两个方法，因为这两个方法的入参类型和`Map`一样，可以使用泛型自由指定类型，但是`Properties`的存贮格式是`String-String`，因此如果你把一个非`String`类型的变量给到`put()`方法，加到了`Properties`类里，当你再调用`Properties.store()`方法进行保存的时候，就会抛出一个`ClassCastException`，告诉你这个类型不能转化成`String`类型。所以为了保证安全，如果要操作`.properties`文件就必须使用`getProperty()`和`setProperty()`。

#### **使用Set**

我们已经知道，`Map`用来存贮`key-value`的映射，并且要覆写`equals()`和`hashCode()`方法，但是如果我们不需要映射，只要保存一组不重复的`key`，那么就可以使用`Set`，和`Map`一样，`Set`只是个接口，其常用实现类是`HashSet`，的主要方法如下：

- 将元素添加进`Set<E>`: `boolean add(E e)`
- 将元素从集合中删除`Set<E>`删除：`boolean remove(Object o)`
- 判断集合中是否存在某个元素 `boolean contains(Object o)`

```java
public class HashSetClass {
    public static void main(String[] args){
        Set<String> set = new HashSet<>();
        set.add("java");
        set.add("spring");
        set.add("boot");
        set.remove("BOOT"); //false 删除失败
        set.remove("boot"); // true 删除成功
        set.contains("aaa"); // false 没找到
        set.contains("java"); // true 找到了
        set.size(); // 2 该Set包含两个元素
    }
}
```

`Set`就相当于`Map`的`key`部分，不存贮`value`，经常使用`Set`是为了元素去重复，`Set`在存放的时候也需要元素实现`equals()`和`hashCode()`，如果我们观察源码会发现，`HashSet`的内部，其实还是利用`HashMap`来实现的，只不过不允许你传入`value`而已，下面观察`HashSet`核心源码

```java
import java.util.HashMap;
import java.util.Object;
public class HashSet<E> implements Set<E> {
    
    private HashMap<E, Object> map = new HashMap<>();
    
    private static final Object PRESENT = new Object();

    public boolean add (E e) {
        return map.put(e, PRESENT) == null;
    }
    
    public boolean remove(Object o) {
        return map.remove(o) == PRESENT;
    }
    
    public boolean contains (Object o) {
        return map.containsKey(o);
    }
}
```

`Set`接口不保证顺序，但是`SortedSet`可以保证顺序，和`Map`类似

- `HashSet`不保证顺序，因为它实现了`Set`接口没有实现`SortedSet`接口
- `TreeSet`可以保证顺序，它实现了`SortedSet`接口

`HashSet`的`key`排列顺序既不是写入顺序，也不任何排序顺序，甚至在不同JDK版本下顺序也不相同，所以在Java开发时假定`Set`或`Map`的`key`有序是非常危险的

在给`TreeSet`添加元素的时候，和`TreeMap`的要求一样，`key`的元素必须实现`Comparable`接口，或者在创建`TreeSet`的时候，指定一个`Comparator`对象

```java
public class TreeSetClass {
    public static void main(String[] args){
        Set<Student> set = new TreeSet<>(new Comparator<Student>(){
            public int compare (Student s1, Student s2) {
                return s1.name.compareTo(s2.name);
            }
        });
    }
}
```

#### **使用Queue**

队列(Queue)是一种非常常用的集合，`Queue`实现了一个先进先出(FIFO)的有序表，它和`List`的区别在于`List`可以在任意位置添加和删除元素，而`Queue`只有两个操作

- 把元素添加到末尾
- 从队列头部取出元素

Java标准库中，`Queue`定义了一下几个方法

- `int size()` 获取队列长度
- `boolean add(E)`/`boolead offer(E)` 添加元素到末尾
- `E remove()`/`E poll()` 获取队首并从队列中删除
- `E element()`/`E peek()` 获取队首但不从队列中删除

对于集体的实现类，有的`Queue`有最大队列长度限制，有的没有

我们注意到上面说的每种操作都对应着两个方法，这是因为元素在添加获取失败的时候，其反馈不同

|操作|throw Exception|返回 false or null|
|---|---|---|
|添加元素到队尾|`add(E e)`|`boolean offer(E e)`|
|取队首元素并删除|`E remove()`|`E poll()`|
|取队首元素不删除|`E element()`|`E peek()`|

这套方法可以根据需求来使用，有一点需要注意的是，不要往队列中添加`null`，因为你无法确定使用使用`poll()`方法时，取到的是`null`元素还是空队列

`Queue`的实现类非常多，分为阻塞队列和非阻塞队列

三个非阻塞队列包括
- `LinkedList` // 常用
- `PriorityQueue` // 常用
- `ConcurrentLinkedQueue`

七个阻塞队列包括

- `ArrayBlockingQueue`
- `LinkedBlockingQueue`
- `PriorityBlockingQueue`
- `DelayQueue`
- `SynchronouseQueue`
- `LinkedTrnsferQueue`
- `LinkedBlockingDeque`

每个实现类之间都存在差异，后面我会专门总结一篇队列知识点

我们会发现，在`List`篇我们也同样介绍了`LinkedList`类，因为`LinkedList`既实现了`List`，也实现了`Queue`，所以依照面向对象多态原则，使用`List`引用它就是`List`，使用`Queue`引用，它就是`Queue`

``` java
// 这是一个List
List<String> list = new LinkedList<>();

// 这是一个Queue
Queue<String> queue = new LinkedList<>();
```

#### **使用PriorityQueue**

思考一个银行柜台案例，每个人取一个号`A1`，`A2`，`A3...`然后按照号码顺序依次办理实际上就是一个`Queue`。

但是如果这是来了一个客户，好吗是`V1`，虽然当前排队的是`A10`，`A11`，`A12`但是柜台下一个喊得却是`V1`。

我们发现使用普通的`Queue`队列就无法实现"VIP插队"业务，所以就要使用优先队列：`PriorityQueue`。

`PriorityQueue`和`Queue`的区别是什么？`PriorityQueue`的出队顺序与元素的优先级有关，`PriorityQueue`在调用`remove()`或`poll()`方法的时候，返回的总是优先级最高的元素。

因此，我们在使用`PriorityQueue`的时候，需要给每个元素指定优先级（排序），观察以下代码：

```java
public class PriorityQueueClass {
    public static void main(String[] args){
        Queue<String> q = new PriorityQueue<>();
        q.offer("apple");
        q.offer("pear");
        q.offer("banana");
        System.out.println(q.poll()); // apple
        System.out.println(q.poll()); // banana
        System.out.println(q.poll()); // pear
        System.out.println(q.poll()); // null 队列已经空了
    }
}
```
观察到，我们入队的顺序是`"apple"`，`"pear"`，`"banana"`，出对的顺序确实`"apple"`，`"banana"`，`"pear"`，这是因为从字符串的排序来看，`"apple"`在最前面，`"pear"`在最后面，所以放入`PriorityQueue`集合的元素必须实现`Comparable`接口，或者传入一个`Comparator`对象来判断元素顺序，改写上面的代码，使他可以用在银行排队业务

```java
public class PriorityQueueVIPClass {
    public static void main(String[] args){
        Queue<User> q = new PriorityQueue<>(new UserComparable());
        q.offer(new User("Bob", "A1"));
        q.offer(new User("Alice", "A2"));
        q.offer(new User("Boss", "V1"));
        System.out.println(q.poll()); // Boss
        System.out.println(q.poll()); // Bob
    }
}

public class UserComparable implements Comparator<User> {
    public int compare(User u1, User u2) {
        if(u1.number.charAt(0) == u2.number.charAt(0)){
            // 如果两个同级，就比较号码大小
            return (Integer) Integer.parseInt(u1.number.substring(1)).compareTo((Integer) u2.number.substring(1));
        }
        // 如果不一样，就V优先
        if(u1.number.charAt(0) == "V"){
            return -1;
        }else {
            return 1;
        }
    }
}
```

#### **使用Deque**

除了`PriorityQueue`队列之外，还有一种双端队列`Deque`，与之前的队列不同，双端队列就是两端都可以操作，既可以添加元素到队尾，也可以添加到队首，既可以从队首获取，也可以从队尾获取

下面比较一下`Queue`和`Deque`的入队出队方法

|操作|Queue|Deque|
|---|---|---|
|添加元素到队尾|add(E)/offer(E e)|addLast(E e)|
|取队首元素并删除|E remove()/E poll()|E removeFirst()/E pollFirst()|
|取队首元素不删除|E element()/E peek()|E getFirst()/E peekFirst()|
|添加元素到队首|无|addFirst(E e)|
|取队尾元素并删除|无|E removeLast()/E pollLast()|
|取队尾元素不删除|无|E getLast()/E peekLast()|

事实上，`Deque`是一个继承自`Queue`的接口，所以`Queue`包含的`offer()`和`add()`方法在`Deque`上也是可以使用，只是不建议使用，因为使用`offerLast()`和`addLast()`更加明确作用。

`Deque`的实现类有`ArrayDeque`和`LinkedList`，又有`LinkedList`，它即是`List`，有事`Queue`，还是`Deque`，所以在使用的时候，我们总是以特定接口来引用，因为持有接口说明代码的抽象层次更高，而接口本身定义的方法代表了特定的用途。

```java
public class LinkedListClass {
    public static void main(String[] args){
        // 不推荐的写法
        LinkedList<String> d1 = new LinkedList<>();
        d1.offerLast("z");

        Deque<String> d2 = new LinkedList<>();
        d2.offerLast("z");
    }
}
```

#### **使用Stack**

栈(Stack)是一种后进先出，先进后出（LIFO）的数据结构，`Stack`只能不断的压入(push)元素，最后压进去的必须最早弹出(pop)来，下面是`Stack`的入栈出栈的方法

- 把元素压栈：`push(E e)`;
- 把栈顶的元素弹出：`E pop()`;
- 取栈顶元素但不弹出：`E peek()`;

在Java中，我们也可以使用`Deque`实现和`Stack`一样的功能：

- 把元素压入栈：`push(E e)`/`addFirst(E e)`
- 把栈顶元素弹出：`E pop()`/`E removeFirst()`
- 读取栈顶元素：`E peek()`/`E peekFirst()`

为什么没有`Stack`接口呢？因为有一个遗留类就叫`Stack`，为了兼容性考虑，所以没办法创建`Stack`接口，只能用`Deque`来模拟一个`Stack`，当使用`Deque`作为`Stack`的时候，注意只使用`push()`/`pop()`/`peek()`方法，不要使用`addFirst()`/`removeFirst()`/`peekFirst()`方法，这样更符合`Stack`的调用方法。

`Stack`在计算机中使用非常广泛，JVM在处理Java方法调用的时候就会通过栈这种数据结构维护方法调用的层次，例如：

```java
public class StackFunctionClass {
    public static void main(String[] args){
        foo(123);
    }
    
    static String foo(int x){
        return "F-" + bar(x + 1);
    }
    
    static int bar(int x){
        return x << 2;
    }
}
```

JVM会创建方法调用栈，每调用一个方法，先将参数压栈，然后执行对应的方法；当方法返回时，返回值压栈，调用方法通过栈操作获得返回值。

因为方法调用栈有容量限制，嵌套调用层数过多后导致栈溢出，就会引发`StackOverFlowError`；

再看一个`Stack`的用途，对整数进行进制转换就可以利用栈

例如我们吧一个`int`整数`12500`转换为十六进制表示的字符串

首先准备一个空栈

``` java
│   │
│   │
│   │
│   │
│   │
│   │
│   │
│   │
└───┘
```

然后计算`12500 / 16 = 781...4`，余数是`4`，我们把`4`压栈；

``` java
│   │
│   │
│   │
│   │
│   │
│   │
│   │
│ 4 │
└───┘
```

然后计算`781 / 16 = 48...13`，余数是`13`，`13`的16进制用字母`D`表示，我们把`D`压栈

``` java
│   │
│   │
│   │
│   │
│   │
│ D │
│   │
│ 4 │
└───┘
```

然后计算`48 / 16 = 3...0`，余数是`0`，我们把`0`压栈

``` java
│   │
│   │
│   │
│ 0 │
│   │
│ D │
│   │
│ 4 │
└───┘
```

最后计算`3 / 16 = 0...3`，余数是`3`，我们再把`3`压栈

``` java
│   │
│ 3 │
│   │
│ 0 │
│   │
│ D │
│   │
│ 4 │
└───┘
```

当商是`0`的时候，计算结束，把栈的所有元素一次弹出，组成字符串`"30D4"`，这就是十进制整数`12500`的`16`进制表示的字符串。

计算中辍表达式

在编写程序的时候，我们使用带括号的数学表达式实际上是中缀表达式，即运算符在中间，例如`1 + 2 * (9 - 5)`，但是计算机执行表达式的时候，它并不能直接计算中缀表达式，而是通过编译器吧中缀表达式转换为后缀表达式，像这样`1 2 9 5 - * +`，在这个编译过程中就会用到栈，我们先看看如何通过栈计算后缀表达式。

计算后缀表达式不必考虑优先级，直接从左到右一次计算，因此计算起来很简单，首先是一个空栈：

``` java
│   │
│   │
│   │
│   │
│   │
│   │
│   │
│   │
└───┘
```

然后一次扫描后缀表达式`1 2 9 5 - * +`，遇到数字`1`，直接压栈，然后`2`，直接压栈...

然后我们得到一个栈

``` java
│   │
│ 5 │
│   │
│ 9 │
│   │
│ 2 │
│   │
│ 1 │
└───┘
```

然后遇到了一个减号，弹出栈顶的两个元素`9` 和 `5`，并计算 `9 - 5 = 4`，把结果压栈

``` java
│   │
│   │
│   │
│ 4 │
│   │
│ 2 │
│   │
│ 1 │
└───┘
```

然后遇到乘号，弹出栈顶的两个元素`2`和`4`，并计算 `2 * 4 = 8`，把结果压栈

 ``` java
 │   │
 │   │
 │   │
 │   │
 │   │
 │ 8 │
 │   │
 │ 1 │
 └───┘
 ```
 
 然后遇到`+`号，再弹出栈顶两个元素`1`和`8`，计算`1 + 8 = 9`，把结果`9`压栈
 
 扫描结束，弹出栈的唯一一个元素，得到计算结果`9`。
 
 [查看一个10进制转换16进制的例子](https://github.com/Dyinfalse/JavaLearn/blob/master/blob/src/main/java/com/jgmt/blog/practice/StackTest.java)
 
 #### **使用Iterator**
 
 Java的集合都可以使用`for each`循环，`List`，`Set`，`Queue`会迭代每个元素，`Map`会迭代每个`Key`，以List为例
 
 ```java
public class IterratorListEXClass {
    public static void main(String[] args){
        List<String> list = List.of("apple", "orange", "pear");
        for(String s : list){
            System.out.println(s);
        }
    }
}
```

实际上，java编译器不知道如何遍历`List`，上述代码能够编译通过，是因为编译器把`for each`循环通过`Iterator`改写成了普通`for`循环：

``` java
for (Iterator<String> it = list.iterator(); it.hasNext();){
    String s = it.next();
    System.out.println(s);
}
```

我们把这种通过`Iterator`对象遍历集合的模式成为迭代器。

使用迭代器的好处在于，调用方总是以统一的方式遍历各种集合类型，而不必关心它们内部的存储结构。

例如，我们虽然知道`ArrayList`在内部是以数组形式存贮元素，而且还提供了`get(int)`方法，我们可以使用普通的`for`循环遍历：

``` java
for (int i = 0; i < list.size(); i++){
    Object value = list.get(i);
    
}
```

但是这样一来调用方就必须知道集合内部的存储结构，而且不够通用，如果`ArrayList`换成`LinkedList`，方法的耗时就会随着索引的增加而增加，或者把`ArrayList`换成`Set`，那么就会编译出错，这也是`Iterator`的优势。

使用`Iterator`就不存在上面描述的问题，因为`Iterator`对象就是集合内部自己创建的，它自己知道如何高效的遍历内部数据集合，调用方则获得统一的代码，编译器才能把标准的`for each`循环自动转换成`Iterator`迭代器。

如果我们自己编写一个集合类，想要使用`for each`循环，只需要满足下面的条件

- 集合类实现`Iterable`接口，该接口要求返回一个`Iterator`对象
- 用`Iterator`对象迭代集合内部数据

这里的关键在于，集合类通过调用`iertator()`方法返回一个`Iterator`对象，这个对象必须自己知道如何遍历集合

下面是一个简单的`Iterator`示例，它以倒序遍历集合：

```java
public class Main {
    public static void main(String[] args){
        ReverseList<String> rlist = new ReverseList<>();
        
        rlist.add("apple");
        rlist.add("orange");
        rlist.add("pear");
        
        for(String s : rlist){
            System.out.println(s);
        }
    }
}

public class ReverseList<T> implements Iterable<T> {
    private List<T> list = new ArrayList<>();
    
    public void add(T t) {
        list.add(t);
    }
    
    @Override
    public Iterator<T> iterator () {
        return new ReverIterator(list.size());
    }
    
    class ReverseIterator implements Iterator<T> {
        int index;
        
        ReverIterator(int index) {
            this.index = index;
        }
        
        @Override
        public boolean hasNext() {
            return index > 0;
        }
        
        @Override
        public T next () {
            index --;
            return ReverseList.this.list.get(index);
        }
    }
}
```

虽然`ReverseList`和`ReverseIterator`实现比较麻烦，但是这是底层的集合库，是需要实现一遍，而调用方完全不需要知道内部实现方式，只需要调用`for each`循环就可以。

在编写`Iterator`的时候，我们通常可以用一个内部类实现接口`Iterator`，这个内部类可以直接访问对应的外部类的所有字段和方法，例如上面的内部类`ReverseIterator`可以使用`ReverList.this`获得当前外部类的`this`引用，然后通过`this`，就可以访问`ReverseList`的所有字段和方法。





























