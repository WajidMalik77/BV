package com.background.video.recorder.camera.recorder.util;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.background.video.recorder.camera.recorder.model.Authentication;
import com.background.video.recorder.camera.recorder.ui.fragment.ForgotFragment;
import com.background.video.recorder.camera.recorder.ui.fragment.SplashTwoFragment;
import com.background.video.recorder.camera.recorder.ui.fragment.VerifyUserFragment;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class UserAuthenticationUtil {
    private static final String TAG = "UserAuthenticationUtil";
    private final Context context;
    private final UserVerification userVerification;
    private final boolean from;
    private boolean userVerified;
    private boolean userAnswerVerified;

    public UserAuthenticationUtil(Context context, UserVerification userVerification, boolean from) {
        this.context = context;
        this.userVerification = userVerification;
        this.from = from;
    }

    public void userAuthentication(@NonNull List<Authentication> userAuthentication) {
        Observable.fromIterable(userAuthentication)
                .observeOn(Schedulers.computation())
                .subscribeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<Authentication>() {
                    @Override
                    public boolean test(@NonNull Authentication authentication) throws Exception {
                        int pass = authentication.getUserPassword();
                        Log.d(TAG, "test: " + VerifyUserFragment.userEnteredPassword);
                        if (from) {
                            if (pass == SplashTwoFragment.userEnteredPassword) {
                                userVerified = true;
                            } else {
                                Log.d(TAG, "test: password in database" + pass);
                                userVerified = false;
                            }
                        } else {
                            String answer = authentication.getUserAnswer();
                            Log.e(TAG, "test: " + ForgotFragment.userEnterAsnwer);
                            Log.e(TAG, "test: from forgot pass" + answer);
                            userAnswerVerified = answer.equals(ForgotFragment.userEnterAsnwer);
                            Log.e(TAG, "test: " + userAnswerVerified);
                         //   userVerification.userVerified(userAnswerVerified);
                        }


                        return false;
                    }
                }).subscribe(new Observer<Authentication>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Authentication authentication) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                if (from) {
                    userVerification.userVerified(userVerified);
                } else {
                    userVerification.userVerified(userAnswerVerified);
                }


            }
        });
    }

    public interface UserVerification {
        void userVerified(boolean password);
    }
}
