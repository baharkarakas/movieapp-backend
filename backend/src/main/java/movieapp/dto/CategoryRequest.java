package movieapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryRequest {
    @NotBlank @Size(max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    private Boolean isPublic;

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Boolean getIsPublic() { return isPublic; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
}
