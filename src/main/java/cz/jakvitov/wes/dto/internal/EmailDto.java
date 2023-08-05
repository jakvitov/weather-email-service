package cz.jakvitov.wes.dto.internal;

/**
 * Dto representing one email
 */

public class EmailDto {

    private String dest;
    private String subject;
    private String text;

    private String from;

    public EmailDto() {
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return "EmailDto{" +
                "dest='" + dest + '\'' +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                ", from='" + from + '\'' +
                '}';
    }
}
