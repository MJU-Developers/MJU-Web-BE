package lambda.notice;

import com.google.gson.Gson;

import java.sql.Date;

public class Notice {
    private Integer noticeId;
    private String loginId;
    private String title;
    private String description;
    private Date date;

    public Notice(Integer noticeId, String loginId, String title, String description, Date date) {
        this.noticeId = noticeId;
        this.loginId = loginId;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public Notice(String json) {
        Gson gson=new Gson();
        Notice tempNotice = gson.fromJson(json, Notice.class);
        this.noticeId = tempNotice.noticeId;
        this.loginId = tempNotice.loginId;
        this.title = tempNotice.title;
        this.description = tempNotice.description;
        this.date = tempNotice.date;
    }

    public String toString() {
        return new Gson().toJson(this);
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
}
