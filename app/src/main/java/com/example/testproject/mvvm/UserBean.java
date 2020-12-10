package com.example.testproject.mvvm;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;


public class UserBean extends BaseObservable  {
    public String name;
    public String age;

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);

    }
    public void setAge(String age) {
        this.age = age;
        notifyPropertyChanged(BR.age);
    }
    @Bindable
    public String getName() {
        return this.name;
    }
    @Bindable
    public String getAge() {
        return this.age;
    }
}
