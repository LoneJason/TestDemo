package com.example.testproject.dagger;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

/**
 * 用@Module注解标注，主要用来提供依赖。之所以有Module类主要是为了提供那些没有构造函数的类的依赖，这些类无法用@Inject标注
 *
 */
@Module
public class myModule
{

    @Provides
    @Singleton
    Presenter providePresenter()
    {
        return new Presenter();
    }
}
