package com.slt.restapi;

import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.slt.data.User;
import com.slt.fragments.LoginFragment;
import com.slt.fragments.RegisterFragment;
import com.slt.restapi.data.REST_Location;
import com.slt.restapi.data.REST_User;
import com.slt.restapi.data.REST_User_Functionalities;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Usman Ahmad on 13.01.2018.
 */

public class OtherRestCalls {


    public static void createUser_Functionalities(String email, final RegisterFragment context) {

        REST_User_Functionalities r_u_f = new REST_User_Functionalities();
        r_u_f.email = email;
        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.createUser_Functionalities(r_u_f);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    Singleton test = null;
                    try {
                        test = new Gson().fromJson(response.body().toString(), Singleton.class);
                        context.showSnackBarMessage("Registering successfull.");
                    }catch(Exception e) {
                        boolean debug = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    public static void retrieveUser_Functionalities(final User user, final LoginFragment context) {

        REST_User_Functionalities r_u_f = new REST_User_Functionalities();
        r_u_f.email = user.getEmail();

        Endpoints api = RetroClient.getApiService();
        Call<JsonObject> call = api.getUser_Functionalities(r_u_f);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    Singleton test = null;
                    try {
                        test = new Gson().fromJson(response.body().toString(), Singleton.class);
                    }catch(Exception e) {
                        boolean debug = true;
                    }
                    TemporaryDB.getInstance().setAppUser(test.getResponse_user_functionalities());
                    TemporaryDB.getInstance().h_users.put(user, test.getResponse_user_functionalities());
                    context.openTimelineActivity();


                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public static void retrieveFriends() {

        REST_User_Functionalities r_u_f = TemporaryDB.getInstance().getAppUser();
        ArrayList<String> friends = new ArrayList<>();

        //TODO: Dynamically add FriendIds
        friends.add("5a5a4e85a2c2412bf0efa460");
        friends.add("5a5a4ea4a2c2412bf0efa461");
        friends.add("5a5a4f65a2c2412bf0efa462");
        r_u_f.friends = friends;

        if(r_u_f != null) {

            Endpoints api = RetroClient.getApiService();
            Call<JsonObject> call = api.getFriends(r_u_f);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        Singleton test = null;
                        try {
                            test = new Gson().fromJson(response.body().toString(), Singleton.class);
                            boolean debug = true;
                        } catch (Exception e) {
                            boolean debug = true;
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });

        }
    }

    public static void retrieveTimelines() {

        REST_User_Functionalities r_u_f = TemporaryDB.getInstance().getAppUser();
        ArrayList<String> friends = new ArrayList<>();

        //TODO: Dynamically add FriendIds
        friends.add("5a5a4e85a2c2412bf0efa460");
        friends.add("5a5a4ea4a2c2412bf0efa461");
        friends.add("5a5a4f65a2c2412bf0efa462");
        r_u_f.friends = friends;

        if(r_u_f != null) {

            Endpoints api = RetroClient.getApiService();
            Call<JsonObject> call = api.getCompleteTimeLines(r_u_f);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        Singleton test = null;
                        try {
                            test = new Gson().fromJson(response.body().toString(), Singleton.class);
                            boolean debug = true;
                        } catch (Exception e) {
                            boolean debug = true;
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });

        }
    }

    public static void updateUser(User user) {


        REST_User_Functionalities r_u_f = TemporaryDB.getInstance().getAppUser();
        r_u_f.email = user.getEmail();
        r_u_f.foreName = user.getForeName();
        r_u_f.lastName = user.getLastName();

        LinkedList<User> users = user.getUserList();
        ArrayList<String> ids = new ArrayList<>();

        for(User u: users) {
            REST_User_Functionalities friend = TemporaryDB.getInstance().h_users.get(u);

            if(friend != null) {
                ids.add(friend._id);
            }
        }

        r_u_f.friends = ids;
        Location l = user.getLastLocation();

        REST_Location r_l = new REST_Location(l.getLatitude(), l.getLongitude());

        r_u_f.lastLocation = r_l;
        r_u_f.lastLocationUpdateDate = user.getLastLocationUpdateDate();
        r_u_f.myAge = user.getMyAge();
        r_u_f.myCity = user.getMyCity();
        r_u_f.myImage = user.getMyImage();

        if(r_u_f != null) {

            Endpoints api = RetroClient.getApiService();
            Call<JsonObject> call = api.updateUser_Functionalities(r_u_f);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        Singleton test = null;
                        try {
                            test = new Gson().fromJson(response.body().toString(), Singleton.class);
                        } catch (Exception e) {
                            boolean debug = true;
                        }



                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });

        }
    }


}
