package examples.android.md.rx.rv.com.org.simplerxandroid;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {
    private static  final String TAG = MainActivity.class.getName();
    private Button mButton, mButton2;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        mButton = (Button) findViewById(R.id.mButton);
        mButton2 = (Button) findViewById(R.id.mButton2);
        mTextView = (TextView) findViewById(R.id.mTextView);
        Subscription mButtonSubscription = RxView.clicks(mButton).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Log.d(TAG, "call");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "In the run");
                        mTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                mTextView.setText("Text after clicked");
                            }
                        });
                    }
                }).start();
            }
        });

        //Let's see this framework in action with a concrete example. First, let's create a basic Observable:
        //Create the observables
        final Observable<String> myObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello World");
                subscriber.onCompleted();
            }
        });

        final Observable<Integer>integerObservable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                Log.d(TAG, "call");
                subscriber.onNext(21);
                subscriber.onCompleted();
            }
        });

        final Observable<String> stringObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String name = "Bruce";
                String lastName = "Wayne";
                subscriber.onNext(name + " " + lastName);
            }
        });

        //Subscribers definition
        final Subscriber<Integer>integerSubscriber = new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError::" + e.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "::Integer onNext::" + integer);
            }
        };


        //Our Observable emits "Hello, world!" then completes. Now let's create a Subscriber to consume the data:
        final Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError::" + e.getMessage());
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, s);
            }
        };

        final Subscriber<String> stringSubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError::" + e.getMessage());
            }

            @Override
            public void onNext(String s) {
                s += "::Result Afther the subscription";
                Log.d(TAG, s);
            }
        };

        //This button subscribes the subscrbers to each Observer
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myObservable.subscribe(mySubscriber);
                integerObservable.subscribe(integerSubscriber);
                stringObservable.subscribe(stringSubscriber);

            }
        });


    }
}
