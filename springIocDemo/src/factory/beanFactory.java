package factory;

import JavaBean.Student;

import java.util.HashMap;
import java.util.Map;

/**
 * 工厂bean 使用的工厂类，使用map接受在XML中定义的实体，对外暴露getStudent方法作为工厂方法，接受map的key，获取在工厂bean中定义的实体
 */
public class beanFactory {
    private Map<Integer, Student> map = new HashMap<Integer, Student>();

    public void setMap (Map<Integer, Student> map){
        this.map = map;
    }

    public Student getStudent(Integer id){
        return this.map.get(id);
    }
}
