package cn.lingyikz.soundbook.soundbook.pojo;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PostInfo {


    private String message;

    private String nu;

    private String ischeck;

    private String com;

    private String status;

    private String condition;

    private String state;

    private List<DataDTO> data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {

        private String time;

        private String context;

        private String ftime;
    }
}
