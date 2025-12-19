package movieapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryDtos {

    public static class CreateCategoryRequest {
        @NotBlank
        @Size(max = 100)
        public String name;
    }

    public static class UpdateCategoryRequest {
        @NotBlank
        @Size(max = 100)
        public String name;
    }

    public static class CategoryResponse {
        public Long id;
        public String name;

        public CategoryResponse(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
