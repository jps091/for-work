/*
package project.forwork.api.domain.salepost.infrastructure.query;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import project.forwork.api.domain.resume.infrastructure.QResumeEntity;
import project.forwork.api.domain.salepost.controller.model.SalePostResponse;
import project.forwork.api.domain.salepost.infrastructure.QSalePostEntity;
import project.forwork.api.domain.thumbnailimage.infrastructure.QThumbnailImageEntity;

import static project.forwork.api.domain.resume.infrastructure.QResumeEntity.*;
import static project.forwork.api.domain.salepost.infrastructure.QSalePostEntity.*;
import static project.forwork.api.domain.thumbnailimage.infrastructure.QThumbnailImageEntity.*;

@Repository
public class SalePostQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public SalePostQueryDslRepository(EntityManager em) {
        this.queryFactory  = new JPAQueryFactory(em);
    }

    public Page<SalePostResponse> search(SalePostSearchCond cond, Pageable pageable){
        queryFactory
                .select(Projections.constructor(SalePostResponse.class,
                        salePostEntity.id,
                        salePostEntity.title,
                        resumeEntity.price,
                        thumbnailImageEntity.url,
                        salePostEntity.viewCount,
                        salePostEntity.quantity,
                        resumeEntity.fieldType,
                        resumeEntity.levelType,
                        salePostEntity.registeredAt))
                .from(salePostEntity)
                .join(salePostEntity.resumeEntity, resumeEntity)
                .join(salePostEntity.thumbnailImageEntity, thumbnailImageEntity)
                .where()
    }
}
*/
