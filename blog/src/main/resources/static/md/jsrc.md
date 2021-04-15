## 前端日常

*2021-02-20 Dyinfalse*

> 这里放一些平时JS遇到了就记录一下的小知识点总结, 以及遇到的场景

##### [MutationObserver](https://developer.mozilla.org/zh-CN/docs/Web/API/MutationObserver)
监听元素的构造方法, 可以监听元素的样式属性, 节点变化, 内容变化

但是元素的外形改变, 并不意味着样式属性发生改变, 例如: width: 100%的情况, 具体的宽度可能会发生改变, 但是属性值始终是100%, 所以 MutationObserver != ResizeObserver, 样式改变是属性改变的结果, 并不能倒推.

> 场景：在阅读Vue(2.0)的$nextTick方法的源码的时候看到了这个方法作为Promise的备选方案，虽然不常用，但确实是个强大的API，在EventLoop中，`MutationObserver.observe`方法可以作为微观任务


##### 事件捕获& 事件冒泡

关于事件的描述有很多，这里就不赘述，只做记录

在一连串事件中, 先捕获, 再冒泡, 但是在但是当点击事件的target元素既有捕获事件也有冒泡事件的时候, 会先执行冒泡, 再执行捕获, 事件冒泡和事件捕获, 是事件执行的方式, 我们通常需要指定一连串事件的执行方式, 或中断, 或继续, 整体顺序和理解上偏差不大, 从最顶级父元素开始执行捕获, 一直执行到target元素, 执行target的冒泡, 之后紧接着执行target捕获, 然后 依次从内向外执行冒泡事件.

> 场景：在实现设计器复杂交互的时候，牵扯到事件相关的很多知识点，索性腾出时间复习一下，并且做了几个小的验证

##### Vue页面缓存

实现一个函数很简单，但函数调用的时机，环境，频率，在复杂业务场景下就很难把握，这里记录一个Vue缓存带来的一点问题

Vue页面缓存会造成一个问题, 及: 进入页面的依据是路由, 而跳转进一个路由的时候, 页面是否缓存会直接影响钩子函数执行和页面初始化逻辑的书写的方式, 进而增加组件开发的难度, 甚至产生BUG

场景1：
页面A没有缓存, 第一次访问, 正确进入`created`, `mounted`, 所有钩子函数都被正确触发, 代码逻辑清晰

场景2：
页面B有缓存, 第一次访问, 缓存没有载入, 与`页面A`的实现相同, 所有钩子都被正确触发, 但是此时`页面B`被缓存了, 前往`页面C`之后, 再返回`页面B`, 这个时候, 所有页面B的初始化钩子都不会生效, 无法更新局部的或全部的页面B的数据, 此时可以产生动作触发的, 就是`watch $router`, 监听路由的变化, 然后根据路由参数来处理, 但是增加了路由变化的组件, 在首次访问初始化的时候, 会和`created`产生冲突, 因为都是在做初始化这件事, 并且`created`的钩子会优先`watch $router`执行, 导致`watch $router`中书写的代码必须严格控制在特定场景执行, 并且需要使用明确变量来作为其执行的前提或开关

在实现之前, 应该考虑到页面缓存的必要性, 更新频率, 访问频率, 场景划分, 最终决定是否使用缓存或如何使用.

> 场景：因为设计器的功能体量很大，并且IDE是支持tab切换的，所以很可能用户在设计到一般会切到别的地方再回来，如果不做缓存设计器每次都重新加载体验非常不好，所以要给设计器增加一个缓存，但是页面的缓存会导致一些问题，也是我对Vue页面缓存知识不太了解导致.

##### Vue是如何处理get和set的？

文件路径: `node_modules/vue/src/core/observer/index.js`

代码不是很多，而且很容易看懂，就直接贴上我自己实现的吧

```js
import TypeUtils form *;
/**
 * Define a property.
 */
export function def(obj, key, val, enumerable = true) {
    Object.defineProperty(obj, key, {
        value: val,
        enumerable: !!enumerable,
        writable: true,
        configurable: true
    })
}
/**
 * 
 * @param {any}} obj 需要代理的对象
 * @param {*} element 
 */
export default function overwriteProp(obj, element) {
    if(!TypeUtils.isObject(obj)) return;
    let conf = {}
    let keys = Object.keys(obj)

    for(let i = 0; i < keys.length; i++){
        let key = keys[i];
        const property = Object.getOwnPropertyDescriptor(obj, key);
        /**
         * 这里保存了setter和getter主要是给Pack里面代理数据用的, 
         * 正常代理Element类里面的style. packStyle是不需要保存之前的setter和getter的,
         * 因为创建Element的时候, 实例对象还没有交给Vue接管, 所以属性监听是先与vue的, vue会自动处理之前的监听
         */
        const getter = property && property.get;
        const setter = property && property.set;

        if(key == '__DATA_EDITER_ID') continue;
        if(TypeUtils.isObject(obj[key])){
            overwriteProp(obj[key], element)
        } else if(TypeUtils.isArray(obj[key])) {
            def(obj[key], '__element__', element) // -> 保留我的业务数据
            obj[key].map(o => overwriteProp(o, element))
        }
        let value = obj[key];
        conf[key] = {
            configurable: true,
            enumerable: true,
            set(newValue) {
                if (newValue === value || (newValue !== newValue && value !== value)) {
                    return
                }
                doSomeThing(element); // -> 这里是我自己的业务
                setter && setter.call(obj, newValue) // -> 主要是这里
                value = newValue;
                
            },
            get() {
                const val = getter ? getter.call(obj) : value // -> 主要是这里
                return val
            }
        }
        
    }
    Object.defineProperties(obj, conf)
}
```

