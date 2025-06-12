package interfaces;

import java.util.Set;
import model.User;

public interface Likeable {
    void like(User user);
    void unlike(User user);
    int getLikeCount();
    Set<User> getLikedByUsers();
}
