package org.villagex.villagex.network;

import org.villagex.villagex.model.Config;
import org.villagex.villagex.model.Project;
import org.villagex.villagex.model.Village;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface DataService {
    @GET("api/villages")
    Observable<List<Village>> getVillages();

    @GET("api/projects")
    Observable<List<Project>> getProjects();

    @GET("api/versions")
    Observable<Config> getConfig();
}
