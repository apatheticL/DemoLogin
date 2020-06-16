package com.example.loginbyfacebookongoogle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.buttonLoginFace)
    Button buttonLoginWithFace;
    @BindView(R.id.buttonLoginGoogle)
    Button buttonLoginWithGoogle;
    private LoginDifferent loginDifferent;
    private CallbackManager callbackManager;
    private GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getData()==null){
            setContentView(R.layout.login_fragment);
            ButterKnife.bind(this);
            createRepuest();

            callbackManager =  CallbackManager.Factory.create();
            loginDifferent = new LoginDifferent(this, this, callbackManager,googleSignInClient);
            buttonLoginWithFace.setOnClickListener(this);
            buttonLoginWithGoogle.setOnClickListener(this);

        }
        else {

        }



    }

    private void updateUI(GoogleSignInAccount account) {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

        }
    }

    private void createRepuest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonLoginFace: {
                loginWithFacebook();
                break;
            }
            case R.id.buttonLoginGoogle:{
                loginWithGoogle();
                break;
            }}

    }

    private void loginWithGoogle() {
        loginDifferent.googleLogin();
    }

    private void loginWithFacebook() {
        loginDifferent.faceBookLogin();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode== loginDifferent.RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String accountData = account.getDisplayName()+ " " +account.getEmail() + account.getIdToken();
                Toast.makeText(this,accountData,Toast.LENGTH_SHORT).show();
            }catch (ApiException e){
                Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }

        }
        else {
            callbackManager.onActivityResult(requestCode,resultCode,data);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}