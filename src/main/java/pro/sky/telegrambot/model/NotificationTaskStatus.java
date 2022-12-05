package pro.sky.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity
public class NotificationTaskStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String status;

    @OneToMany(mappedBy = "status")
    @JsonBackReference
    private Collection<NotificationTask> notificationTasks;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Collection<NotificationTask> getNotificationTasks() {
        return notificationTasks;
    }

    public void setNotificationTasks(Collection<NotificationTask> notificationTasks) {
        this.notificationTasks = notificationTasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotificationTaskStatus that = (NotificationTaskStatus) o;

        if (id != that.id) return false;
        return status != null ? status.equals(that.status) : that.status == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NotificationTaskStatus{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", notificationTasks=" + notificationTasks.stream().map(NotificationTask::getId)
                .collect(Collectors.toList()) +
                '}';
    }
}
