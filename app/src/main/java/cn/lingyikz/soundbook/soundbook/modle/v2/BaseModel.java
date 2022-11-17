package cn.lingyikz.soundbook.soundbook.modle.v2;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BaseModel{

    private Boolean success;
    private Integer code;
    private String message;
    private String data;
}
