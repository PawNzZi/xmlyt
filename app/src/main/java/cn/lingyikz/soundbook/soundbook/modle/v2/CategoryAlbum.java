package cn.lingyikz.soundbook.soundbook.modle.v2;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CategoryAlbum {


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
        public static class RowsDTO {
            private Object createTime;
            private Object createUser;
            private Object updateTime;
            private Object updateUser;
            private Long id;
            private Long albumId;
            private Long categoryId;
            private Object albumName;
            private Object categoryName;
            private ZmlmCategoryDTO zmlmCategory;
            private ZmlmAlbumDTO zmlmAlbum;

            @NoArgsConstructor
            @Data
            public static class ZmlmCategoryDTO {
                private Object createTime;
                private Object createUser;
                private Object updateTime;
                private Object updateUser;
                private Long id;
                private String name;
                private Object description;
                private Object thumb;
                private Integer isdelete;
            }

            @NoArgsConstructor
            @Data
            public static class ZmlmAlbumDTO {
                private Object createTime;
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
}
