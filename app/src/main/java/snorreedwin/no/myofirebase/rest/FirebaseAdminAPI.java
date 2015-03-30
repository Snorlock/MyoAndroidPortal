package snorreedwin.no.myofirebase.rest;

import org.json.JSONObject;

import java.util.Map;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import snorreedwin.no.myofirebase.models.Account;
import snorreedwin.no.myofirebase.models.FirebaseCreateResponse;

public interface FirebaseAdminAPI {

    //or if there are some special cases you can process your response manually
    @GET("/account")
    public void fetchUser(@Query(value="token", encodeValue=false) String token,
                          Callback<Account> accountCallback);

    @POST("/firebase/{id}")
    public void createFirebase(@Path("id") String id,
                               @Query(value="token", encodeValue=false) String token,
                               @Query(value="appName") String appName,
                               Callback<FirebaseCreateResponse> createCallback);
}
