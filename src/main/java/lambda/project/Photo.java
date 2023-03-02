package lambda.project;

import com.google.gson.Gson;

public class Photo {
    private Integer photoId;
    private Integer boardId;
    private Integer noticeId;
    private String fileType;
    private String fileName;

    public Photo(Integer photoId, Integer boardId, Integer noticeId, String fileType, String fileName) {
        this.photoId = photoId;
        this.boardId = boardId;
        this.noticeId = noticeId;
        this.fileType = fileType;
        this.fileName = fileName;
    }
    public Photo(String json) {
        Gson gson=new Gson();
        Photo tempNotice = gson.fromJson(json, Photo.class);
        this.photoId = tempNotice.photoId;
        this.boardId = tempNotice.boardId;
        this.noticeId = tempNotice.noticeId;
        this.fileType = tempNotice.fileType;
        this.fileName = tempNotice.fileName;
    }

    public Integer getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Integer photoId) {
        this.photoId = photoId;
    }

    public Integer getBoardId() {
        return boardId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }

    public Integer getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Integer noticeId) {
        this.noticeId = noticeId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
