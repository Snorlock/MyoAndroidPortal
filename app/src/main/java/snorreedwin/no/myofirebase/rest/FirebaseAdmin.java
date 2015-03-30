package snorreedwin.no.myofirebase.rest;

import retrofit.RestAdapter;

public class FirebaseAdmin {
    private final String baseUrl = "https://admin.firebase.com/";
    private final FirebaseAdminAPI service;
    public RestAdapter restAdapter;

    public FirebaseAdmin() {
        restAdapter = new RestAdapter.Builder()
                .setEndpoint(baseUrl)
                .build();
        service = restAdapter.create(FirebaseAdminAPI.class);


    }

    public FirebaseAdminAPI getService() {
        return service;
    }


}
