package lambda.home;


public class ProjectPhoto {
    private Integer boardId;
    private String loginId;
    private String title;
    private String member;
    private String description;
    private Integer photoId;
    private String fileType;
    private String fileName;

    public ProjectPhoto(Integer boardId, String loginId, String title, String member, String description, Integer photoId, String fileType, String fileName) {
        this.boardId = boardId;
        this.loginId = loginId;
        this.title = title;
        this.member = member;
        this.description = description;
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

    public Integer getBoardId() {
        return boardId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
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

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
