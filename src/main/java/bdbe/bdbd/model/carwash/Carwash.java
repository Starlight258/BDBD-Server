package bdbe.bdbd.model.carwash;

import bdbe.bdbd.model.file.File;
import bdbe.bdbd.model.keyword.carwashKeyword.CarwashKeyword;
import bdbe.bdbd.model.location.Location;
import bdbe.bdbd.model.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "carwash")
public class Carwash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private double rate = 0;

    @Column(length = 50, nullable = false)
    private String tel;

    @Column(nullable = false)
    private String des;

    @Column(name = "price", nullable = false)
    private int price;

    @OneToOne
    @JoinColumn(name = "l_id", nullable = false)
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "m_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "carwash")
    private List<CarwashKeyword> carwashKeywords = new ArrayList<>();

    @OneToMany(mappedBy = "carwash")
    private List<File> fileList = new ArrayList<>();

    @Builder
    public Carwash(Long id, String name, double rate, String tel, String des, int price, Location location, Member member) {
        this.id = id;
        this.name = name;
        this.rate = rate;
        this.tel = tel;
        this.des = des;
        this.price = price;
        this.location = location;
        this.member = member;
    }


    public void updateRate(double rate) {
        this.rate = rate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
