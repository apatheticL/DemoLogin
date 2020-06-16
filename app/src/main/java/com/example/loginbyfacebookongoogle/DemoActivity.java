package com.example.loginbyfacebookongoogle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 111;
    @BindView(R.id.buttonLoginFaceTest)
    Button buttonLoginFace;
    @BindView(R.id.buttonLoginGoogleTest)
    Button buttonLoginGoogle;
    @BindView(R.id.buttonLogOutFace)
    Button buttonLogoutFace;
    @BindView(R.id.buttonLogOutGoogle)
    Button buttonLogoutGoogle;
    private CallbackManager callbackManager;
    private GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_login);
        ButterKnife.bind(this);
        callbackManager = CallbackManager.Factory.create();
        referenceGoogle();
        buttonLoginFace.setOnClickListener(this);
        buttonLoginGoogle.setOnClickListener(this);
        buttonLogoutFace.setOnClickListener(this);
        buttonLogoutGoogle.setOnClickListener(this);
    }

    private void referenceGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonLoginFaceTest:{
                loginFaceBook();
                break;
            }
            case R.id.buttonLoginGoogleTest:{
                googleLogin();
                break;
            }
            case R.id.buttonLogOutFace:{
                logOUtFace();
                break;
            }
            case R.id.buttonLogOutGoogle:{
                logoutGoogle();
            }
        }
    }

    private void logoutGoogle() {
        googleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"logout google",Toast.LENGTH_SHORT).show();
                    }
                });
        // disconnect account
        googleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"logout google",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void logOUtFace() {
        LoginManager.getInstance().logOut();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode== RC_SIGN_IN){
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

    public void googleLogin() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void loginFaceBook() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_birthday"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    String name = object.getString("name");
                                    String email = object.getString("email");
                                    if (email.equals("")) {
                                        Toast.makeText(getApplicationContext(), "Facebook ko co email", Toast.LENGTH_LONG).show();
                                    }

                                    Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
                                    Log.d("facebook", name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.i("MainActivity", "@@@onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i("MainActivity", "@@@onError: " + error.getMessage());
            }
        });
    }
}
