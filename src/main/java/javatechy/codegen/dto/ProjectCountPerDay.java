package javatechy.codegen.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ProjectCountPerDay {
    private String date;
    private long count;

    public ProjectCountPerDay(String date, long count) {
        this.date = date;
        this.count = count;
    }
}
