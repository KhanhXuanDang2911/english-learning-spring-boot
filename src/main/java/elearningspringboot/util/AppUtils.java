package elearningspringboot.util;

import com.github.slugify.Slugify;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class AppUtils {
    private static final Slugify slugify = Slugify.builder().build();

    public static String renderSlug(String text){
        return slugify.slugify(text);
    }

    public static boolean isValidSortParam(String input) {
        return input.matches("^[a-zA-Z][a-zA-Z0-9_]*:(asc|desc)$");
    }

    public static Pageable generatePageableWithSort(List<String> sorts, List<String> whileListFieldSorts, int pageNumber, int pageSize){
        Pageable pageable;
        List<Sort.Order> orders = new ArrayList<>();
        if (sorts != null) {
            orders = sorts.stream().filter(s -> {
                        boolean isValidSortParams = isValidSortParam(s);
                        if (!isValidSortParams) return false;
                        String[] sortPart = s.split(":");
                        return whileListFieldSorts.contains(sortPart[0]);
                    }).map(s -> {
                        String[] sortPart = s.split(":");
                        if (sortPart[1].equalsIgnoreCase("asc"))
                            return Sort.Order.asc(sortPart[0]);
                        else return Sort.Order.desc(sortPart[0]);
                    })
                    .toList();
        }
        if (orders.isEmpty())
            pageable = PageRequest.of(pageNumber - 1, pageSize);
        else
            pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(orders));
        return pageable;
    }
}
