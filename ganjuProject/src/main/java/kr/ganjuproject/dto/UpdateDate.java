package kr.ganjuproject.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Value;

import java.time.LocalDateTime;

@Getter
@Value
public class UpdateDate {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy hh:mm:ss a", timezone = "Asia/Seoul")
    private LocalDateTime start;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy hh:mm:ss a", timezone = "Asia/Seoul")
    private LocalDateTime end;
}
