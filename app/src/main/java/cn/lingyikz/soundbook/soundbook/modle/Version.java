package cn.lingyikz.soundbook.soundbook.modle;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Version {


    private Integer code;
    private String message;
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        private Integer id;
        private Integer code;
        private String number;
        private String descrition;
        private Long created;
    }
}
