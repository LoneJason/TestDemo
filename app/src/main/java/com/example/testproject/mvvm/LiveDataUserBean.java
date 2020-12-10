package com.example.testproject.mvvm;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LiveDataUserBean extends ViewModel
{
    public MutableLiveData<String> user=new MutableLiveData<>("小明");
    public MutableLiveData<String> old=new MutableLiveData<>("18");
}
