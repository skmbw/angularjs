package com.ice.personnel.controller;

import java.util.ArrayList;
import java.util.List;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

public class OgnlTest2 {
 public static void main(String[] args) throws OgnlException {
  Person person = new Person();
  person.setName("zhangsan");
  Dog dog = new Dog();
  dog.setName("wangcai");
  OgnlContext context = new OgnlContext();
  // OGNL实现了MAP接口
  context.put("person", person);
  context.put("dog", dog);
  context.setRoot(person);// 设置唯一的根对象
  Object object = Ognl.parseExpression("name");// 解析字符串，若没有#，则到根对象找。#是明确告诉OGNL从哪个对象中找。调用getName()方法
  System.out.println(object);// 显示name
  Object object2 = Ognl.getValue(object, context, context.getRoot());
  System.out.println(object2);
  System.out.println("-------------------");
  Object object3 = Ognl.parseExpression("#person.name");
  System.out.println(object3);
  Object object4 = Ognl.getValue(object3, context, context.getRoot());
  System.out.println(object4);
  System.out.println("-------------------");
  Object object5 = Ognl.parseExpression("#dog.name");
  System.out.println(object5);
  Object object6 = Ognl.getValue(object5, context, context.getRoot());
  System.out.println(object6);
  System.out.println("-------------------");
  Object object7 = Ognl.parseExpression("name.toUpperCase().length()");// name.toUperCase()相当于getName().toUperCase();
  System.out.println(object7);
  Object object8 = Ognl.getValue(object7, context, context.getRoot());
  System.out.println(object8);
  System.out.println("-------------------");
  // 静态方法可以通过类名字直接调用
  Object object9 = Ognl
    .parseExpression("@java.lang.Integer@toBinaryString(10)");// @@中间是报名，后面是方法名，10是将10转换为2禁止字符串
  System.out.println(object9);
  Object object10 = Ognl.getValue(object9, context, context.getRoot());
  System.out.println(object10);
  System.out.println("-------------------");
  Object object11 = Ognl.parseExpression("@@min(4,10)");//也可写成静态值PI  E 等（π和e）
  System.out.println(object11);
  Object object12 = Ognl.getValue(object11, context, context.getRoot());
  System.out.println(object12);
 
  Object object13 = Ognl.parseExpression("new java.util.LinkedList()") ;
  System.out.println(object13);
  Object object14 = Ognl.getValue(object13,context,context.getRoot()) ;
  System.out.println(object14);
 
  
  //合二为一
  Object object15 = Ognl.getValue("{'aa','bb','cc','dd'}", context,context.getRoot()) ;//{'aa','bb','cc','dd'}是集合。Ognl视集合和数组为一样的
  System.out.println(object15);
  Object object15_1 = Ognl.getValue("{'aa','bb','cc','dd'}[2]", context,context.getRoot()) ;
  System.out.println(object15_1);
  dog.setFriends(new String[]{"aa","bb","cc"}) ;
  Object object16 = Ognl.getValue("#dog.friends",context,context.getRoot()) ;
  System.out.println(object16);//toString
 
  Object object16_1 = Ognl.getValue("#dog.friends[1]",context,context.getRoot()) ;
  System.out.println(object16_1);//toString
  List<String> list = new ArrayList<String>() ;
  list.add("hello");
  list.add("world") ;
  list.add("hello world") ;
  context.put("list",list) ;
  System.out.println(Ognl.getValue("#list[0]",context,context.getRoot()));
 
  System.out.println("---------------------");
  System.out.println(Ognl.getValue("#{'key1':'value1','key2':'value2','key3':'value3','key4':'value4'}",context,context.getRoot()));
 
  System.out.println(Ognl.getValue("#{'key1':'value1','key2':'value2','key3':'value3','key4':'value4'}['key3']",context,context.getRoot()));
  System.out.println("---------------------");
  
  System.out.println("------------过滤器--------");
  List<Person> persons = new ArrayList<Person>() ;
  Person p1 = new Person() ;
  Person p2 = new Person() ;
  Person p3 = new Person() ;
  
  p1.setName("zhangsan") ;
  p2.setName("lisi") ;
  p3.setName("wangwu") ;
  
  persons.add(p1) ;
  persons.add(p2) ;
  persons.add(p3) ;
  
  context.put("persons",persons) ;
  System.out.println(Ognl.getValue("#persons.{?#this.name.length()>4}",context,context.getRoot()));//#this表示当前待操作的对象
  System.out.println(Ognl.getValue("#persons.{?#this.name.length()>4}.size()",context,context.getRoot()));//#this表示当前待操作的对象
  System.out.println(Ognl.getValue("#persons.{?#this.name.length()>4}[0]",context,context.getRoot()));//#this表示当前待操作的对象
  //获取到集合中的第一个元素 collection.{^expression}
  System.out.println(Ognl.getValue("#persons.{^#this.name.length()>4}",context,context.getRoot()));
  //获取到集合中的最后一个元素 collection.{$expression}
  System.out.println(Ognl.getValue("#persons.{$#this.name.length()>4}",context,context.getRoot()));
  System.out.println(Ognl.getValue("#persons.{$#this.name.length()>4}[0].name",context,context.getRoot()));
  System.out.println("-----------------");
  
  //投影(projection), collection.{expression}
  System.out.println(Ognl.getValue("#persons.{name}",context,context.getRoot()));
  System.out.println("------------------------");
  //名字长度>=5则显示原来的。若<5则返回hello world
  System.out.println(Ognl.getValue("#persons.{#this.name.length()<=5?'hello world':#this.name}",context,context.getValues()));
  
 }
 
}