> 场景：在IDE开发的过程中，需要对对象的变化进行深度监听，但是对象上包含循环引用，使用Vue自带的`watcher`会发生编译错误，所以需要自己实现一个Vue的监听逻辑，为了防止和Vue的代理发生冲突，所以先看了下源码，并且参考实现了自己的代理
> 整个处理流程大概是下面两种情况
> 1.创建数据(会实例化) 2.执行自己的代理 3.数据交给Vue渲染 4.Vue代理
> 1.实例化之后的数据发生变更 2.Vue自己的代理（$set） 3.我自己的代理

##### Vue组件事件的自定义触发和绑定

这是在实现IDE的过程中，涉及事件绑定的时候，突然想到的实现方式，觉得很不错，于是记下来，上代码！

这是组件的定义地方，组件内正常定义外抛方法，没有入侵的痕迹，通过页面的交互，可以直接选择事件名称，绑定给对应的组件实例`Element.methods: Array<EventName>`
```js
methods: {
      change(e) { // 函数名称和外抛名称一致，也可以不一致，在渲染的地方使用`vm.constructor.extendOptions.methods`获取方法内容使用正则截取
          this.$emit("change", e);
      },
      pageSizeChange(e) {
          this.$emit("pageSizeChange", e);
      }
}
```

动态渲染组件的地方

使用的时候我们可以拿到一个`Array<EventName>`，使用`bind`给事件所对应的函数增加一个事件名称参数，`callEvent`会固定去执行当前`Element`所绑定的事件

（这里涉及到Element的使用，这个Element是我自己定义的在实现设计器的时候专门实现出来的一个抽象类，后面会专门抽事件单独介绍一下设计器）

```html
<template>
    <element  v-on="events"></element>
</template>
<script>
// 最后 event被组装成 { event1: callEvent, event2: callEvent}
export default {
    name: 'Element',
    data() {
        return {
            events: {}
        }
    },
    methods: {
        callEvent (functionName, ...agrs) {
            this.element.events.map(e => e.action == functionName && e.run())
        },
    },
    created() {
    },
    mounted() {
        this.element.methods.map(name => {
            this.events[name] = this.callEvent.bind(this, name)
        })
    }
}
</script>
```

> 场景：实现设计器事件绑定功能，可以理解是使用纯UI交互的方式实现自定义事件的绑定

##### Vue的Key

高效管理复用元素, diff算法在更新dom时候, 相同类型的元素更新时只会替换属性, 不会重新渲染, 根据key的比较, 会避开不需要更新的元素, 更准确高效 因为diff算法采用的是同层级比较的方法, 所以当同层级元素发生改变的时候, diff会记录一个变更, 那么这个变更具体是什么类型, 会按照一些他的判断依据进行确定, 比较典型的例子就是循环元素的时候, 如果你没加key,或者加的key不对, 那么在对列表进行操作的时候, 一些元素自带属性的变更就出现问题, 什么是元素的自带属性, 就是dom本身的属性, 不是通过你定义数据来确定的属性, 就是元素本身自带的属性, 例如checkbox的选中状态, 假设你没有给它v-model绑定值, 那么选中状态就是它的自带属性, 这些自带属性当元素发生变化的时候, 会根据key去渲染, 如果你给定的key是索引而不是唯一id, 那么当数组变化导致元素对应的索引发生变化的时候,就会出现我们常见的索引错位的现象. 就会导致界面和预期不符合的情况

> 场景：看到了一个面试题：如何理解Vue中的Key，正好有时间就整理了一份自己的答案

##### 什么是MVVM

MVVM, 重点在于VM, 也就是链接Model和View的桥梁, 于MVC的最大区别在于VM并不是替换的C, 而是抽离的C中业务无关的部分, 让开发者可以更专注于业务, 对于页面展示, 数据分发, 都交给VM去处理, 使得原来的C不想是传统的C, 所以被称为VM

> 场景：源自于一个经典面试题，一直对前端的MVC，MVVM存在一些问题所以想自己尝试总结一下



