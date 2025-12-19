package movieapp.dto;

public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private boolean isPublic;
    private Long userId;

    public CategoryResponse(Long id, String name, String description, boolean isPublic, Long userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.userId = userId;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isPublic() { return isPublic; }
    public Long getUserId() { return userId; }
}
