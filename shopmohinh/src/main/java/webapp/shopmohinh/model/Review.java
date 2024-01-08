package webapp.shopmohinh.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="reviews")
public class Review
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name_user;

    @Column(nullable=false)
    private String email;

    @Column(nullable=false)
    private Integer rated;

    @Column(nullable=false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable=false, columnDefinition = "int default 0")
    private Integer status;

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