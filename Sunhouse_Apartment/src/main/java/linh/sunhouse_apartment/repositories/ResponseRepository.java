package linh.sunhouse_apartment.repositories;

import linh.sunhouse_apartment.entity.Question;
import linh.sunhouse_apartment.entity.Response;

import java.util.List;

public interface ResponseRepository {
    void save(Response response);
    List<Response> getResponses(Question question);
}
