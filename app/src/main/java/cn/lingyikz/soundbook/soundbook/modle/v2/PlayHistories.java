package cn.lingyikz.soundbook.soundbook.modle.v2;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PlayHistories {
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
            private Long userId;
            private Long albumId;
            private Long soundId;
            private Integer status;
            private Long playPiont;
            private ZmlmAlbumDTO zmlmAlbum;
            private ZmlmUserDTO zmlmUser;
            private ZmlmSoundDTO zmlmSound;

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

            @NoArgsConstructor
            @Data
            public static class ZmlmUserDTO {
                private Object createTime;
                private Object createUser;
                private Object updateTime;
                private Object updateUser;
                private Long id;
                private String nickname;
                private Object password;
                private Object email;
                private Object avatar;
                private Object phone;
                private Integer status;
                private Object level;
            }

            @NoArgsConstructor
            @Data
            public static class ZmlmSoundDTO {
                private Object createTime;
                private Object createUser;
                private Object updateTime;
                private Object updateUser;
                private Long id;
                private String name;
                private String description;
                private String player;
                private String url;
                private Integer episodes;
                private Long albumId;
            }
        }
    }
}
