package cortez.archie.dev.rescuedroid;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 10/23/2017.
 */

public interface RescueService {

    @GET("centers/{id}/")
    Call<Center> getCenterInfo(@Path("id") String id);

    @GET("people/{id}/")
    Call<Person> getUserInfo(@Path("id") String id);
}
