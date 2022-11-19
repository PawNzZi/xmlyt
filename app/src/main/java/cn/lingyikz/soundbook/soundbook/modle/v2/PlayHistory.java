package cn.lingyikz.soundbook.soundbook.modle.v2;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PlayHistory {


    private Boolean success;
    private Integer code;
    private String message;
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
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
        private Object zmlmUser;
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
        public static class ZmlmSoundDTO {
            private Object createTime;
            private Object createUser;
            private Object updateTime;
            private Object updateUser;
            private Long id;
            private String name;
            private String description;
            private Object player;
            private String url;
            private Integer episodes;
            private Long albumId;
        }
    }
}
