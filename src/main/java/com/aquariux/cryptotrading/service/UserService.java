package com.aquariux.cryptotrading.service;

import com.aquariux.cryptotrading.dto.WalletBalanceDto;
import com.aquariux.cryptotrading.model.User;
import com.aquariux.cryptotrading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  @Autowired private UserRepository userRepository;

  public WalletBalanceDto retrieveWalletBalance(Long userId) {
    User user = userRepository.findById(userId).orElse(null);
    if (user == null) {
      return null;
    }
    return WalletBalanceDto.fromUser(user);
  }
}
