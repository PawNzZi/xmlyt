package cn.lingyikz.soundbook.soundbook.modle.v2;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HomeBanner {

    private Boolean success;
    private Integer code;
    private String message;
    private List<DataDTO> data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        private String createTime;
        private String createUser;
        private Object updateTime;
        private Object updateUser;
        private String id;
        private String name;
        private String code;
        private String value;
        private String remark;
        private Integer status;
    }
}
