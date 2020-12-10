package com.example.testproject.dagger;
import javax.inject.Inject;

public class view {
    //方法1
    @Inject
    Presenter presenter;

    public void testDagger() {
//        DaggermyComponent.create().voidGetPresenter(this);     //把这个类里面的所有@Inject注解全部加入到Component中去
        presenter.showPresenter();
    }

    //方法2,当component里面的方法是Presenter getPresenter()，直接返回一个对象的时候可以使用这种调用方法;
    public static void mtestDagger() {
        //加载module
//        myComponent component = DaggermyComponent.builder().myModule(new myModule()).build();   //获取component的对象，然后去获取里面的注入
//        Presenter presenter = component.getPresenter();
//        presenter.showPresenter();
    }
}
