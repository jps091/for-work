package project.forwork.api.domain.cartresume.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.api.common.infrastructure.BaseTimeEntity;
import project.forwork.api.domain.cart.infrastructure.CartEntity;
import project.forwork.api.domain.cartresume.model.CartResume;
import project.forwork.api.domain.resume.infrastructure.ResumeEntity;

@Entity
@Table(name = "cart_resumes")
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartResumeEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_resume_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEntity cartEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private ResumeEntity resumeEntity;


    public static CartResumeEntity from(CartResume cartResume){
        CartResumeEntity cartResumeEntity = new CartResumeEntity();
        cartResumeEntity.id = cartResume.getId();
        cartResumeEntity.cartEntity = CartEntity.from(cartResume.getCart());
        cartResumeEntity.resumeEntity = ResumeEntity.from(cartResume.getResume());
        return cartResumeEntity;
    }

    public CartResume toModel(){
        return CartResume.builder()
                .id(id)
                .cart(cartEntity.toModel())
                .resume(resumeEntity.toModel())
                .build();
    }
}
