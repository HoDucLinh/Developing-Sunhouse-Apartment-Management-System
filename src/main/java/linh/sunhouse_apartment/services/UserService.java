package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.dtos.request.AuthenticationRequest;
import linh.sunhouse_apartment.dtos.response.AuthenticationResponse;
import linh.sunhouse_apartment.entity.User;

public interface UserService {
    // AuthenticationResponse authenticate (AuthenticationRequest request);
    boolean createUser(User user);

}
