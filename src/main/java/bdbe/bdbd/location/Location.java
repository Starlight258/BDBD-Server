package bdbe.bdbd.location;

import bdbe.bdbd._core.errors.exception.BadRequestError;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="location")
public class Location { //지역
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Column(name="place", length = 255, nullable = false)
    private String place; //장소명

    @Column(name="address", length = 255, nullable = false)
    private String address; //도로명 주소

    @Column(nullable = false)
    private double latitude; //위도

    @Column(nullable = false)
    private double longitude; //경도


    @Builder
    public Location(Long id, String place, String address, double latitude, double longitude) {
        validateLatitudeAndLongitude(latitude, longitude);

        this.id = id;
        this.place = place;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private void validateLatitudeAndLongitude(double latitude, double longitude) {
        if (latitude < -90 || latitude > 90) {
            throw new BadRequestError("Invalid latitude value. Latitude must be between -90 and 90.");
        }
        if (longitude < -180 || longitude > 180) {
            throw new BadRequestError("Invalid longitude value. Longitude must be between -180 and 180.");
        }
    }



    public void updateAddress(String address, String place, double latitude, double longitude) {
        validateLatitudeAndLongitude(latitude, longitude);

        this.place = place;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
