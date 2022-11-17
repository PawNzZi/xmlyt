package cn.lingyikz.soundbook.soundbook.modle.v2;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Category {


    private Boolean success;
    private Integer code;
    private String message;
    private List<DataDTO> data;

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
        private Object thumb;
        private Integer isdelete;
    }
}
