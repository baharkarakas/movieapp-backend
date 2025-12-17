package com.bahar.movieapp.dto;

import jakarta.validation.constraints.NotNull;

public class WatchlistDtos {

    public static class AddWatchlistRequest {
        @NotNull
        public Long categoryId;

        @NotNull
        public Long movieId;
    }

    public static class WatchlistItemResponse {
        public Long id;
        public Long categoryId;
        public String categoryName;

        public Long movieId;
        public String title;
        public String director;
        public Integer year;
        public String posterUrl;

        public WatchlistItemResponse(Long id,
                                     Long categoryId, String categoryName,
                                     Long movieId, String title, String director, Integer year, String posterUrl) {
            this.id = id;
            this.categoryId = categoryId;
            this.categoryName = categoryName;
            this.movieId = movieId;
            this.title = title;
            this.director = director;
            this.year = year;
            this.posterUrl = posterUrl;
        }
    }
}
