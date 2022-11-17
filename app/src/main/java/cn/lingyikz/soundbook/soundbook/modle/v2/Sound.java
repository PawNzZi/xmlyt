package cn.lingyikz.soundbook.soundbook.modle.v2;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Sound {


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
        private String name;
        private Object description;
        private Object player;
        private String url;
        private Integer episodes;
        private Long albumId;
    }
}
