package databases;

import java.util.List;

public interface Searchable {
    List query(String query);
}
