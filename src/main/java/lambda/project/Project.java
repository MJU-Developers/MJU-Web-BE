package lambda.project;

import com.google.gson.Gson;

public class Project {
    private Integer boardId;
    private String loginId;
    private String title;
    private String member;
    private String description;

    public Project(Integer boardId, String loginId, String title, String member, String description) {
        this.boardId = boardId;
        this.loginId = loginId;
        this.title = title;
        this.member = member;
        this.description = description;
    }


    public Project(String json) {
        Gson gson=new Gson();
        Project tempProject = gson.fromJson(json, Project.class);
        this.boardId = tempProject.boardId;
        this.loginId = tempProject.loginId;
        this.title = tempProject.title;
        this.member = tempProject.member;
        this.description = tempProject.description;
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
}
