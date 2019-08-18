package com.learning.rxoperators;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.learning.rxoperators.todolist.models.Task;
import com.learning.rxoperators.todolist.util.DataSource;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createOperator();
    }


    /**
     * Example of Create Operator. Operator used is fromIterable
     */

    private void createOperator(){

        // Method 1 working
       /* Observable<Task> taskObservable = Observable
                .fromIterable(DataSource.createTaskList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());*/

       //Method 2

        Observable<Task> taskObservable = Observable
                .fromIterable(DataSource.createTaskList())
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) throws Exception {
                        Thread.sleep(10000);
                        return task.isComplete();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
        taskObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: called");
            }

            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext: called"+Thread.currentThread().getName());
                Log.d(TAG, "onNext: called"+task.getDescription());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: called"+e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: called");
            }
        });
    }


}
