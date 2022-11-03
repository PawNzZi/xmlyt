package cn.lingyikz.soundbook.soundbook.modle;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Banner {

    private Integer code;
    private String message;
    private List<DataDTO> data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        private Integer id;
        private Integer typeid;
        private String value;
        private Object code;
        private Object remark;
        private Integer status;
        private Long createdat;
    }
}
