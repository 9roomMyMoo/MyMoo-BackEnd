package com.example.mymoo.domain.store.entity;

import com.example.mymoo.domain.account.entity.Account;
import com.example.mymoo.domain.store.dto.response.GDreamCardResponse;
import com.example.mymoo.domain.store.exception.StoreException;
import com.example.mymoo.domain.store.exception.StoreExceptionDetails;
import com.example.mymoo.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TB_STORE")
@Entity
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "char(50)")
    private String name;

    @Column(name = "ZipCode", nullable = false, columnDefinition = "char(10)")
    private String zipCode;

    @Min(value = 0, message = "방문 횟수는 0 이상이어야 합니다.")
    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer visitCount;

    @Min(value = 0, message = "이용 가능 금액은 0 이상이어야 합니다.")
    @Column(nullable = false)
    @ColumnDefault("0")
    private Long usableDonation;

    @OneToOne(fetch = FetchType.LAZY)
    private AddressOld addressOld;

    @OneToOne(fetch = FetchType.LAZY)
    private AddressNew addressNew;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
//    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "account_id")
    private Account account;

    @Builder
    public Store(
            String name,
            String zipCode,
            Integer visitCount,
            Long usableDonation,
            AddressOld addressOld,
            AddressNew addressNew,
            Double longitude,
            Double latitude,
            Account account) {
        this.name = name;
        this.zipCode = zipCode;
        this.visitCount = visitCount;
        this.usableDonation = usableDonation;
        this.addressOld = addressOld;
        this.addressNew = addressNew;
        this.longitude = longitude;
        this.latitude = latitude;
        this.account = account;
    }




    public void incrementVisitCount() {
        this.visitCount++;
    }

    public void addUsableDonation(Long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("후원하는 금액은 0 이하일 수 없습니다.");
        }
        this.usableDonation += amount;
    }

    public void useUsableDonation(Long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("사용 금액은 0 이하일 수 없습니다.");
        }
        if (this.usableDonation < amount) {
            throw new StoreException(StoreExceptionDetails.NOT_ENOUGH_STORE_POINT);
        }
        this.usableDonation -= amount;
    }

    public void updateAddressOld(AddressOld addressOld) {
        this.addressOld = addressOld;
    }
    public void updateAddressNew(AddressNew addressNew) {
        this.addressNew = addressNew;
    }
}
