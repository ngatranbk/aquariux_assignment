package com.aquariux.cryptotrading.dto;

import com.aquariux.cryptotrading.model.User;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletBalanceDto {
  private BigDecimal usdtBalance;
  private BigDecimal ethBalance;
  private BigDecimal btcBalance;

  public static WalletBalanceDto fromUser(User user) {
    if (user == null) {
      return null;
    }
    WalletBalanceDto dto = new WalletBalanceDto();
    dto.setUsdtBalance(user.getUsdtBalance());
    dto.setBtcBalance(user.getBtcBalance());
    dto.setEthBalance(user.getEthBalance());
    return dto;
  }
}
