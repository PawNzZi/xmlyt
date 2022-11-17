package cn.lingyikz.soundbook.soundbook.modle.v2;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Album{

    private Boolean success;
    private Integer code;
    private String message;
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        private Integer pageNo;
        private Integer pageSize;
        private Integer totalPage;
        private Integer totalRows;
        private List<RowsDTO> rows;
        private List<Integer> rainbow;
        private Boolean hasNextPage;
        private Integer nextPage;

        @NoArgsConstructor
        @Data
        public static class RowsDTO implements Serializable {
            private String createTime;
            private Object createUser;
            private Object updateTime;
            private Object updateUser;
            private Long id;
            private String name;
            private String description;
            private String author;
            private String thumb;
            private Integer weight;
            private Integer isdelete;
        }
    }
}
