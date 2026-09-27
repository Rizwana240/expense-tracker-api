package expense_tracker_api;

import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CategorizerService {

    private static final Map<String, String> KEYWORD_MAP = new LinkedHashMap<>();
    static {
        KEYWORD_MAP.put("uber", "Transport");
        KEYWORD_MAP.put("ola", "Transport");
        KEYWORD_MAP.put("petrol", "Transport");
        KEYWORD_MAP.put("fuel", "Transport");
        KEYWORD_MAP.put("swiggy", "Food");
        KEYWORD_MAP.put("zomato", "Food");
        KEYWORD_MAP.put("restaurant", "Food");
        KEYWORD_MAP.put("grocery", "Food");
        KEYWORD_MAP.put("rent", "Housing");
        KEYWORD_MAP.put("electricity", "Utilities");
        KEYWORD_MAP.put("wifi", "Utilities");
        KEYWORD_MAP.put("internet", "Utilities");
        KEYWORD_MAP.put("movie", "Entertainment");
        KEYWORD_MAP.put("netflix", "Entertainment");
        KEYWORD_MAP.put("spotify", "Entertainment");
        KEYWORD_MAP.put("book", "Education");
        KEYWORD_MAP.put("course", "Education");
        KEYWORD_MAP.put("tuition", "Education");
    }

    public String categorize(String description) {
        String lower = description.toLowerCase();
        for (Map.Entry<String, String> entry : KEYWORD_MAP.entrySet()) {
            if (lower.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "Other";
    }
}