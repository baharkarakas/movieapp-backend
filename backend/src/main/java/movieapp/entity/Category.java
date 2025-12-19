package movieapp.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "categories",
        uniqueConstraints = @UniqueConstraint(name = "ux_categories_user_name", columnNames = {"user_id", "name"})
)
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic = false;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Category() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isPublic() { return isPublic; }
    public User getUser() { return user; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPublic(boolean aPublic) { isPublic = aPublic; }
    public void setUser(User user) { this.user = user; }
}
