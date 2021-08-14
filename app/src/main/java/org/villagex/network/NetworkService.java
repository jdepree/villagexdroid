package org.villagex.network;

import org.villagex.model.Config;
import org.villagex.model.Project;
import org.villagex.model.Village;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface NetworkService {
    @GET("api/villages")
    Observable<List<Village>> getVillages();

    @GET("api/projects")
    Observable<List<Project>> getProjects();

    @GET("api/versions")
    Observable<Config> getConfig();
}
