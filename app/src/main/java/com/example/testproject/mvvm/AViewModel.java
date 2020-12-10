package com.example.testproject.mvvm;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AViewModel extends ViewModel {
  private MutableLiveData<String> liveData;

    /**
     * 获取liveData
     */
  public LiveData getLiveData()
  {
       if(liveData==null)
       {
           liveData=new MutableLiveData<>();
           loadData();
       }
      return liveData;
  }
  private void loadData()
  {
      new Thread(new Runnable() {
          @Override
          public void run() {
            liveData.postValue("这是LiveData的数据..........嘿嘿");
          }
      }).start();
  }

  public void updataUser()
  {
      loadData();
  }
}
