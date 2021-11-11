package edu.cnm.deepdive.signintest;

import android.app.Application;
import com.facebook.stetho.Stetho;
import edu.cnm.deepdive.signintest.service.GoogleSignInService;

public class SignInTestApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);
    GoogleSignInService.setContext(this);
  }

}
