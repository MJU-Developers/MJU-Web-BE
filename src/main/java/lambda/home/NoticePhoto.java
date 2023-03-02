package lambda.home;

import java.sql.Date;

public class NoticePhoto {
    private Integer noticeId;
    private String loginId;
    private String title;
    private String description;
    private Date date;
    private Integer photoId;
    private String fileType;
    private String fileName;

    public NoticePhoto(Integer noticeId, String loginId, String title, String description, Date date, Integer photoId, String fileType, String fileName) {
        this.noticeId = noticeId;
        this.loginId = loginId;
        this.title = title;
        this.description = description;
        this.date = date;
        this.photoId = photoId;
        this.fileType = fileType;
        this.fileName = fileName;
    }

    public Integer getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Integer photoId) {
        this.photoId = photoId;
    }

    public Integer getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Integer noticeId) {
        this.noticeId = noticeId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
