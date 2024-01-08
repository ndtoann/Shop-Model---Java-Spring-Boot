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
@Table(name="orders")
public class Order
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name_customer;

    @Column(nullable=false)
    private String tel;

    @Column(nullable=false)
    private String email;

    @Column(nullable=false)
    private Long city_id;

    @Column(nullable=false)
    private Long district_id;

    @Column(nullable=false)
    private Long ward_id;

    @Column(nullable=false)
    private String address;

    @Column(nullable=false)
    private String method_pack;

    @Column(nullable=false)
    private String method_checkout;

    @Column(nullable=true)
    private String note;

    @Column(nullable=false)
    private Double total_money;

    @Column(nullable=false, columnDefinition = "int default 0")
    private Integer status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

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