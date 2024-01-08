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
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable=false)
    private String productName;

    @Column(nullable=false)
    private String product_image;

    @Column(nullable=false)
    private Double price;

    @Column(nullable=false, columnDefinition = "LONGTEXT")
    private String description;

    @Column(nullable=false, columnDefinition = "LONGTEXT")
    private String detail;

    @Column(nullable=false, columnDefinition = "int default 0")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

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
