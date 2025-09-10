package com.bci.user.service;

import com.bci.user.dto.PhoneRequest;
import com.bci.user.dto.PhoneResponse;
import com.bci.user.model.Phone;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for mapping between {@link Phone}, {@link PhoneRequest}, and {@link PhoneResponse} objects.
 * <p>
 * Provides static methods to convert:
 * <ul>
 *   <li>Request DTOs ({@link PhoneRequest}) to entity objects ({@link Phone}).</li>
 *   <li>Entity objects ({@link Phone}) to response DTOs ({@link PhoneResponse}).</li>
 * </ul>
 * This class is stateless and should be used only as a converter.
 * </p>
 */
public class PhoneMapper {

  /**
   * Converts a list of {@link PhoneRequest} objects into a list of {@link Phone} entities.
   *
   * @param phoneRequests the list of request DTOs to convert; may be {@code null}
   * @return a list of {@link Phone} entities, or {@code null} if the input list is {@code null}
   */
  public static List<Phone> toEntities(List<PhoneRequest> phoneRequests) {
    if (phoneRequests == null) return null;

    return phoneRequests.stream().map(PhoneMapper::toEntity).collect(Collectors.toList());
  }

  /**
   * Converts a single {@link PhoneRequest} into a {@link Phone} entity.
   *
   * @param phoneRequest the request DTO containing phone details
   * @return a {@link Phone} entity populated with the provided details
   */
  public static Phone toEntity(PhoneRequest phoneRequest) {
    Phone phone = new Phone();
    phone.setNumber(phoneRequest.getNumber());
    phone.setCityCode(phoneRequest.getCityCode());
    phone.setCountryCode(phoneRequest.getCountryCode());
    return phone;
  }

  /**
   * Converts a list of {@link Phone} entities into a list of {@link PhoneResponse} DTOs.
   *
   * @param phones the list of phone entities to convert; may be {@code null}
   * @return a list of {@link PhoneResponse} DTOs, or {@code null} if the input list is {@code null}
   */
  public static List<PhoneResponse> toResponses(List<Phone> phones) {
    if (phones == null) return null;

    return phones.stream().map(PhoneMapper::toResponse).collect(Collectors.toList());
  }

  /**
   * Converts a single {@link Phone} entity into a {@link PhoneResponse} DTO.
   *
   * @param phone the phone entity to convert
   * @return a {@link PhoneResponse} containing the phone details
   */
  public static PhoneResponse toResponse(Phone phone) {
    return new PhoneResponse(phone.getNumber(), phone.getCityCode(), phone.getCountryCode());
  }
}
