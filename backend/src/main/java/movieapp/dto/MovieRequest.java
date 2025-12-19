package movieapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MovieRequest {

    @NotBlank
    private String title;

    private String director;

    @NotNull
    private Integer year;

    private String posterUrl;

    public String getTitle() { return title; }
    public String getDirector() { return director; }
    public Integer getYear() { return year; }
    public String getPosterUrl() { return posterUrl; }

    public void setTitle(String title) { this.title = title; }
    public void setDirector(String director) { this.director = director; }
    public void setYear(Integer year) { this.year = year; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
}
