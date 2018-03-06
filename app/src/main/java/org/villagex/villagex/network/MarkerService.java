package org.villagex.villagex.network;

import org.villagex.villagex.data.Project;
import org.villagex.villagex.data.Village;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MarkerService {
    @GET("api/villages")
    Observable<List<Village>> getVillages();

    @GET("api/projects")
    Observable<List<Project>> getProjects();
}
