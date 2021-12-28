package objectProtocol;

import java.sql.Date;

public class FestivalRequest implements Request{
    private Date date;

    public FestivalRequest(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
