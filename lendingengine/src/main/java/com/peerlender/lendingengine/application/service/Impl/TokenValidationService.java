package com.peerlender.lendingengine.application.service.Impl;

import com.peerlender.lendingengine.domain.model.User;

public interface TokenValidationService {
    User validateTokenAndGetUser(final String token);

}
