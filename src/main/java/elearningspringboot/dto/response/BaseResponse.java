package elearningspringboot.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseResponse {
    private String id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}