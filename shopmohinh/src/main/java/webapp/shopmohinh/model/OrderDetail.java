package webapp.shopmohinh.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="order_detail")
public class OrderDetail
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String product_name;

    @Column(nullable=false)
    private String product_image;

    @Column(nullable=false)
    private Double price;

    @Column(nullable=false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "oreder_id")
    private Order order;
}