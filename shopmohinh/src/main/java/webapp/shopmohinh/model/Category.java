package webapp.shopmohinh.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String cate_name;

    @Column(nullable=false)
    private String cate_image;

    @Column(nullable=false)
    private String banner;

    @Column(nullable=false, columnDefinition = "int default 0")
    private Integer status;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    @Column(nullable=true)
    private LocalDateTime created_at;

    @Column(nullable=true)
    private LocalDateTime updated_at;

    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        updated_at = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    @PreUpdate
    protected void onUpdate() {
        created_at = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        updated_at = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }
}
