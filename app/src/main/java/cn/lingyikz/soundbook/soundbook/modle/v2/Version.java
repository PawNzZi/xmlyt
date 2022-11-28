package cn.lingyikz.soundbook.soundbook.modle.v2;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Version {


    private Boolean success;
    private Integer code;
    private String message;
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        private String createTime;
        private Object createUser;
        private Object updateTime;
        private Object updateUser;
        private Long id;
        private Integer code;
        private String number;
        private String descrition;
        private String downloadUrl ;
    }
}
