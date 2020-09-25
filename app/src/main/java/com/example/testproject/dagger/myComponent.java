package com.example.testproject.dagger;

import com.example.testproject.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * 这个实际上就是中间的类，去寻找module和构造函数是否有相关的依赖
 */
@Singleton
@Component(modules = myModule.class)
public interface myComponent
{
     Presenter getPresenter();
     void voidGetPresenter(view v);   //目的是让Component和view发生关系，因为Component需要引用到目标类（view），大致流程Component会查找目标类中用Inject注解标注的属性，
                                     // 查找到相应的属性后会接着查找该属性对应的用Inject标注的构造函数（也就是查找Presenter的构造函数），如果没有构造函数的话就会去module里面去找有没有提供依赖
}
