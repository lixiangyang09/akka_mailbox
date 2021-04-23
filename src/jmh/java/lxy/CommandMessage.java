package lxy;

import lombok.Builder;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Builder
@Value
@ToString
public class CommandMessage {
    @NonNull
    CommandType commandType;
}
