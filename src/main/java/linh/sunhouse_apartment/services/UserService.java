package linh.sunhouse_apartment.services;

import linh.sunhouse_apartment.dtos.request.AuthenticationRequest;
import linh.sunhouse_apartment.dtos.response.AuthenticationResponse;

public interface UserService {
    AuthenticationResponse authenticate (AuthenticationRequest request);
}
