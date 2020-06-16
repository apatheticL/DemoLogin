package com.example.loginbyfacebookongoogle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.zxing.common.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginDifferent {
    public static final int RC_SIGN_IN = 123;
    private Context context;
    private Activity activity;
    private CallbackManager callbackManager;
    private GoogleSignInClient googleSignInClient;
    public LoginDifferent(Context context,
                          Activity activity, CallbackManager callbackManager,
                          GoogleSignInClient googleSignInClient) {
        this.context = context;
        this.activity = activity;
        this.callbackManager = callbackManager;
        this.googleSignInClient = googleSignInClient;
    }


    public void faceBookLogin() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "email", "user_birthday"));
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
                                        Toast.makeText(context, "Facebook ko co email", Toast.LENGTH_LONG).show();
                                    }

                                    Toast.makeText(context, name, Toast.LENGTH_LONG).show();
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

    public void googleLogin() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
