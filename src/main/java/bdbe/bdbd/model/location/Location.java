package bdbe.bdbd.model.location;

import bdbe.bdbd._core.exception.BadRequestError;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collections;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Column(name="place", length = 255, nullable = false)
    private String place;

    @Column(name="address", length = 255, nullable = false)
    private String address;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;


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
            throw new BadRequestError(
                    BadRequestError.ErrorCode.VALIDATION_FAILED,
                    Collections.singletonMap("Invalid latitude value", "Latitude must be between -90 and 90.")
            );
        }
        if (longitude < -180 || longitude > 180) {
            throw new BadRequestError(
                    BadRequestError.ErrorCode.VALIDATION_FAILED,
                    Collections.singletonMap("Invalid longitude value", "Longitude must be between -90 and 90.")
            );
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
