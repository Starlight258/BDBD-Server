package bdbe.bdbd.carwash;

import bdbe.bdbd.file.File;
import bdbe.bdbd.keyword.carwashKeyword.CarwashKeyword;
import bdbe.bdbd.location.Location;
import bdbe.bdbd.member.Member;
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
@Table(name="carwash")
public class Carwash{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Column(length = 100, nullable = false)
    private String name; //세차장 이름

    @Column(nullable = false)
    private double rate = 0; // 별점

    @Column(length = 50, nullable = false)
    private String tel; //전화번호

    @Column(length = 255, nullable = false)
    private String des; //세차장 설명

    @Column(name="price", nullable = false)
    private int price; //30분당 가격

    @OneToOne //일대일-소유측
    @JoinColumn(name="l_id", nullable = false) //외래키
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY) //외래키
    @JoinColumn(name="m_id",  nullable = false)
    private Member member;

    @OneToMany(mappedBy = "carwash") //양방향 비소유측, 1:1 참조 - readOnly
    private List<CarwashKeyword> carwashKeywords = new ArrayList<>();

    @OneToMany(mappedBy = "carwash") //양방향 비소유측, 1:1 참조  - readOnly
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
