package cn.lingyikz.soundbook.soundbook.modle;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class XmlyNextPaly {


    private Integer code;
    private String message;
    private List<DataDTO> data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        private Integer id;
        private String name;
        private Object description;
        private Object categories;
        private Object tags;
        private Object player;
        private String url;
        private Integer episodes;
        private Integer albumId;
        private Long created;
    }
}
