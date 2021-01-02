package khushtar.e.camerax;

public class PhotoHandler  {

    private String imageName,date,time,type,url;

    public PhotoHandler(String date, String imageName, String time, String type,String url) {
        this.imageName = imageName;
        this.date = date;
        this.time = time;
        this.type = type;
        this.url=url;
    }

    public PhotoHandler() {
        this.imageName="Image Name";
        this.date = "Date";
        this.time = "Time";
        this.type = "Type";
        this.url="R.drawable.group_pic";
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
