package edu.cnm.deepdive.signintest.controller;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import edu.cnm.deepdive.signintest.databinding.ActivityMainBinding;
import edu.cnm.deepdive.signintest.viewmodel.LoginViewModel;

public class MainActivity extends AppCompatActivity {

  private LoginViewModel loginViewModel;
  private ActivityMainBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    getLifecycle().addObserver(loginViewModel);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    binding.signOut.setOnClickListener((v) -> loginViewModel.signOut());
    loginViewModel.getAccount().observe(this, this::handleAccount);
    setContentView(binding.getRoot());
  }

  private void handleAccount(GoogleSignInAccount account) {
    if (account != null) {
      binding.displayName.setText(account.getDisplayName());
    } else {
      Intent intent = new Intent(this, LoginActivity.class)
          .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
    }
  }

}