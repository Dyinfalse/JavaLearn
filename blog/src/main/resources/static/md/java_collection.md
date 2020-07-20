## java集合知识点

*2020-07-19 Dyinfalse*

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
        List<Student> list = List.of(new Student("XiaoMing", 60), new Student("XiaoHong", 80), new Student("XiaoHua", 90));
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
    public String name;
    public Integer score;
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
上面代码`put`了两个`Mick`，可以看出，第二次`put`的`value`把第一`put`的给冲掉了，也就是说，在`Map`中重复添加相同`key`的映射，`value`以最后一次添加的为准













