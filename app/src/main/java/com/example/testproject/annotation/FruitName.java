package com.example.testproject.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 注解声明接口
 */

/**
 * @Target 表示该注解用于什么地方，可以理解为：当一个注解被 @Target 注解时，这个注解就被限定了运用的场景，也就是告诉系统你这个是什么标签。可以使用的 ElementType 参数：
 * ElementType.CONSTRUCTOR: 对构造方法进行注解；
 * ElementType.ANNOTATION_TYPE: 对注解进行注解；
 * ElementType.FIELD: 对属性、成员变量、成员对象（包括 enum 实例）进行注解；
 * ElementType.LOCAL_VARIABLE: 对局部变量进行注解；
 * ElementType.METHOD: 对方法进行注解；
 * ElementType.PACKAGE: 对包进行注解；
 * ElementType.PARAMETER: 对描述参数进行注解；
 * ElementType.TYPE: 对类、接口、枚举进行注解；
 */
@Target(ElementType.FIELD)
/**
 * RetentionPolicy.SOURCE: 注解只在源码阶段保留，在编译器完整编译之后，它将被丢弃忽视；
 * RetentionPolicy.CLASS: 注解只被保留到编译进行的时候，例如butterknife，它并不会被加载到 JVM 中；
 * RetentionPolicy.RUNTIME: 注解可以保留到程序运行的时候，它会被加载进入到 JVM 中，所以在程序运行时可以获取到它们；笔者接触到大部分的注解都是 RUNTIME 的生命周期。
 */
@Retention(RetentionPolicy.RUNTIME)
/**
 * @Documented 是一个简单的标记注解，表示是否将注解信息添加在 Java 文档，即 Javadoc 中。
 */
@Documented
public @interface FruitName
{
    String name() default "";
}


