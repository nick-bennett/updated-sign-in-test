package edu.cnm.deepdive.signintest.viewmodel;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import edu.cnm.deepdive.signintest.service.GoogleSignInService;
import io.reactivex.disposables.CompositeDisposable;

public class LoginViewModel extends AndroidViewModel implements DefaultLifecycleObserver {

  private final GoogleSignInService repository;
  private final MutableLiveData<GoogleSignInAccount> account;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  public LoginViewModel(@NonNull Application application) {
    super(application);
    repository = GoogleSignInService.getInstance();
    account = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    refresh();
  }

  public void refresh() {
    pending.add(
        repository
            .refresh()
            .subscribe(
                account::postValue,
                (throwable) -> this.account.postValue(null)
            )
    );
  }

  public LiveData<GoogleSignInAccount> getAccount() {
    return account;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void startSignIn(ActivityResultLauncher<Intent> launcher) {
    repository.startSignIn(launcher);
  }

  public void completeSignIn(ActivityResult result) {
    pending.add(
        repository
            .completeSignIn(result)
            .subscribe(
                account::postValue,
                this::postThrowable
            )
    );
  }

  public void signOut() {
    pending.add(
        repository
            .signOut()
            .subscribe(
                () -> account.postValue(null),
                this::postThrowable
            )
    );
  }

  private void postThrowable(Throwable throwable) {
    Log.e(getClass().getSimpleName(), throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

  @Override
  public void onStop(@NonNull LifecycleOwner owner) {
    DefaultLifecycleObserver.super.onStop(owner);
    pending.clear();
  }

}
