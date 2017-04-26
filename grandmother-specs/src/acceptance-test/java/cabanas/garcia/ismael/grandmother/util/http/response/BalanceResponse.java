package cabanas.garcia.ismael.grandmother.util.http.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@Getter
@ToString
public class BalanceResponse {
    private String balance;
}
