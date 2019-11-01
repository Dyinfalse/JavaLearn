package JavaBean;

/**
 * java Bean
 */
public class Student {
    private String name;
    private Integer age;

    public Student() {

    }

    public Student(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    /**
     * bean 使用的工厂方法，如果有这个方法，spring就不自己创建了，而是调用这个方法，
     * 将方法返回结果，确定为 ClassPathXmlApplicationContext.getBean 的返回结果
     * @return
     */
    public static Student getInstance(int arg) {
        System.out.println("getInstance: 调用工厂方法，参数：arg = " + arg);
        Student s = new Student("厂狗小王",19);
        return s;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString(){
        return "这个学生的名字是" + this.name + "，这个学生的年龄是" + this.age;
    }
}
